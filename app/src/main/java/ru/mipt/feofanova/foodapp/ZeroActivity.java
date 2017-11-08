package ru.mipt.feofanova.foodapp;

/**
 * Created by Olga Tharzhevskaya on 07.11.2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

public class ZeroActivity extends AppCompatActivity {

    static final String KEY_TIMER_VALUE_ = "TIMER";
    long milliSeconds = 5000;

    static CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
    }

    @Override
    public void onResume() {
        super.onResume();

        timer = new CountDownTimer(milliSeconds, 10) {

            public void onTick(long milliSec) {
                milliSeconds = milliSec;
            }

            public void onFinish() {
                Intent intent = new Intent(ZeroActivity.this, IngredientsActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        timer.cancel();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        milliSeconds = savedInstanceState.getLong(KEY_TIMER_VALUE_);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_TIMER_VALUE_, milliSeconds);
    }
}
