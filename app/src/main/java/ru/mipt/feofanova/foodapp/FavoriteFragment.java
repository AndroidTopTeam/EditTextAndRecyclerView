package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FavoriteFragment extends Fragment implements ImageDownloaderTask.IImageResponseListener
{
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
    private DBHelper mDBHelper;
    private int numFavourite;
    //private ArrayList<GsonMealObject> parsedJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite, container,
                false);

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_favorite);

        mRecyclerView = getActivity().findViewById(R.id.favorite_recipes_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecipesAdapter = new FavoriteFragment.mAdapter(mDataSet, mUrlsSet);
        mRecyclerView.setAdapter(mRecipesAdapter);
        parsedJson = new ArrayList<>();
        mDBHelper = new DBHelper(getActivity());
        //mDataSet
        int numFavourite = mDBHelper.getCount();
        ArrayList<String> favourites;
        for (int i = 0; i < numFavourite; ++i)
        {
            favourites = mDBHelper.getValues(i + 1);
            GsonMealObject temp = new GsonMealObject(favourites.get(0), favourites.get(2), favourites.get(1), favourites.get(4));
            parsedJson.add(temp);
            mDataSet.add(favourites.get(0));
            mUrlsSet.add(favourites.get(4));
        }


    }

    @Override
    public void onResponse(Bitmap img, ImageView currentImage)
    {
        //currentImage.setImageBitmap(img);
        currentImage.setImageBitmap(img);
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

            return new FavoriteFragment.mAdapter.ViewHolder(view);
        }

        public int pos;

        @Override
        public void onBindViewHolder(final mAdapter.ViewHolder holder, final int position)
        {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            pos = position;
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка

            imgResponseTask = new ImageDownloaderTask(url, holder.mImageView); ///args
            imgResponseTask.delegate = FavoriteFragment.this;
            mImage = holder.mImageView;
            imgResponseTask.execute();


            holder.mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //3-е активити
                    //Передать сюда нужные данные
                    Intent data = new Intent(getActivity(), MenuActivity.class);
                    Singleton.setParsedJsonResp(parsedJson);
                    data.putExtra("currentMealIndex", position);
                    getActivity().setResult(RESULT_OK, data);
                    startActivity(data);
                }
            });


        }

        public int getItemCount()
        {
            return mDataSet.size();
        }
    }
}
