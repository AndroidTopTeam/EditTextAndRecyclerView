package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class RecipesListActivity extends AppCompatActivity
{

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private final ArrayList<String> myDataset = new ArrayList<>();
    //.Intent intent = new In
    private String reqBody;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        myRecyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);
        reqBody = getIntent().getStringExtra("reqBody");
        String[] tmp  = new String[reqBody.length()];
        tmp = reqBody.split("\"");

        for(String s: tmp)
            myDataset.add(s);
        myDataset.add(reqBody);

        //String[] myDataset = getResources().getStringArray(R.array.number_strings);
        myAdapter = new MyAdapter(myDataset);
        myRecyclerView.setAdapter(myAdapter);

    }

    /*@Override
    protected void onActivityResult(int code, int result, Intent data)
    {
        if (result == RESULT_OK)
        {
            if (data.hasExtra("reqBody"))
            {
                reqBody = data.getStringExtra("reqBody");
            }
        }
    }*/

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
    {
        private ArrayList<String> myDataset;

        class ViewHolder extends RecyclerView.ViewHolder
        {
            public Button myButton;

            public ViewHolder(Button button)
            {
                super(button);
                myButton = button;
            }
        }

        public MyAdapter(ArrayList<String> inputDataset)
        {
            myDataset = inputDataset;
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
            holder.myButton.setText(myDataset.get(position));
        }

        public int getItemCount()
        {
            return myDataset.size();
        }

    }
}
