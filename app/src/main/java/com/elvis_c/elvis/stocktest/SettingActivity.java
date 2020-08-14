package com.elvis_c.elvis.stocktest;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_version = findViewById(R.id.tv_version);

        String VersionName = null;
        try {
            VersionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText(getResources().getString(R.string.text_demo) + "\n" + "Version : " + VersionName);
    }
}