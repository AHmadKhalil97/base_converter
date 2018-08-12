package com.scriptorient.baseconverter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "ubunto_regular.ttf");
        ((TextView) findViewById(R.id.title)).setTypeface(typeface);

        try {
            getSupportActionBar().hide();
        }
        catch (Exception e) {

        }


        try {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < 100; i++) {
                        progressBar.setProgress(i);

                        if (i == 99) {
                            progressBar.setProgress(100);
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
        catch (Exception e) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1800);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
