package com.example.programmer.womensafety;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashForWomenSafety extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_for_women_safety);
    tv=(TextView)findViewById(R.id.women);
    iv = (ImageView)findViewById(R.id.imageView);

    Thread myThread = new Thread(){

        @Override
        public void run() {

            try {
                sleep(3000);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }catch (InterruptedException a){
                a.printStackTrace();
            }

        }
    };
    myThread.start();
    }
}
