package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataSet = new ArrayList<>();
    private final ArrayList<String> mUrlsSet = new ArrayList<>();
    //private LruCache<String, Bitmap> mMemoryCache;
    private String reqBody;
    private List<GsonMealObject> parsedJson;
    private Button prev;
    private Button next;
    private final String filename = "recipefile";
    private ImageDownloaderTask imgResponseTask;
    private ImageView mImage;
    private HttpGetRequestTask newMealsTask;
    private ProgressView mProgressNext;
    private String basicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_recipes_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecipesAdapter = new FavoriteActivity.mAdapter(mDataSet, mUrlsSet);
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
                mCardView = view.findViewById(R.id.recipe_card_view);
                mImageView = view.findViewById(R.id.recipe_card_view_image);
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
                    .inflate(R.layout.recipe_card_view, parent, false);

            return new FavoriteActivity.mAdapter.ViewHolder(view);
        }

        public int pos;

        @Override
        public void onBindViewHolder(final mAdapter.ViewHolder holder, final int position)
        {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            pos = position;
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка

            holder.mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //3-е активити
                    //Передать сюда нужные данные
                    Intent data = new Intent(FavoriteActivity.this, MenuActivity.class);
                }
            });


        }

        public int getItemCount()
        {
            return mDataSet.size();
        }
    }
}
