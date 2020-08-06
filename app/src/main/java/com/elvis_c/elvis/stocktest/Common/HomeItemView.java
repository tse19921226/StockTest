package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.R;

public class HomeItemView extends LinearLayout {

    private String TAG = getClass().getSimpleName();
    private onClickListener mListener;
    private ImageView iv_item;
    private TextView tv_item;

    public HomeItemView(Context context) {
        super(context);
    }

    public HomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view, null);
        this.addView(view);
        initView();
        initAttrs(context, attrs);
    }

    public HomeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view, null);
        this.addView(view);
        initView();
        initAttrs(context, attrs);
    }

    private void initView(){
        iv_item = findViewById(R.id.iv_item);
        tv_item = findViewById(R.id.tv_item);
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeItemView);
        iv_item.setImageResource(typedArray.getResourceId(R.styleable.HomeItemView_itemImage, 0));
        tv_item.setText(typedArray.getString(R.styleable.HomeItemView_itemText));
        tv_item.setTextColor(typedArray.getColor(R.styleable.HomeItemView_itemTextColor, 0));
        tv_item.setTextSize(typedArray.getDimension(R.styleable.HomeItemView_itemTextSize, 20));
    }

    public interface onClickListener{
        void onClick();
    }

    public void setOnClickListener(onClickListener listener){
        mListener = listener;
    }

}
