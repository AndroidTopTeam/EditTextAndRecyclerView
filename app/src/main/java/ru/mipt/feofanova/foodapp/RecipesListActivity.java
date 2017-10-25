package ru.mipt.feofanova.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

public class RecipesListActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        myRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        myRecyclerView.setHasFixedSize(true);

        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        String[] myDataset = getResources().getStringArray(R.array.number_strings);
        myAdapter = new MyAdapter(myDataset);
        myRecyclerView.setAdapter(myAdapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] myDataset;

        class ViewHolder extends RecyclerView.ViewHolder {
            public Button myButton;
            public ViewHolder(Button button) {
                super(button);
                myButton = button;
            }
        }

        public MyAdapter(String[] inputDataset) {
            myDataset = inputDataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            Button button = (Button)LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_button, parent, false);

            return new ViewHolder(button);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.myButton.setText(myDataset[position]);
        }

        public int getItemCount() {
            return myDataset.length;
        }

    }
}
