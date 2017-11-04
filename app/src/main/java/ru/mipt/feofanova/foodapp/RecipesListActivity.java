package ru.mipt.feofanova.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipesListActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataSet = new ArrayList<>();
    private final ArrayList<String> mUrlsSet = new ArrayList<>();
    //.Intent intent = new In
    private String reqBody;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        reqBody = getIntent().getStringExtra("reqBody");
        //String[] tmp  = new String[reqBody.length()];
        //tmp = reqBody.split("\"");
        //myDataset.addAll();
        for (GsonRecArray i : new Gson().fromJson(reqBody, GsonRequestSampleRec.class).getResults())
        {
            mDataSet.add(i.getTitle());
            mUrlsSet.add(i.getThumbnail());
        }



        //String[] mDataSet = getResources().getStringArray(R.array.number_strings);
        mRecipesAdapter = new mAdapter(mDataSet, mUrlsSet);
        mRecyclerView.setAdapter(mRecipesAdapter);
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>
    {
        private ArrayList<String> mDataSet;
        private ArrayList<String> mUrlsSet;

        class ViewHolder extends RecyclerView.ViewHolder
        {
            CardView mCardView;
            ImageView mImageView;
            TextView mTextView;

            public ViewHolder(View view)
            {
                super(view);
                mCardView = view.findViewById(R.id.card_view);
                mImageView = view.findViewById(R.id.img);
                mTextView = view.findViewById(R.id.recipe_name);
            }
        }

        public mAdapter(ArrayList<String> inputDataDet, ArrayList<String> urls)
        {
            mDataSet = inputDataDet;
            mUrlsSet = urls;
            //new ImageDownloaderTask((ImageView))
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_button, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            holder.mTextView.setText(mDataSet.get(position));
            holder.mImageView.setImageResource(R.drawable.pic);
            new ImageDownloaderTask(mUrlsSet.get(position), holder.mImageView).execute(); ///args
            holder.mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //Start 3rd activity
                    //holder.mTextView.setText("ouch!");
                }
            });
        }

        public int getItemCount()
        {
            return mDataSet.size();
        }

    }
}
