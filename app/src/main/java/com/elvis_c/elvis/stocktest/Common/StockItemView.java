package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.Model.Company;
import com.elvis_c.elvis.stocktest.R;

public class StockItemView extends RelativeLayout implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private TextView tv_StockName;
    private TextView tv_StockId;
    private TextView tv_StockPrice;
    private ImageView iv_StockPriceChange;
    private TextView tv_StockSpread;
    private TextView tv_StockPercentage;
    private StockItemClickCallback mStockItemClickCallback;

    public StockItemView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_item, null);
        this.addView(view);
    }

    public StockItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_item, null);
        this.addView(view);
    }

    public StockItemView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_item, null);
        this.addView(view);
    }

    public void initView(){
        tv_StockName = findViewById(R.id.tv_stockName);
        tv_StockId = findViewById(R.id.tv_stockId);
        tv_StockPrice = findViewById(R.id.tv_stockPrice);
        iv_StockPriceChange = findViewById(R.id.iv_priceChange);
        tv_StockSpread = findViewById(R.id.tv_stockSpread);
        tv_StockPercentage = findViewById(R.id.tv_stockPercentage);
    }

    public void setStockData(Company company) {
        tv_StockName.setText(company.getN());
        tv_StockId.setText(company.getC());
        tv_StockPrice.setText(company.getZ());
        tv_StockSpread.setText(String.format("%.2f", setSpread(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))));
        tv_StockPercentage.setText(String.format("%.2f", getPercentage(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))) + "%");
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

    public void setStockIndexData(){

    }

    private void setItemColor(boolean isRise){
        if (isRise) {
            tv_StockName.setTextColor(getResources().getColor(R.color.text_red));
            tv_StockId.setTextColor(getResources().getColor(R.color.text_red));
            tv_StockPrice.setTextColor(getResources().getColor(R.color.text_red));
            tv_StockSpread.setTextColor(getResources().getColor(R.color.text_red));
            tv_StockPercentage.setTextColor(getResources().getColor(R.color.text_red));
        } else {
            tv_StockName.setTextColor(getResources().getColor(R.color.text_green));
            tv_StockId.setTextColor(getResources().getColor(R.color.text_green));
            tv_StockPrice.setTextColor(getResources().getColor(R.color.text_green));
            tv_StockSpread.setTextColor(getResources().getColor(R.color.text_green));
            tv_StockPercentage.setTextColor(getResources().getColor(R.color.text_green));
        }
    }


    @Override
    public void onClick(View v) {
        mStockItemClickCallback.onClick();
    }

    public void registerStockItemCallback(StockItemClickCallback stockItemClickCallback){
        mStockItemClickCallback = stockItemClickCallback;
    }

    public void unregisterStockItemCallback(){
        mStockItemClickCallback = null;
    }

    interface StockItemClickCallback{
        void onClick();
    }
}
