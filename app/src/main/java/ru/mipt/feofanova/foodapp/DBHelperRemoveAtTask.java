package ru.mipt.feofanova.foodapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import ru.mipt.feofanova.foodapp.fragments.MenuFragment;

/**
 * Created by talgat on 24.12.17.
 */

public class DBHelperRemoveAtTask extends AsyncTask<Void, Void, Void>
{
    DBHelper mDBHelper;
    String dish;
    AppCompatActivity mActivity;
    public DBHelperRemoveAtTask(AppCompatActivity activity, String name)
    {
        dish = name;
        mActivity = activity;
        mDBHelper = new DBHelper(mActivity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mDBHelper.remove(dish);
        return null;
    }
}
