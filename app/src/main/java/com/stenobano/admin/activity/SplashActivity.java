package com.stenobano.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.stenobano.admin.Lazy;
import com.stenobano.admin.MainActivity;
import com.stenobano.admin.R;
import com.stenobano.admin.session.SesssionManager;

import java.util.HashMap;
import java.util.Map;

import static com.stenobano.admin.session.SesssionManager.NAME;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT =4000;
    Context mContext = this;
    String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        id=new SesssionManager(this).userID();
        finish();
        if (Lazy.haveNetworkConnection(mContext) == true) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (id == null) {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        i.putExtra("a", "0");
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);

        } else {
            Lazy.networkDialog(mContext);
        }
    }


    @Override
    protected void onResume() {

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if (id==null) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtra("a", "0");
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

        super.onResume();
    }
}