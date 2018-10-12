package com.example.team32gb.jobit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlashScreenActivity extends AppCompatActivity {
    private TextView tvVersion;
    private ProgressBar prgLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        prgLoading = findViewById(R.id.prgLoading);
        tvVersion = findViewById(R.id.tv_version);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(getString(R.string.tv_version) + " " + packageInfo.versionName);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FlashScreenActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }, 1000);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
