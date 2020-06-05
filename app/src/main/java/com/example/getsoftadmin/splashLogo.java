package com.example.getsoftadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class splashLogo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);
        final UserSession userSession = new UserSession(this);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!userSession.getMobile().equals("")){

                    Intent intent = new Intent(splashLogo.this, Homepage.class);
                    System.out.println("error");
                    finish();
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(splashLogo.this,UserLogin.class);
                    finish();
                    startActivity(intent);
                }
            }
        },2000);
    }
}
