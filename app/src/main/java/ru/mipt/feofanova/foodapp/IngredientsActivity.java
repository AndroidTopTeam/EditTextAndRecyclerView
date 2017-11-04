package ru.mipt.feofanova.foodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.ArrayList;


public class IngredientsActivity extends AppCompatActivity implements HttpGetRequest.IResponseListener
{
    private static final String INGREDIENTS_KEY_ = "INGREDIENTS";

    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private Button mFindButton;
    private String reqBody;
    private MyAdapter mAdapter;
<<<<<<< HEAD
    private RecyclerView.LayoutManager mLayoutManager;
=======
    private HttpGetRequest req;
    //private ArrayList<String> ingreds;
>>>>>>> talgat
    private final ArrayList<String> ingredients = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        //req.delegate = this;

        mEditText = (EditText) findViewById(R.id.edit_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mFindButton = (Button) findViewById(R.id.find_button);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(this,
                R.layout.ingredient_element, ingredients);
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
<<<<<<< HEAD

                try
                {
                    RequestCreator creator = new RequestCreator(ingredients, null, null);
                    HttpGetRequest req = new HttpGetRequest(creator.makeRequestString());
                    req.execute();
                    reqBody = req.get();
                    Log.e("REQBODY", reqBody);
                    Intent data = new Intent(IngredientsActivity.this, RecipesListActivity.class);
                    data.putExtra("reqBody", reqBody);
                    setResult(RESULT_OK, data);
                    startActivity(data);
                    //IngredientsActivity.this.finish();
                } /*catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }*/
                catch(Exception e)
                {
                    e.printStackTrace();
                }
=======
                RequestCreator creator = new RequestCreator(ingredients, null, null);
                req = new HttpGetRequest(creator.makeRequestString());
                req.delegate = IngredientsActivity.this;
                req.execute();
                //IngredientsActivity.this.finish();
>>>>>>> talgat
            }
        });
    }

<<<<<<< HEAD
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
=======
    @Override
    public void onResponse(String res)
    {
        reqBody = res;
        Log.e("REQBODY", reqBody);
        Intent data = new Intent(IngredientsActivity.this, RecipesListActivity.class);
        data.putExtra("reqBody", reqBody);
        setResult(RESULT_OK, data);
        startActivity(data);
    }

    class MyAdapter extends ArrayAdapter<String>
>>>>>>> talgat
    {
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<String> mDataSet;

        public MyAdapter(Context context, int resource, ArrayList<String> list)
        {
            mDataSet = list;
            layout = resource;
            inflater = LayoutInflater.from(context);
        }
/*
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            final ViewHolder viewHolder;
            if (convertView == null)
            {
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String str = mDataSet.get(position);
            viewHolder.ingredient.setText(str);


            final MyAdapter adapter = this;
            viewHolder.button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mDataSet.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
*/
        class ViewHolder extends RecyclerView.ViewHolder
        {
            public final Button button;
            public final TextView ingredient;

            ViewHolder(View view)
            {
                super(view);
                button = view.findViewById(R.id.ingredient_button);
                ingredient = view.findViewById(R.id.name);
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
            holder.ingredient.setText(mDataSet.get(position));
            final MyAdapter adapter = this;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(INGREDIENTS_KEY_, ingredients);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ingredients.addAll(0, savedInstanceState.getStringArrayList(INGREDIENTS_KEY_));
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

}
