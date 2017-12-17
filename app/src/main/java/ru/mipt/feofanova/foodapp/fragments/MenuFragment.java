package ru.mipt.feofanova.foodapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import android.support.design.widget.FloatingActionButton;

import ru.mipt.feofanova.foodapp.DBHelper;
import ru.mipt.feofanova.foodapp.FavouriteButtonColorChangerTask;
import ru.mipt.feofanova.foodapp.GsonMealObject;
import ru.mipt.feofanova.foodapp.ImageDownloaderTask;
import ru.mipt.feofanova.foodapp.R;
import ru.mipt.feofanova.foodapp.Singleton;


public class MenuFragment extends Fragment implements ImageDownloaderTask.IImageResponseListener, FavouriteButtonColorChangerTask.ColorChangerResponseListener
{
    TextView mTitleTextView;
    ImageView mMealPhoto;
    TextView mIngredientsTitleTextView;
    TextView mIngredientsList;
    TextView mRecipeTextView;
    TextView mRecipeDescriptionTextView;
    GsonMealObject currentMeal;
    private ImageDownloaderTask imgResponseTask;
    private DBHelper mDBHelper;
    public FloatingActionButton mFloatingActionButton;
    Boolean isInFavourite;
    Bitmap mCurrentDishPhoto;
    AppCompatActivity mActivity;

    @Override
    public void onResponse(Bitmap img, ImageView currentImage)
    {
        //currentImage.setImageBitmap(img);
        currentImage.setImageBitmap(img);
        mCurrentDishPhoto = img;
    }

    @Override
    public void onResponse(Boolean res)
    {
        if (res)
        {
            //TODO: change for normal pic and debug this (it isnt working now)
            mFloatingActionButton.setBackgroundColor(Color.BLACK);
            mFloatingActionButton.setImageResource(R.drawable.star);
            //mFloatingActionButton.notify();
        }
        isInFavourite = res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_fragment, container,
                false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();

        mTitleTextView = mActivity.findViewById(R.id.recipe_title);
        mMealPhoto = mActivity.findViewById(R.id.recipe_menu_image);
        mIngredientsTitleTextView = mActivity.findViewById(R.id.ingredients_string);
        mIngredientsList = mActivity.findViewById(R.id.ingredients_list);
        mRecipeTextView = mActivity.findViewById(R.id.recipe_string);
        mRecipeDescriptionTextView = mActivity.findViewById(R.id.recipe_description);

        mDBHelper = new DBHelper(mActivity);

        Bundle bundle = getArguments();
        int index = 0;
        if (bundle != null) {
            index = bundle.getInt("currentMealIndex");
        }
        currentMeal = Singleton.getInstance().getParsedJsonResp().get(index);
        //Log.e("----CURRENTMEAL",currentMeal.getTitle());

        mTitleTextView.setText(currentMeal.getTitle());

        mIngredientsList.setText(currentMeal.getIngredients());
        mRecipeDescriptionTextView.setText(currentMeal.getHref());
        mRecipeDescriptionTextView.setTextColor(Color.BLUE);
        mRecipeDescriptionTextView.setHighlightColor(Color.BLUE);

        imgResponseTask = new ImageDownloaderTask(currentMeal.getThumbnail(), mMealPhoto);
        imgResponseTask.delegate = MenuFragment.this;
        imgResponseTask.execute();


        mRecipeDescriptionTextView.setOnClickListener(new View.OnClickListener()
                                                      {
                                                          @Override
                                                          public void onClick(View view)
                                                          {
                                                              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentMeal.getHref())));
                                                          }
                                                      }
        );



        mFloatingActionButton = mActivity.findViewById(R.id.fab);
        //TODO: here is the checking for favourite dish, change the pic on onResponse(bool)
        FavouriteButtonColorChangerTask favouriteButtonColorChangerTask = new FavouriteButtonColorChangerTask(currentMeal.getTitle(), null, mActivity);
        favouriteButtonColorChangerTask.delegate = MenuFragment.this;
        favouriteButtonColorChangerTask.execute();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //save in favorites
                if (!isInFavourite)
                {
                    mFloatingActionButton.setImageResource(R.drawable.star);
                    mDBHelper.addValue(currentMeal.getTitle(), currentMeal.getIngredients(),
                            currentMeal.getHref(),
                            Calendar.getInstance().get(Calendar.DST_OFFSET) + "" +
                                    Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "",
                            currentMeal.getThumbnail(),
                            mCurrentDishPhoto);

                    //mIngredientsList.setText("sdfgh");
                }
                else
                {
                    //TODO: Message with text "already in favourite" (action bar all smth like that, Idont know)
                }



            }
        });
    }
}
