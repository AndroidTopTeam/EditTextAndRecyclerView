package ru.mipt.feofanova.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Bitmap;
import android.util.Log;

import java.sql.SQLWarning;
import java.util.ArrayList;

/**
 * Created by Талгат on 02.12.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "favourites";
    public static final int DB_VER = 1;

    private static final String CREATE_DB_REQUEST =
            "CREATE TABLE favourites (id INTEGER PRIMARY KEY AUTOINCREMENT, dish TEXT, ingredients TEXT, recipe TEXT, imageHref TEXT);";

    private static final class Factory implements SQLiteDatabase.CursorFactory {
        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            return new SQLiteCursor(sqLiteCursorDriver, s, sqLiteQuery);
        }
    }

    public DBHelper(Context context) {
        this(context, DB_NAME, new Factory(), DB_VER);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_REQUEST);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        final String regionQuery = "select Count(*) as count from favourites";
        Cursor cur = null;
        int result = 0;
        if (db != null) {
            try {
                cur = db.rawQuery(regionQuery, null);
                cur.moveToFirst();
                result = cur.getInt(cur.getColumnIndexOrThrow("count"));
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return result;
    }

    public int countValue(String name) {
        SQLiteDatabase db = getReadableDatabase();
        final String regionQuery = "select Count(*) as count from favourites where dish = ?";
        Cursor cur = null;
        int result = -1;
        if (db != null) {
            try {
                cur = db.rawQuery(regionQuery, new String[]{name});
                cur.moveToFirst();
                result = cur.getInt(cur.getColumnIndexOrThrow("count"));
                //result = cur.getPosition();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return result;
    }

    public void remove(String name) {
        /*SQLiteOpenHelper db = getReadableDatabase();
        final String regionQuery = "select * from favourites where dish = ?";
        Cursor cur = null;
        if(db!=null)
        {
            try
            {

            }
        }*/
        int size = getCount();
        if (size > 0) {
            SQLiteDatabase db = getWritableDatabase();
            if (db != null) {
                db.delete("favourites", "dish=?", new String[]{String.valueOf(name)});
                db.close();
            }
        }
    }

    public int getPositionOf(String name) {
        SQLiteDatabase db = getReadableDatabase();
        final String regionQuery = "select Count(*) as count from favourites where dish = ?";
        Cursor cur = null;
        int result = -1;
        if (db != null) {
            try {
                cur = db.rawQuery(regionQuery, new String[]{name});
                cur.moveToFirst();
                //result = cur.getInt(cur.getColumnIndexOrThrow("count"));
                result = cur.getPosition();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return result;
    }

    public void addValue(String name, String ingredients, String recipe, String imageHref) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dish", name);
            contentValues.put("ingredients", ingredients);
            contentValues.put("recipe", recipe);
            //contentValues.put("imagePath", path);
            contentValues.put("imageHref", imageHref);
            db.insert("favourites", null, contentValues);
            db.close();
            //ImageExternalStorageSaver saver = new ImageExternalStorageSaver(path, img);
            //saver.save();
        }
    }

    public void editValue(int position, String name, String dish, String ingredients, String recipe, String imageHref) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("dish", name);
            contentValues.put("ingredients", ingredients);
            contentValues.put("recipe", recipe);
            //contentValues.put("imagePath", path);
            contentValues.put("imageHref", imageHref);
            db.update("favourites", contentValues, "id =  ?", new String[]{String.valueOf(position)});
            db.close();
        }
    }

    public ArrayList<ArrayList<String>> getAllValues() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ArrayList<String>> res = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery("select dish, ingredients, recipe, imageHref from favourites", null);
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    ArrayList<String> temp = new ArrayList<>();
                    //values.add(cur.getString(cur.getColumnIndexOrThrow("ingredients")));
                    try {
                        temp.clear();
                        temp.add(cur.getString(cur.getColumnIndexOrThrow("dish")));
                        temp.add(cur.getString(cur.getColumnIndexOrThrow("ingredients")));
                        temp.add(cur.getString(cur.getColumnIndexOrThrow("recipe")));
                        //values.add(cur.getString(cur.getColumnIndexOrThrow("imagePath")));
                        temp.add(cur.getString(cur.getColumnIndexOrThrow("imageHref")));
                        res.add(temp);
                        cur.moveToNext();
                    } catch (Exception e) {
                        Log.e("Error in getting str", e.getMessage());
                    }

                }
            }
            cur.close();
            db.close();
            return res;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        db.close();
        return res;
    }

    public ArrayList<String> getValues(int position) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = null;
        int id = 0;
        ArrayList<String> values = new ArrayList<>();

        if (db != null) {
            try {
                //"CREATE TABLE favourites (id INTEGER PRIMARY KEY AUTOINCREMENT, dish TEXT, ingredients TEXT, recipe TEXT, imageHref TEXT);"
                cur = db.query("favourites",
                        new String[]{"id", "dish", "ingredients", "recipe", "imageHref"},
                        "id = ?", new String[]{String.valueOf(position)},
                        null, null, null);

                cur.moveToFirst();
                /*if(cur.getCount() <= 0)
                    removeAt(cur.getPosition());*/
                id = cur.getInt(cur.getColumnIndexOrThrow("id"));
                values.add(cur.getString(cur.getColumnIndexOrThrow("dish")));
                values.add(cur.getString(cur.getColumnIndexOrThrow("ingredients")));
                values.add(cur.getString(cur.getColumnIndexOrThrow("recipe")));
                //values.add(cur.getString(cur.getColumnIndexOrThrow("imagePath")));
                values.add(cur.getString(cur.getColumnIndexOrThrow("imageHref")));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return values;
    }

    public void removeLast() {
        int size = getCount();
        if (size > 0) {
            SQLiteDatabase db = getWritableDatabase();
            if (db != null) {
                db.delete("favourites", "id=?", new String[]{String.valueOf(size)});
                db.close();
            }
        }
    }

    public void removeAt(int index) {
        int size = getCount();
        if (size > 0) {
            SQLiteDatabase db = getWritableDatabase();
            if (db != null) {
                db.delete("favourites", "id=?", new String[]{String.valueOf(index)});
                db.close();
            }
        }
    }


}
