package com.elvis_c.elvis.stocktest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.Model.Company;

public class StockItemView extends RelativeLayout implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private TextView tv_StockName;
    private TextView tv_StockId;
    private TextView tv_StockPrice;
    private ImageView iv_StockPriceChange;
    private TextView tv_StockSpread;
    private TextView tv_StockPercentage;

    public StockItemView(Context context) {
        super(context);
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
        tv_StockSpread.setText(String.valueOf(setSpread(Float.valueOf(company.getY()), Float.valueOf(company.getZ()))));
    }

    private float setSpread(float f_y, float f_z){

        if (f_y > f_z) {
            return f_y - f_z;
        } else {
            return f_z - f_y;
        }
    }


    @Override
    public void onClick(View v) {

    }
}
