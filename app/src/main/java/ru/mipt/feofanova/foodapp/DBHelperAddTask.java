package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import ru.mipt.feofanova.foodapp.DBHelper;

/**
 * Created by talgat on 24.12.17.
 */

public class DBHelperAddTask extends AsyncTask<Void, Void, Void> {
    DBHelper mDBHelper;
    String name;
    String ingredients;
    String recipe;
    String path;
    String imageHref;
    Bitmap img;

    public DBHelperAddTask(AppCompatActivity activity, String pname, String pingredients, String precipe, String ppath, String pimageHref, Bitmap pimg) {
        name = pname;
        ingredients = pingredients;
        recipe = precipe;
        path = ppath;
        imageHref = pimageHref;
        img = pimg;
        mDBHelper = new DBHelper(activity);
    }


    @Override
    protected Void doInBackground(Void... voids) {
        mDBHelper.addValue(name, ingredients, recipe, imageHref);
        return null;
    }
}
