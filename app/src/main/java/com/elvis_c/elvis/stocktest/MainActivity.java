package com.elvis_c.elvis.stocktest;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_test);
        button = findViewById(R.id.btn_test);
        DataManagement.getInstance(getApplicationContext());
        Log.d(TAG, "onCreate, test 1");
        String url = DataManagement.urlData + DataManagement.test;
        syncData = new SyncData();
        syncData.setmCtx(getApplicationContext());
        syncData.execute(url);
        new Thread(runnable).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockInfo = DataManagement.getInstance(getApplicationContext()).getStockInfo();
                textView.setText(stockInfo.getRtcode());
                DataManagement.getInstance(getApplicationContext()).readCsvFile();
                for (int i = 0; i < DataManagement.getInstance(getApplicationContext()).getAllCompanyCodes().size(); i++) {
                    Log.d(TAG, "Test, company code = " + DataManagement.getInstance(getApplicationContext()).getAllCompanyCodes().get(i).getCompanyCode());
                    Log.d(TAG, "Test, company name = " + DataManagement.getInstance(getApplicationContext()).getAllCompanyCodes().get(i).getCompanyName());
                }
            }
        });

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

}
