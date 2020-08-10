package com.elvis_c.elvis.stocktest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elvis_c.elvis.stocktest.Common.BaseDialog;
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
    private ImageView iv_deleteAll;
    private ProgressBar progressBar;

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
        iv_deleteAll = findViewById(R.id.iv_delete_all);
        progressBar = findViewById(R.id.progress);
        iv_deleteAll.setOnClickListener(onClickListener);
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

    @Override
    protected void onRestart() {
        super.onRestart();
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
            progressBar.setVisibility(View.GONE);
            companies.clear();
            companies.addAll(list);
            favoritesAdapter.updateList(companies);
            favoritesAdapter.notifyDataSetChanged();
        }
    };

    Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            dataSync();
            syncHandler.postDelayed(syncRunnable, 5000);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_delete_all) {
//                dataManagement.deleteAllFavorite();
//                Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_delete_all),
//                        Toast.LENGTH_SHORT).show();
                showDeleteDialog();
            }
        }
    };

    public void showDeleteDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnsActivity.this);
        builder.setMessage(getResources().getString(R.string.text_delete_dialog_title));
        builder.setPositiveButton(getResources().getText(R.string.text_delete_dialog_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dataManagement.deleteAllFavorite();
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_delete_all),
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getResources().getText(R.string.text_delete_dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncHandler.removeCallbacks(syncRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncData.unregisterSyncDataCallback();
        syncHandler.removeCallbacks(syncRunnable);
    }
}