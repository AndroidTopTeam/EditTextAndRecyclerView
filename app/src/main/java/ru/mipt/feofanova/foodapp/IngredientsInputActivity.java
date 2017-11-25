package ru.mipt.feofanova.foodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.rey.material.widget.ProgressView;

import java.util.ArrayList;


public class IngredientsInputActivity extends AppCompatActivity implements HttpGetRequestTask.IResponseListener
{
    private static final String INGREDIENTS_KEY_ = "INGREDIENTS";

    private EditText mEditText;
    private Button mFindButton;
    private String reqBody;
    private IngredientsInputActivity.mAdapter mAdapter;
    private HttpGetRequestTask req;
    private final ArrayList<String> ingredients = new ArrayList<>();
    private ProgressView mProgressView;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String basicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        //req.delegate = this;
        basicUrl = "";
        mEditText = (EditText) findViewById(R.id.edit_text);
        mFindButton = (Button) findViewById(R.id.find_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recipes_recycler_view);
        mProgressView = (ProgressView) findViewById(R.id.ingredients_progress_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new mAdapter(this,
                R.layout.ingredient_button, ingredients);
        mRecyclerView.setAdapter(mAdapter);

        mEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        ingredients.add(0, mEditText.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        mEditText.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        mFindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RequestUrlCreator creator = new RequestUrlCreator(ingredients);
                req = new HttpGetRequestTask(
                        (basicUrl = creator.makeRequestString()),
                        mProgressView,
                        (ViewGroup) findViewById(R.id.rel));

                req.delegate = IngredientsInputActivity.this;
                req.execute();
            }
        });
    }

    @Override
    public void onResponse(String res)
    {
        reqBody = res;
        //Log.e("REQBODY", reqBody);
        Intent data = new Intent(IngredientsInputActivity.this, MealsListActivity.class);
        data.putExtra("reqBody", reqBody);
        data.putExtra("basicUrl", basicUrl);
        setResult(RESULT_OK, data);
        startActivity(data);
    }

    class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>
    {
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<String> mDataSet;

        mAdapter(Context context, int resource, ArrayList<String> list)
        {
            mDataSet = list;
            layout = resource;
            inflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final Button button;
            final TextView name;

            ViewHolder(View view)
            {
                super(view);
                button = view.findViewById(R.id.ingredient_button);
                name = view.findViewById(R.id.ingredient_name);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            holder.name.setText(mDataSet.get(position));
            final mAdapter adapter = this;
            holder.button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mDataSet.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        public int getItemCount()
        {
            return mDataSet.size();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(INGREDIENTS_KEY_, ingredients);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        ingredients.addAll(0, savedInstanceState.getStringArrayList(INGREDIENTS_KEY_));
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled)
    {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup)
            {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

}
