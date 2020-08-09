package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvis_c.elvis.stocktest.Model.Company;
import com.elvis_c.elvis.stocktest.R;
import com.elvis_c.elvis.stocktest.controller.DataManagement;
import com.elvis_c.elvis.stocktest.controller.SyncData;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<Company> companies = new ArrayList<>();
    private SyncData syncData;
    private String url;
    private Handler syncHandler;


    public FavoritesAdapter(Context context){
        mContext = context;
        syncHandler = new Handler();
    }

    public void updateList(ArrayList<Company> Companies){
        companies = Companies;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.stock_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        viewHolder.stockItemView.initView();
        viewHolder.stockItemView.setStockData(companies.get(i));
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private StockItemView stockItemView;
        public ViewHolder( View itemView) {
            super(itemView);
            stockItemView.findViewById(R.id.si_favorite);
        }
    }



}
