package com.elvis_c.elvis.stocktest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.elvis_c.elvis.stocktest.Common.HomeItemView;
import com.elvis_c.elvis.stocktest.Common.StockItemView;
import com.elvis_c.elvis.stocktest.Model.Company;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private StockItemView stockItemView;
    private SyncData syncData;
    private Handler syncHandler;
    private String url = DataManagement.urlData + DataManagement.twIndex;
    private HomeItemView hv_search;
    private HomeItemView hv_owns;
    private HomeItemView hv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        dataSync();
        syncHandler = new Handler();
    }

    private void initView(){
        stockItemView = findViewById(R.id.si_view);
        stockItemView.initView();
        hv_search = findViewById(R.id.hv_search);
        hv_owns = findViewById(R.id.hv_owns);
        hv_setting = findViewById(R.id.hv_setting);
        hv_search.setOnClickListener(onClickListener);
        hv_owns.setOnClickListener(onClickListener);
        hv_setting.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.hv_search:
                    syncData.unregisterSyncDataCallback();
                    syncHandler.removeCallbacks(syncRunnable);
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.hv_owns:
                    syncData.unregisterSyncDataCallback();
                    syncHandler.removeCallbacks(syncRunnable);
                    Intent intent_owns = new Intent();
                    intent_owns.setClass(HomeActivity.this, OwnsActivity.class);
                    startActivity(intent_owns);
                    break;
                case R.id.hv_setting:
                    syncData.unregisterSyncDataCallback();
                    syncHandler.removeCallbacks(syncRunnable);
                    Intent intent_setting = new Intent();
                    intent_setting.setClass(HomeActivity.this, SettingActivity.class);
                    startActivity(intent_setting);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        syncHandler.postDelayed(syncRunnable, 5000);
    }

    SyncData.SyncDataCallback syncDataCallback = new SyncData.SyncDataCallback() {
        @Override
        public void SyncFinish(List<Company> list) {
            stockItemView.setStockData(DataManagement.getInstance(getApplicationContext()).getStockInfo().getMsgArray().get(0));
        }
    };

    Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            dataSync();
            syncHandler.postDelayed(syncRunnable, 5000);
        }
    };

    private void dataSync(){
        syncData = new SyncData();
        syncData.setmCtx(getApplicationContext());
        syncData.execute(url);
        syncData.registerSyncDataCallback(syncDataCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncData.unregisterSyncDataCallback();
        syncHandler.removeCallbacks(syncRunnable);
    }
}
