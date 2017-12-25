package ru.mipt.feofanova.foodapp.fragments;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;

import android.os.Bundle;
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

import ru.mipt.feofanova.foodapp.GsonMealObject;
import ru.mipt.feofanova.foodapp.GsonMealsObjectsList;
import ru.mipt.feofanova.foodapp.HttpGetRequestTask;
import ru.mipt.feofanova.foodapp.ImageDownloaderTask;
import ru.mipt.feofanova.foodapp.R;
import ru.mipt.feofanova.foodapp.RequestUrlCreator;
import ru.mipt.feofanova.foodapp.Singleton;

import static ru.mipt.feofanova.foodapp.NavigationActivity.fragment;
import static ru.mipt.feofanova.foodapp.NavigationActivity.mFragmentManager;

public class MealsListFragment extends Fragment implements ImageDownloaderTask.IImageResponseListener, HttpGetRequestTask.IResponseListener {

    private static final String RECIPY_NAMES_KEY_ = "RECYPIES";
    private static final String PICTURE_URLS_KEY_ = "PICTURE_URLS";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<String> mDataSet1 = new ArrayList<>();
    private final ArrayList<String> mUrlsSet1 = new ArrayList<>();
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
        if (bundle != null && basicUrl == null && reqBody == null) {
            basicUrl = bundle.getString("basicUrl");
            reqBody = bundle.getString("reqBody");

        }

        if (savedInstanceState == null) {
            if (mDataSet1.isEmpty()) {
                try {
                    Log.e("REQBODY", reqBody);
                    if (reqBody.length() > 0)
                        parsedJson = new Gson().fromJson(reqBody, GsonMealsObjectsList.class).getResults();
                } catch (Exception e) {
                    Log.e("Gson err, empy list", e.getMessage());
                }
                for (GsonMealObject i : parsedJson) {
                    mDataSet1.add(i.getTitle());
                    mUrlsSet1.add(i.getThumbnail());
                }
            }
        } else {
            mDataSet1.addAll(0, savedInstanceState.getStringArrayList(RECIPY_NAMES_KEY_));
            mUrlsSet1.addAll(0, savedInstanceState.getStringArrayList(PICTURE_URLS_KEY_));
            basicUrl = savedInstanceState.getString("basicUrl");
            reqBody = savedInstanceState.getString("reqBody");
            try {
                parsedJson = new Gson().fromJson(reqBody, GsonMealsObjectsList.class).getResults();
            } catch (Exception e) {
                Log.e("Err gson, empty json", e.getMessage());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        NavigationView navigationView = mActivity.findViewById(R.id.navigation_view);
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //pageNumber = 1;
        // parsedJson = new List<>();
        //basicUrl = mActivity.getIntent().getStringExtra("basicUrl");
        mProgressNext = mActivity.findViewById(R.id.progress_next);
        mRecyclerView = mActivity.findViewById(R.id.recipes_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //String[] mDataSet1 = getResources().getStringArray(R.array.number_strings);
        mRecipesAdapter = new mAdapter(mDataSet1, mUrlsSet1);
        mRecyclerView.setAdapter(mRecipesAdapter);

        prev = mActivity.findViewById(R.id.prev_button);
        next = mActivity.findViewById(R.id.next_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSet1.clear();
                mUrlsSet1.clear();
                try {
                    RequestUrlCreator creator = new RequestUrlCreator();
                    Log.e("", "---------------------------------------------------------------");

                    Log.e("----------------BSICURL", basicUrl);
                    basicUrl = creator.changePage(basicUrl, -1);
                    Log.e("----------------BSICURL", basicUrl);
                    newMealsTask = new HttpGetRequestTask(basicUrl, mProgressNext, (ViewGroup) mActivity.findViewById(R.id.recipes_recycler_view));
                    newMealsTask.delegate = MealsListFragment.this;
                    newMealsTask.execute();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                //next.setActivated(true);
                next.setEnabled(true);
                mRecipesAdapter.notifyDataSetChanged();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataSet1.clear();
                mUrlsSet1.clear();
                try {
                    RequestUrlCreator creator = new RequestUrlCreator();
                    Log.e("", "---------------------------------------------------------------");

                    Log.e("----------------BSICURL", basicUrl);
                    basicUrl = creator.changePage(basicUrl, +1);

                    if(( basicUrl.charAt(basicUrl.length() - 1) == '9')) {
                        return;
                    }
                    Log.e("----------------BSICURL", basicUrl);
                    newMealsTask = new HttpGetRequestTask(basicUrl, mProgressNext, (ViewGroup) mActivity.findViewById(R.id.recipes_recycler_view));
                    newMealsTask.delegate = MealsListFragment.this;
                    try {
                        newMealsTask.execute();
                    } catch (Exception e) {
                        Log.e("ErrorRequest", e.getMessage());
                        next.setEnabled(false);
                        mRecipesAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    //next.setActivated(false);
                    next.setEnabled(false);
                    mDataSet1.clear();
                    mUrlsSet1.clear();
                    mRecipesAdapter.notifyDataSetChanged();
                }
                mRecipesAdapter.notifyDataSetChanged();

            }
        });

    }
/*
    @Override
    public void onPause()
    {
        super.onPause();
        mRecipesAdapter.notifyDataSetChanged();
    }
*/

    @Override
    public void onResponse(Bitmap img, ImageView currentImage) {
        currentImage.setImageBitmap(img);
    }

    @Override
    public void onResponse(String res) {
        reqBody = res;
        if (reqBody != "404") {
            try {
                parsedJson = new Gson().fromJson(reqBody, GsonMealsObjectsList.class).getResults();
            } catch (Exception e) {
                Log.e("Err gson, empty json", e.getMessage());
            }
            for (GsonMealObject i : parsedJson) {
                mDataSet1.add(i.getTitle());
                mUrlsSet1.add(i.getThumbnail());
            }
            mRecipesAdapter.notifyDataSetChanged();
        }
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder> {
        private ArrayList<String> mDataSet;
        private ArrayList<String> mUrlsSet;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CardView mCardView;
            ImageView mImageView;
            TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mCardView = view.findViewById(R.id.recipe_card_view);
                mImageView = view.findViewById(R.id.recipe_card_view_image);
                mTextView = view.findViewById(R.id.recipe_name);
                mCardView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Singleton.setParsedJsonResp(parsedJson);

                fragment = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("currentMealIndex", getAdapterPosition());
                fragment.setArguments(bundle);

                mFragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(null).commit();
            }
        }

        public mAdapter(ArrayList<String> inputDataDet, ArrayList<String> urls) {
            mDataSet = inputDataDet;
            mUrlsSet = urls;
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_card_view, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка

            imgResponseTask = new ImageDownloaderTask(url, holder.mImageView); ///args
            imgResponseTask.delegate = MealsListFragment.this;
            mImage = holder.mImageView;
            imgResponseTask.execute();
        }

        public int getItemCount() {
            return mDataSet.size();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(RECIPY_NAMES_KEY_, mDataSet1);
        outState.putStringArrayList(PICTURE_URLS_KEY_, mUrlsSet1);

        outState.putString("basicUrl", basicUrl);
        outState.putString("reqBody", reqBody);
    }
}
