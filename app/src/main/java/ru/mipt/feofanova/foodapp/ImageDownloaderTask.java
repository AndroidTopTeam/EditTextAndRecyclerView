package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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

    public ImageDownloaderTask(String exUrl, ImageView img)
    {
        url = exUrl;
        bmImage = img;
    }

    @Override
    protected Bitmap doInBackground(Void... voids)
    {
        Bitmap mIcon = null;
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
        return mIcon;
    }

    protected void onPostExecute(Bitmap res)
    {
        bmImage.setImageBitmap(res);
    }
}
