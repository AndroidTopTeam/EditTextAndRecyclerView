package ru.mipt.feofanova.foodapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.rey.material.widget.FloatingActionButton;

/**
 * Created by Талгат on 03.12.2017.
 */

public class FavouriteButtonColorChangerTask extends AsyncTask<Void, Void, Boolean>
{
    private Color mFavoutiteButtonColor;
    DBHelper mDBHelper;
    Context mContext;
    String mDish;
    FloatingActionButton addToFavouritesButton;

    public interface ColorChangerResponseListener
    {
        void onResponse(Boolean res);
    }

    public ColorChangerResponseListener delegate = null;

    public FavouriteButtonColorChangerTask(String dish, Color toDraw, Context context)
    {
        mFavoutiteButtonColor = toDraw;
        mDBHelper = new DBHelper(context);
        mDish = dish;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        int numCurrentFavourites = mDBHelper.countValue(mDish);
        return (numCurrentFavourites > 0);
    }

    @Override
    protected void onPostExecute(Boolean res)
    {
        //mFloatingActionButton
        //addToFavouritesButton = mContext.findViewById(R.id.fab);
        super.onPostExecute(res);
        delegate.onResponse(res);
    }
}
