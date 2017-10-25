package ru.mipt.feofanova.foodapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class IngredientsActivity extends AppCompatActivity
{
    private EditText mEditText;
    private ListView mListView;
    private Button mFindButton;
    private String reqBody;
    //private ArrayList<String> ingreds;
    private final ArrayList<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mListView = (ListView) findViewById(R.id.list_view);
        mFindButton = (Button) findViewById(R.id.find_button);


        final MyAdapter mAdapter;
        mAdapter = new MyAdapter(this,
                R.layout.ingredient_button, ingredients);
        mListView.setAdapter(mAdapter);

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
                    IngredientsActivity.this.finish();
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
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String>
    {
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<String> mList;

        MyAdapter(Context context, int resource, ArrayList<String> list)
        {
            super(context, resource, list);
            this.mList = list;
            this.layout = resource;
            this.inflater = LayoutInflater.from(context);
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {

            final ViewHolder viewHolder;
            if (convertView == null)
            {
                convertView = inflater.inflate(this.layout, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String str = mList.get(position);
            viewHolder.name.setText(str);


            final MyAdapter adapter = this;
            viewHolder.button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder
        {
            final Button button;
            final TextView name;

            ViewHolder(View view)
            {
                button = view.findViewById(R.id.ingredient_button);
                name = view.findViewById(R.id.name);
            }
        }

    }

}
