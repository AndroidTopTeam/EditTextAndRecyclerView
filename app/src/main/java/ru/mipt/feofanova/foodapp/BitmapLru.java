package ru.mipt.feofanova.foodapp;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLru {

    static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    static final int cacheSize = maxMemory / 8;

    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
    {
        @Override
        protected int sizeOf(String key, Bitmap bitmap)
        {
            return bitmap.getByteCount() / 1024;
        }
    };
}
