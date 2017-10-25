package ru.mipt.feofanova.foodapp;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Талгат on 25.10.2017.
 */

public class HttpGetRequest extends AsyncTask<Void, Void, String>
{
    private String reqUrl;
    private OkHttpClient client;
    private final Request request;
    private String responseBody;

    public HttpGetRequest(String url)
    {
        reqUrl = url;

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

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return responseBody;
    }

    @Override
    protected void onPostExecute(String res)
    {
        super.onPostExecute(res);
    }


}
