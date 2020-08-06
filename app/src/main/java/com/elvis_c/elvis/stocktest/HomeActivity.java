package com.elvis_c.elvis.stocktest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.elvis_c.elvis.stocktest.Common.StockItemView;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

public class HomeActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private StockItemView stockItemView;
    private SyncData syncData;
    private Handler syncHandler;
    private String url = DataManagement.urlData + DataManagement.twIndex;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        syncHandler.postDelayed(syncRunnable, 5000);
    }

    SyncData.SyncDataCallback syncDataCallback = new SyncData.SyncDataCallback() {
        @Override
        public void SyncFinish() {
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
