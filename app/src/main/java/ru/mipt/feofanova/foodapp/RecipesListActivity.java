package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecipesListActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataSet = new ArrayList<>();
    private final ArrayList<String> mUrlsSet = new ArrayList<>();
    //private LruCache<String, Bitmap> mMemoryCache;
    private String reqBody;
    private List<GsonRecArray> parsedJson;
    private Button prev;
    private Button next;
    private final String filename = "recipefile";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // parsedJson = new List<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        reqBody = getIntent().getStringExtra("reqBody");

        parsedJson = new Gson().fromJson(reqBody, GsonRequestSampleRec.class).getResults();

        for (GsonRecArray i : parsedJson)
        {
            mDataSet.add(i.getTitle());
            mUrlsSet.add(i.getThumbnail());
        }


        //String[] mDataSet = getResources().getStringArray(R.array.number_strings);
        mRecipesAdapter = new mAdapter(mDataSet, mUrlsSet);
        mRecyclerView.setAdapter(mRecipesAdapter);

        prev = (Button) findViewById(R.id.prev_button);
        next = (Button) findViewById(R.id.next_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear mDataSet
                //add to mDataSet and mUrlsSet new items
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear mDataSet
                //add to mDataSet and mUrlsSet new items
            }
        });

    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>
    {
        private ArrayList<String> mDataSet;
        private ArrayList<String> mUrlsSet;

        //public int position;
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

        public int pos;

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка
            new ImageDownloaderTask(url, holder.mImageView).execute(); ///args
            pos = position;
            holder.mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //3-е активити
                    //Передать сюда нужные данные
                    Intent data = new Intent(RecipesListActivity.this, MenuActivity.class);

                    //Singleton.parsedJsonResp = parsedJson;
                    Singleton.setParsedJsonResp(parsedJson);
                    data.putExtra("currentMealIndex", position);
                    setResult(RESULT_OK, data);
                    startActivity(data);

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
