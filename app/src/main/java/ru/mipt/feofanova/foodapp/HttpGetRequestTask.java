package ru.mipt.feofanova.foodapp;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ProgressView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static ru.mipt.feofanova.foodapp.fragments.IngredientsInputFragment.enableDisableViewGroup;

/**
 * Created by Талгат on 25.10.2017.
 */

public class HttpGetRequestTask extends AsyncTask<Void, Void, String>
{
    private String reqUrl;
    private OkHttpClient client;
    private final Request request;
    private String responseBody;
    private ProgressView mProgressView;
    private ViewGroup mViewGroup;

    public interface IResponseListener
    {
        void onResponse(String data);
    }

    public IResponseListener delegate = null;

    public HttpGetRequestTask(String url, ProgressView progressView, ViewGroup viewGroup)
    {
        mProgressView = progressView;
        mViewGroup = viewGroup;

        mProgressView.setVisibility(View.VISIBLE);
        enableDisableViewGroup(viewGroup, false);

        reqUrl = url;
        //this.delegate =  delegate;

        /*client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();*/
        client = new OkHttpClient();

        request = new Request.Builder()
                .url(reqUrl)
                .build();
    }

    @Override
    protected String doInBackground(Void... params)
    {

        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        }
        catch (IOException e)
        {
            return "404";
        }

        return responseBody;
    }

    @Override
    protected void onPostExecute(String res)
    {
        super.onPostExecute(res);
        mProgressView.setVisibility(View.INVISIBLE);
        enableDisableViewGroup(mViewGroup, true);
        delegate.onResponse(res);
    }


}
