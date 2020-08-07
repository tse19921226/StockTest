package com.elvis_c.elvis.stocktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.Model.StockInfo;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    SyncData syncData;
    TextView textView;
    Button button;
    StockInfo stockInfo;
    DataManagement dataManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_test);
        button = findViewById(R.id.btn_test);
        dataManagement = DataManagement.getInstance(getApplicationContext());
        dataManagement.registerCsvDownloadCallback(csvDownloadCallBack);
        Log.d(TAG, "onCreate, test 1");
//        String url = DataManagement.urlData + DataManagement.test;
//        syncData = new SyncData();
//        syncData.setmCtx(getApplicationContext());
//        syncData.execute(url);
        new Thread(runnable).start();

        //test start
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockInfo = dataManagement.getStockInfo();
                textView.setText(stockInfo.getRtcode());
                dataManagement.readCsvFile();
                for (int i = 0; i < dataManagement.getAllCompanyCodes().size(); i++) {
                    Log.d(TAG, "Test, company code = " + dataManagement.getAllCompanyCodes().get(i).getCompanyCode());
                    Log.d(TAG, "Test, company name = " + dataManagement.getAllCompanyCodes().get(i).getCompanyName());
                }
            }
        });
        //test end

    }

    DataManagement.CsvDownloadCallBack csvDownloadCallBack = new DataManagement.CsvDownloadCallBack() {
        @Override
        public void downloadFinish() {
            dataManagement.readCsvFile();
            gotoNextPage();
        }
    };

    private void gotoNextPage(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DataManagement.downloadCsv();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataManagement.unregisterCsvDownloadCallback();
    }
}
