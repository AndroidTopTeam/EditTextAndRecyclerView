package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Талгат on 03.12.2017.
 */

public class ImageExternalStorageSaver
{
    private String mSaveFileName;
    private Bitmap imgToSave;
    public static final String IMAGE_DIR = "/saved_images/";


    public ImageExternalStorageSaver(String name, Bitmap img)
    {
        mSaveFileName = name;
        imgToSave = img;
    }

    public void save()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + IMAGE_DIR );

            myDir.mkdirs();
            mSaveFileName += ".jpg";
            File file = new File(myDir, mSaveFileName);

            if (file.exists())
            {
                file.delete();
            }
            try
            {
                FileOutputStream out = new FileOutputStream(file);
                imgToSave.compress(Bitmap.CompressFormat.JPEG, 120, out);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }

}
