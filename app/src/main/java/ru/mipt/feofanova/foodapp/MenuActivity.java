package ru.mipt.feofanova.foodapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity
{
    TextView mTitleTextView;
    ImageView mMealPhoto;
    TextView mIngredientsTitleTextView;
    TextView mIngredientsList;
    TextView mRecipeTextView;
    TextView mRecipeDescriptionTextView;
    GsonRecArray currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTitleTextView = (TextView)findViewById(R.id.title);
        mMealPhoto = (ImageView)findViewById(R.id.photo);
        mIngredientsTitleTextView = (TextView)findViewById(R.id.ingredients);
        mIngredientsList = (TextView) findViewById(R.id.ingredients_list);
        mRecipeTextView = (TextView)findViewById(R.id.recipe);
        mRecipeDescriptionTextView = (TextView)findViewById(R.id.recipe_description);

        int index = getIntent().getIntExtra("currentMealIndex", 0);
        currentMeal = Singleton.getInstance().getParsedJsonResp().get(index);
        //Log.e("----CURRENTMEAL",currentMeal.getTitle());

        mTitleTextView.setText(currentMeal.getTitle());

        mIngredientsList.setText(currentMeal.getIngredients());
        mRecipeDescriptionTextView.setText(currentMeal.getHref());
        mRecipeDescriptionTextView.setTextColor(Color.BLUE);
        mRecipeDescriptionTextView.setHighlightColor(Color.BLUE);
        new ImageDownloaderTask(currentMeal.getThumbnail(), mMealPhoto).execute();
        mRecipeDescriptionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentMeal.getHref())));
            }
        }
        );
    }
}
