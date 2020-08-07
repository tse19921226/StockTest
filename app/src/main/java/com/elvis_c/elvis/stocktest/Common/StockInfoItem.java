package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.R;

public class StockInfoItem extends RelativeLayout {

    private TextView tv_info_title;
    private TextView tv_info;

    public StockInfoItem(Context context) {
        super(context);

    }

    public StockInfoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_info_item, null);
        this.addView(view);
        initView();
        initAttrs(context, attrs);
    }

    public StockInfoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.stock_info_item, null);
        this.addView(view);
        initView();
        initAttrs(context, attrs);
    }

    private void initView(){
        tv_info_title = findViewById(R.id.tv_info_title);
        tv_info = findViewById(R.id.tv_stock_info);
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockInfoItem);
        tv_info_title.setText(typedArray.getString(R.styleable.StockInfoItem_itemTitle));
        tv_info_title.setTextColor(typedArray.getColor(R.styleable.StockInfoItem_itemTitleColor, 0));
        tv_info_title.setTextSize(typedArray.getDimension(R.styleable.StockInfoItem_itemTitleSize, 20));
        tv_info.setText(typedArray.getString(R.styleable.StockInfoItem_itemInfo));
        tv_info.setTextColor(typedArray.getColor(R.styleable.StockInfoItem_itemInfoColor, 0));
        tv_info.setTextSize(typedArray.getDimension(R.styleable.StockInfoItem_itemInfoSize, 20));
    }

    public void setTitleText(int id){
        tv_info_title.setText(id);
    }

    public void setTitleText(String s) {
        tv_info_title.setText(s);
    }

    public void setTitleColor(int color){
        tv_info_title.setTextColor(color);
    }

    public void setInfoText(int id) {
        tv_info.setText(id);
    }

    public void setInfoText(String s){
        tv_info.setText(s);
    }

    public void setInfoColor(int color){
        tv_info.setTextColor(color);
    }

}
