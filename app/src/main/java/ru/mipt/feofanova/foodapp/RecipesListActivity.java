package ru.mipt.feofanova.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class RecipesListActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataset = new ArrayList<>();
    private String reqBody;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        reqBody = getIntent().getStringExtra("reqBody");
        String[] tmp  = new String[reqBody.length()];
<<<<<<< HEAD
        tmp = reqBody.split("\"");

        for(String s: tmp)
            mDataset.add(s);
        mDataset.add(reqBody);

        //String[] mDataset = getResources().getStringArray(R.array.number_strings);
        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

=======
>>>>>>> talgat
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        private ArrayList<String> mDataset;

        public MyAdapter(ArrayList<String> inputDataset)
        {
            mDataset = inputDataset;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            public Button button;

            public ViewHolder(Button button)
            {
                super(button);
                this.button = button;
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType)
        {
            Button button = (Button) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_button, parent, false);

            return new ViewHolder(button);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            holder.button.setText(mDataset.get(position));
        }

        public int getItemCount()
        {
            return mDataset.size();
        }

    }
}
