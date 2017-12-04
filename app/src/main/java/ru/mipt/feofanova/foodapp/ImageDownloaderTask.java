package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.ImageView;
import android.support.v4.util.LruCache;
import android.widget.Toast;

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


    public interface IImageResponseListener
    {
        void onResponse(Bitmap img, ImageView currentImage);
    }

    public IImageResponseListener delegate;// = null;

    public ImageDownloaderTask(String exUrl, ImageView img)
    {
        url = exUrl;
        bmImage = img;
        delegate = null;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromMemoryCache(key) == null && key != null && bitmap != null)
        {
            BitmapLru.mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key)
    {
        return BitmapLru.mMemoryCache.get(key);
    }


    @Override
    protected Bitmap doInBackground(Void... voids)
    {
        Bitmap mIcon = null;
        if (BitmapLru.mMemoryCache != null)
        {
            mIcon = getBitmapFromMemoryCache(url);
            if (mIcon != null)
            {
                return mIcon;
            }
        }
        try
        {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e)
        {
            Log.e("Error with img download", e.getMessage());
            e.printStackTrace();
        } catch (IOException e)
        {
            Log.e("Error with img download", e.getMessage());
            e.printStackTrace();
        }
        if (BitmapLru.mMemoryCache != null)
        {
            addBitmapToMemoryCache(url, mIcon);
        }
        return mIcon;
    }

    @Override
    protected void onPostExecute(Bitmap res)
    {
        super.onPostExecute(res);
        //bmImage.setImageBitmap(res);
        delegate.onResponse(res, bmImage);
       /* try
        {
            delegate.onResponse(res, bmImage);
        } catch (Exception e)
        {
            Log.e("!!!!!wqesr", "!!!!!!!!!!!qwerdtfgwerdtfgh");
        }*/
    }
}