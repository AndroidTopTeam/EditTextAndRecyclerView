package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Талгат on 03.12.2017.
 */

public class ImageExternalStorageLoading
{
    private String mFileName;



    public ImageExternalStorageLoading(String name)
    {
        mFileName = name;
    }

    public Bitmap load() //throws RuntimeException
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            String root = Environment.getExternalStorageDirectory().toString();
            String path = (root + ImageExternalStorageSaver.IMAGE_DIR + mFileName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            //Uri imageUri = Uri.
            return bitmap;
        }
        //throw new RuntimeException;
        return null;
    }

}
