package com.idit.gasomovil;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final int DURATION_SPLASH = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        //Init Firebase Auth
        try{

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (RuntimeException e){
            Log.e("ERROR", e.getMessage());
        }
        mAuth = FirebaseAuth.getInstance();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                //Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //Check already session, if ok -> MainActivity
                if (mAuth.getCurrentUser() != null)
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, BannerActivity.class));
                finish();
            }
        }, DURATION_SPLASH);
    }

}
