package ru.mipt.feofanova.foodapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static ru.mipt.feofanova.foodapp.NavigationActivity.fragment;

public class MealsListFragment extends Fragment implements ImageDownloaderTask.IImageResponseListener, HttpGetRequestTask.IResponseListener
{

    private static final String RECIPY_NAMES_KEY_ = "RECYPIES";
    private static final String PICTURE_URLS_KEY_ = "PICTURE_URLS";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataSet = new ArrayList<>();
    private final ArrayList<String> mUrlsSet = new ArrayList<>();
    //private LruCache<String, Bitmap> mMemoryCache;
    private String reqBody;
    private List<GsonMealObject> parsedJson;
    private Button prev;
    private Button next;
    private final String filename = "recipefile";
    private ImageDownloaderTask imgResponseTask;
    private ImageView mImage;
    private HttpGetRequestTask newMealsTask;
    private ProgressView mProgressNext;
    private String basicUrl;
    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meals_list, container,
                false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            basicUrl = bundle.getString("basicUrl");
            reqBody = bundle.getString("reqBody");
        }

        if (savedInstanceState == null) {
            //reqBody = mActivity.getIntent().getStringExtra("reqBody");

            parsedJson = new Gson().fromJson(reqBody, GsonMealsObjectsList.class).getResults();

            for (GsonMealObject i : parsedJson) {
                mDataSet.add(i.getTitle());
                mUrlsSet.add(i.getThumbnail());
            }
        } else {
            mDataSet.addAll(0, savedInstanceState.getStringArrayList(RECIPY_NAMES_KEY_));
            mUrlsSet.addAll(0, savedInstanceState.getStringArrayList(PICTURE_URLS_KEY_));
            basicUrl = savedInstanceState.getString("basicUrl");
            reqBody = savedInstanceState.getString("reqBody");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //pageNumber = 1;
        // parsedJson = new List<>();
        //basicUrl = mActivity.getIntent().getStringExtra("basicUrl");
        mProgressNext = (ProgressView) mActivity.findViewById(R.id.progress_next);
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.recipes_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //String[] mDataSet = getResources().getStringArray(R.array.number_strings);
        mRecipesAdapter = new mAdapter(mDataSet, mUrlsSet);
        mRecyclerView.setAdapter(mRecipesAdapter);

        prev = (Button) mActivity.findViewById(R.id.prev_button);
        next = (Button) mActivity.findViewById(R.id.next_button);
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //clear mDataSet
                //add to mDataSet and mUrlsSet new items
                mDataSet.clear();
                mUrlsSet.clear();
                RequestUrlCreator creator = new RequestUrlCreator();
                Log.e("", "---------------------------------------------------------------");

                Log.e("----------------BSICURL", basicUrl);
                basicUrl = creator.changePage(basicUrl, -1);
                Log.e("----------------BSICURL", basicUrl);
                newMealsTask = new HttpGetRequestTask(basicUrl, mProgressNext, (ViewGroup) mActivity.findViewById(R.id.recipes_recycler_view));
                newMealsTask.delegate = MealsListFragment.this;
                newMealsTask.execute();
                //mRecyclerView.updateViewLayout(view);
                //  mRecyclerView.invalidate();

                //RequestUrlCreator creator = new RequestUrlCreator(parsedJson.get(0).getIngredients(), null, new ArrayList<String>().add("2"));
                //String url = creator.makeRequestString();
                //HttpGetRequestTask req = new HttpGetRequestTask();
                //mRecyclerView.updateViewLayout();
                mRecipesAdapter.notifyDataSetChanged();
            }
        });

        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //clear mDataSet
                //add to mDataSet and mUrlsSet new items
                mDataSet.clear();
                mUrlsSet.clear();
                RequestUrlCreator creator = new RequestUrlCreator();

                Log.e("", "---------------------------------------------------------------");

                Log.e("----------------BSICURL", basicUrl);
                basicUrl = creator.changePage(basicUrl, +1);
                Log.e("----------------BSICURL", basicUrl);
                newMealsTask = new HttpGetRequestTask(basicUrl, mProgressNext, (ViewGroup) mActivity.findViewById(R.id.recipes_recycler_view));
                newMealsTask.delegate = MealsListFragment.this;
                newMealsTask.execute();
                //mRecyclerView.updateViewLayout(view);
                //  mRecyclerView.invalidate();

                //RequestUrlCreator creator = new RequestUrlCreator(parsedJson.get(0).getIngredients(), null, new ArrayList<String>().add("2"));
                //String url = creator.makeRequestString();
                //HttpGetRequestTask req = new HttpGetRequestTask();
                //mRecyclerView.updateViewLayout();
                mRecipesAdapter.notifyDataSetChanged();

            }
        });

    }


    @Override
    public void onResponse(Bitmap img, ImageView currentImage)
    {
        //currentImage.setImageBitmap(img);
        currentImage.setImageBitmap(img);
    }

    @Override
    public void onResponse(String res)
    {
        reqBody = res;
        if (reqBody != "404")
        {
            parsedJson = new Gson().fromJson(reqBody, GsonMealsObjectsList.class).getResults();

            for (GsonMealObject i : parsedJson)
            {
                mDataSet.add(i.getTitle());
                mUrlsSet.add(i.getThumbnail());
            }

            mRecipesAdapter.notifyDataSetChanged();
        }
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>
    {
        private ArrayList<String> mDataSet;
        private ArrayList<String> mUrlsSet;

        //public int position;
        class ViewHolder extends RecyclerView.ViewHolder
        {
            CardView mCardView;
            ImageView mImageView;
            TextView mTextView;

            public ViewHolder(View view)
            {
                super(view);
                mCardView = view.findViewById(R.id.recipe_card_view);
                mImageView = view.findViewById(R.id.recipe_card_view_image);
                mTextView = view.findViewById(R.id.recipe_name);
            }
        }

        public mAdapter(ArrayList<String> inputDataDet, ArrayList<String> urls)
        {
            mDataSet = inputDataDet;
            mUrlsSet = urls;
            //new ImageDownloaderTask((ImageView))
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_card_view, parent, false);

            return new ViewHolder(view);
        }

        public int pos;

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            pos = position;
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка

            imgResponseTask = new ImageDownloaderTask(url, holder.mImageView); ///args
            imgResponseTask.delegate = MealsListFragment.this;
            mImage = holder.mImageView;
            imgResponseTask.execute();

            holder.mCardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //3-е активити
                    //Передать сюда нужные данные
                    //Intent data = new Intent(mActivity, MenuActivity.class);

                    //Singleton.parsedJsonResp = parsedJson;
                    Singleton.setParsedJsonResp(parsedJson);
                    /*data.putExtra("currentMealIndex", position);
                    mActivity.setResult(RESULT_OK, data);
                    startActivity(data);*/

                    try {
                        fragment = MenuActivity.class.newInstance();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentMealIndex", position);
                    fragment.setArguments(bundle);

                    FragmentManager mFragmentManager = mActivity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, fragment);
                    fragmentTransaction.commit();
                    //holder.mTextView.setText("ouch!");
                }
            });


        }

        public int getItemCount()
        {
            return mDataSet.size();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(RECIPY_NAMES_KEY_, mDataSet);
        outState.putStringArrayList(PICTURE_URLS_KEY_, mUrlsSet);

        outState.putString("basicUrl", basicUrl);
        outState.putString("reqBody", reqBody);
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled)
    {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup)
            {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }
}
