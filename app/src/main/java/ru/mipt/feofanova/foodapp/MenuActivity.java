package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity implements ImageDownloaderTask.IImageResponseListener, FavouriteButtonColorChangerTask.ColorChangerResponseListener
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

    @Override
    public void onResponse(Bitmap img, ImageView currentImage)
    {
        //currentImage.setImageBitmap(img);
        currentImage.setImageBitmap(img);
    }

    @Override
    public void onResponse(Boolean res)
    {
        if (res)
        {
            //TODO: change for normal pic and debug this (it isnt working now)
            mFloatingActionButton.setBackgroundColor(Color.BLACK);
            mFloatingActionButton.setImageResource(R.drawable.pic);
        }
        isInFavourite = res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTitleTextView = (TextView) findViewById(R.id.recipe_title);
        mMealPhoto = (ImageView) findViewById(R.id.recipe_menu_image);
        mIngredientsTitleTextView = (TextView) findViewById(R.id.ingredients_string);
        mIngredientsList = (TextView) findViewById(R.id.ingredients_list);
        mRecipeTextView = (TextView) findViewById(R.id.recipe_string);
        mRecipeDescriptionTextView = (TextView) findViewById(R.id.recipe_description);

        mDBHelper = new DBHelper(this);


        int index = getIntent().getIntExtra("currentMealIndex", 0);
        currentMeal = Singleton.getInstance().getParsedJsonResp().get(index);
        //Log.e("----CURRENTMEAL",currentMeal.getTitle());

        mTitleTextView.setText(currentMeal.getTitle());

        mIngredientsList.setText(currentMeal.getIngredients());
        mRecipeDescriptionTextView.setText(currentMeal.getHref());
        mRecipeDescriptionTextView.setTextColor(Color.BLUE);
        mRecipeDescriptionTextView.setHighlightColor(Color.BLUE);

        imgResponseTask = new ImageDownloaderTask(currentMeal.getThumbnail(), mMealPhoto);
        imgResponseTask.delegate = MenuActivity.this;
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



        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        //TODO: here is the checking for favourite dish, change the pic on onResponse(bool)
        FavouriteButtonColorChangerTask favouriteButtonColorChangerTask = new FavouriteButtonColorChangerTask(currentMeal.getTitle(), null, this);
        favouriteButtonColorChangerTask.delegate = MenuActivity.this;
        favouriteButtonColorChangerTask.execute();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //save in favorites
                if (!isInFavourite)
                {
                    mDBHelper.addValue(currentMeal.getTitle(), currentMeal.getIngredients(),
                            currentMeal.getHref(),
                            Calendar.getInstance().get(Calendar.DST_OFFSET) + "" +
                                    Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + "");

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
