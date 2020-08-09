package com.elvis_c.elvis.stocktest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.elvis_c.elvis.stocktest.Common.FavoritesAdapter;
import com.elvis_c.elvis.stocktest.Model.Company;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

import java.util.ArrayList;
import java.util.List;

public class OwnsActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private RecyclerView rl_favorite;

    private DataManagement dataManagement;
    private SyncData syncData;
    private String url;
    private Handler syncHandler;
    private ArrayList<String> StockIds = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private ArrayList<Company> companies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owns);
        initView();
        initUrl();
        syncHandler = new Handler();
    }

    private void initView(){
        rl_favorite = findViewById(R.id.rl_favorite);
        dataManagement = DataManagement.getInstance(getApplicationContext());
        dataManagement.db2favoriteList();
        StockIds = dataManagement.getFavoriteList();
        Log.d(TAG, "initView, StockIds.size = " + StockIds.size());
        favoritesAdapter = new FavoritesAdapter(getApplicationContext());
        rl_favorite.setLayoutManager(new GridLayoutManager(this, 2));
        rl_favorite.setAdapter(favoritesAdapter);
    }

    private void initUrl(){
        url = DataManagement.urlData;
        for (int i = 0; i < StockIds.size(); i++) {
            Log.d(TAG, "initUrl StockIds.get(i) = " + StockIds.get(i));
            if (i != StockIds.size() - 1) {
                url = url + String.format(DataManagement.stockCode, StockIds.get(i)) + "|";
            } else {
                url = url + String.format(DataManagement.stockCode, StockIds.get(i));
            }
        }
        Log.d(TAG, "initUrl url = " + url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncHandler.postDelayed(syncRunnable, 5000);
    }

    private void dataSync(){
        syncData = new SyncData();
        syncData.setmCtx(getApplicationContext());
        syncData.execute(url);
        syncData.registerSyncDataCallback(syncDataCallback);
    }

    SyncData.SyncDataCallback syncDataCallback = new SyncData.SyncDataCallback() {
        @Override
        public void SyncFinish(List<Company> list) {
            companies.clear();
            companies.addAll(list);
            favoritesAdapter.updateList(companies);
        }
    };

    Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            dataSync();
            syncHandler.postDelayed(syncRunnable, 5000);
        }
    };
}