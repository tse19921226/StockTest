package com.elvis_c.elvis.stocktest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elvis_c.elvis.stocktest.Common.StockInfoItem;
import com.elvis_c.elvis.stocktest.Model.Company;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StockInfoActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private TextView tv_InfoTitle;
    private ImageView iv_Previous;
    private ImageView iv_Next;
    private ImageView iv_Add;
    private TextView tv_stock_price;
    private TextView tv_stock_spread;
    private TextView tv_date;
    private StockInfoItem si_c, si_spread, si_percent, si_tv, si_v, si_y, si_o, si_h, si_l;

    private int PageType;
    private String StockID;
    private Handler syncHandler;
    private SyncData syncData;
    private String url;
    private ArrayList<Company> mArrayList = new ArrayList<>();
    private Company company;
    private DataManagement dataManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);
        Intent intent = this.getIntent();
        PageType = intent.getIntExtra("TYPE", 1001);
        StockID = intent.getStringExtra("STOCK_ID");
        url = DataManagement.urlData + String.format(DataManagement.stockCode, StockID);
        Log.d(TAG, "onCreate, url = " + url);
        dataManagement = DataManagement.getInstance(getApplicationContext());
        initView();
        dataSync();
        syncHandler = new Handler();
    }

    private void initView(){
        tv_InfoTitle = findViewById(R.id.tv_stockName);
        iv_Previous = findViewById(R.id.iv_previous);
        iv_Next = findViewById(R.id.iv_next);
        iv_Add = findViewById(R.id.iv_add);
        tv_stock_price = findViewById(R.id.tv_stock_price);
        tv_stock_spread = findViewById(R.id.tv_stock_spread);
        tv_date = findViewById(R.id.tv_date);
        si_c = findViewById(R.id.si_c);
        si_spread = findViewById(R.id.si_spread);
        si_percent = findViewById(R.id.si_percent);
        si_tv = findViewById(R.id.si_tv);
        si_v = findViewById(R.id.si_v);
        si_y = findViewById(R.id.si_y);
        si_o = findViewById(R.id.si_o);
        si_h = findViewById(R.id.si_h);
        si_l = findViewById(R.id.si_l);
        iv_Previous.setOnClickListener(onClickListener);
        iv_Next.setOnClickListener(onClickListener);
        iv_Add.setOnClickListener(onClickListener);
        if (PageType == DataManagement.TYPE_NO_GROUP) {
            iv_Next.setVisibility(View.GONE);
            iv_Previous.setVisibility(View.GONE);
            iv_Add.setVisibility(View.VISIBLE);
        } else {
            iv_Next.setVisibility(View.VISIBLE);
            iv_Previous.setVisibility(View.VISIBLE);
            iv_Add.setVisibility(View.GONE);
        }
    }

    private void setData(){
        tv_InfoTitle.setText(company.getN());
        tv_stock_price.setText(dataManagement.FormatFloat(Float.valueOf(company.getZ())));
//        tv_stock_spread.setText(String.format("%.2f", setSpread(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))));
        tv_stock_spread.setText(dataManagement.FormatFloat(setSpread(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))));
        tv_date.setText(dataManagement.currentTime2DateString());
        si_c.setInfoText(dataManagement.FormatFloat(Float.valueOf(company.getZ())));
        si_spread.setInfoText(tv_stock_spread.getText().toString());
//        si_percent.setInfoText(String.format("%.2f", getPercentage(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))) + "%");
        si_percent.setInfoText(dataManagement.FormatFloat(getPercentage(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))) + "%");
        si_tv.setInfoText(company.getTv());
        si_v.setInfoText(company.getV());
        si_y.setInfoText(dataManagement.FormatFloat(Float.valueOf(company.getY())));
        si_o.setInfoText(dataManagement.FormatFloat(Float.valueOf(company.getO())));
        si_h.setInfoText(dataManagement.FormatFloat(Float.valueOf(company.getH())));
        si_l.setInfoText(dataManagement.FormatFloat(Float.valueOf(company.getL())));
    }

    private float setSpread(float f_y, float f_z){//f_y昨收, f_z當盤成交價
        Log.d(TAG, "setSpread, f_y = " + f_y);
        Log.d(TAG, "setSpread, f_z = " + f_z);
        if (f_y > f_z) {
            setItemColor(false);
            return f_y - f_z;
        } else {
            setItemColor(true);
            return f_z - f_y;
        }
    }

    private float getPercentage (float f_y, float f_z){
        return (f_z - f_y) / f_y * 100;
    }


    private void setItemColor(boolean isRise) {
        if (isRise) {
            tv_InfoTitle.setTextColor(getResources().getColor(R.color.text_red));
            tv_stock_price.setTextColor(getResources().getColor(R.color.text_red));
            tv_stock_spread.setTextColor(getResources().getColor(R.color.text_red));
            tv_date.setTextColor(getResources().getColor(R.color.text_red));
            si_c.setInfoColor(getResources().getColor(R.color.text_red));
            si_spread.setInfoColor(getResources().getColor(R.color.text_red));
            si_percent.setInfoColor(getResources().getColor(R.color.text_red));
            si_tv.setInfoColor(getResources().getColor(R.color.text_red));
            si_v.setInfoColor(getResources().getColor(R.color.text_red));
            si_y.setInfoColor(getResources().getColor(R.color.text_red));
            si_o.setInfoColor(getResources().getColor(R.color.text_red));
            si_h.setInfoColor(getResources().getColor(R.color.text_red));
            si_l.setInfoColor(getResources().getColor(R.color.text_red));
        } else {
            tv_InfoTitle.setTextColor(getResources().getColor(R.color.text_green));
            tv_stock_price.setTextColor(getResources().getColor(R.color.text_green));
            tv_stock_spread.setTextColor(getResources().getColor(R.color.text_green));
            tv_date.setTextColor(getResources().getColor(R.color.text_green));
            si_c.setInfoColor(getResources().getColor(R.color.text_green));
            si_spread.setInfoColor(getResources().getColor(R.color.text_green));
            si_percent.setInfoColor(getResources().getColor(R.color.text_green));
            si_tv.setInfoColor(getResources().getColor(R.color.text_green));
            si_v.setInfoColor(getResources().getColor(R.color.text_green));
            si_y.setInfoColor(getResources().getColor(R.color.text_green));
            si_o.setInfoColor(getResources().getColor(R.color.text_green));
            si_h.setInfoColor(getResources().getColor(R.color.text_green));
            si_l.setInfoColor(getResources().getColor(R.color.text_green));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncHandler.postDelayed(syncRunnable, 5000);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_previous:
                    break;
                case R.id.iv_next:
                    break;
                case R.id.iv_add:
                    dataManagement.addData2DB(StockID);
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.text_add_new_data),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
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

    SyncData.SyncDataCallback syncDataCallback = new SyncData.SyncDataCallback() {
        @Override
        public void SyncFinish(List<Company> list) {
            Log.d(TAG, "SyncFinish, list.size = " + list.size());
            mArrayList.addAll(list);
            company = list.get(0);
            setData();
        }
    };
}