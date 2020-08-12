package com.elvis_c.elvis.stocktest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private String deleteStockTemp;

    private enum DeleteType{
        ONE,
        ALL
    }

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
        favoritesAdapter.registerCallback(adapterItemClick);
        favoritesAdapter.registerLongClickCallback(longClickCallback);
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
        syncHandler.postDelayed(syncRunnable, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        syncHandler.postDelayed(syncRunnable, 0);
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
                showDeleteDialog(DeleteType.ALL);
            }
        }
    };

    FavoritesAdapter.AdapterItemClick adapterItemClick = new FavoritesAdapter.AdapterItemClick() {
        @Override
        public void onItemClick(String StockID) {
            Log.d(TAG, "onItemClick, StockID = " + StockID);
            Intent intent = new Intent();
            intent.setClass(OwnsActivity.this, StockInfoActivity.class);
            intent.putExtra("TYPE", DataManagement.TYPE_GROUP);
            intent.putExtra("STOCK_ID", StockID);
            startActivity(intent);
        }
    };

    FavoritesAdapter.AdapterItemLongClickCallback longClickCallback = new FavoritesAdapter.AdapterItemLongClickCallback() {
        @Override
        public void onLongTouch(String StockID) {
            Log.d(TAG, "onLongTouch, StockID = " + StockID);
            deleteStockTemp = StockID;
            showDeleteDialog(DeleteType.ONE);
        }
    };

    public void showDeleteDialog(final DeleteType deleteType) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnsActivity.this);
        if (deleteType == DeleteType.ONE) {
            builder.setMessage(getResources().getString(R.string.text_delete_dialog_title));
        } else {
            builder.setMessage(getResources().getString(R.string.text_delete_dialog_title));
        }
        builder.setPositiveButton(getResources().getText(R.string.text_delete_dialog_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (deleteType == DeleteType.ONE) {
                    deleteOne();
                } else {
                    deleteAll();
                }
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

    private void deleteOne(){
        dataManagement.deleteData2DB(deleteStockTemp);
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_delete_all),
                Toast.LENGTH_SHORT).show();
        StockIds = dataManagement.getFavoriteList();
        initUrl();
        closeSync();
        dataSync();
        syncHandler.postDelayed(syncRunnable, 0);
    }

    private void deleteAll(){
        dataManagement.deleteAllFavorite();
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_delete_all),
                Toast.LENGTH_SHORT).show();
        StockIds.clear();
        companies.clear();
        closeSync();
        favoritesAdapter.updateList(companies);
        favoritesAdapter.notifyDataSetChanged();
    }

    private void closeSync(){
        syncHandler.removeCallbacks(syncRunnable);
        syncData.unregisterSyncDataCallback();
        syncData = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncHandler.removeCallbacks(syncRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritesAdapter.unregisterCallback();
        favoritesAdapter.unregisterLongClickCallback();
        syncData.unregisterSyncDataCallback();
        syncHandler.removeCallbacks(syncRunnable);
    }
}