package com.elvis_c.elvis.stocktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elvis_c.elvis.stocktest.controller.DataManagement;

public class HomeActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private StockItemView stockItemView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView(){
        stockItemView = findViewById(R.id.si_view);
        stockItemView.initView();
        stockItemView.setStockData(DataManagement.getInstance(getApplicationContext()).getStockInfo().getMsgArray().get(0));
    }



    @Override
    protected void onResume() {
        super.onResume();
    }
}
