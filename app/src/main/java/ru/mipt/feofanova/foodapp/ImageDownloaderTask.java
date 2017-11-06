package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.support.v4.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Талгат on 05.11.2017.
 */

public class ImageDownloaderTask extends AsyncTask<Void, Void, Bitmap>
{
    private ImageView bmImage;
    private String url;
    private LruCache<String, Bitmap> mMemoryCache;

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public ImageDownloaderTask(String exUrl, ImageView img, LruCache<String, Bitmap> memCache)
    {
        url = exUrl;
        bmImage = img;
        mMemoryCache = memCache;
    }

    @Override
    protected Bitmap doInBackground(Void... voids)
    {
        Bitmap mIcon = null;
        mIcon = getBitmapFromMemoryCache(url);
        if (mIcon != null) {
            return mIcon;
        }
        try
        {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        }
        catch (MalformedURLException e)
        {
            Log.e("Error with img download", e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.e("Error with img download", e.getMessage());
            e.printStackTrace();
        }
        addBitmapToMemoryCache(url, mIcon);
        return mIcon;
    }

    protected void onPostExecute(Bitmap res)
    {
        bmImage.setImageBitmap(res);
    }
}