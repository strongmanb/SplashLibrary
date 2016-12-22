package com.strongmanb.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.strongman.splash.SplashLayout;

public class SplashActivity extends AppCompatActivity {

    private SplashLayout mSplashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.mSplashLayout = (SplashLayout) findViewById(R.id.splash_layout);
        //set image from res
        this.mSplashLayout.setSplashImage(R.mipmap.splash_bg);
        //set image from net
        //this.mSplashLayout.setSplashImage("http://b.zol-img.com.cn/sjbizhi/images/4/320x510/1368514518611.jpg");

        mSplashLayout.startCount();
        this.mSplashLayout.setCounterStopListener(new SplashLayout.CounterListener() {

            @Override
            public void stop() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mSplashLayout.pauseCount();
        super.onDestroy();
    }
}
