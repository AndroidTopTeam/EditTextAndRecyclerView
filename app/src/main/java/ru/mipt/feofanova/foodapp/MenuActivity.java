package ru.mipt.feofanova.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView outText = (TextView) this.findViewById(R.id.recipe_description);
        outText.setMovementMethod(new ScrollingMovementMethod());
    }
}
