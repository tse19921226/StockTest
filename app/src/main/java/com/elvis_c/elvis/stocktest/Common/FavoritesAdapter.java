package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private AdapterItemClick itemClick;
    private AdapterItemLongClickCallback mLongClickCallback;

    public FavoritesAdapter(Context context){
        mContext = context;
        syncHandler = new Handler();
    }

    public void updateList(ArrayList<Company> Companies){
        companies = Companies;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.favorite_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        try {
            viewHolder.stockItemView.initView();
            viewHolder.stockItemView.setStockData(companies.get(i));

            viewHolder.stockItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(companies.get(viewHolder.getAdapterPosition()).getC());
                }
            });

            viewHolder.stockItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickCallback.onLongTouch(companies.get(viewHolder.getAdapterPosition()).getC());
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private StockItemView stockItemView;
        public ViewHolder( View itemView) {
            super(itemView);
            stockItemView = itemView.findViewById(R.id.si_favorite);
        }
    }

    public void registerCallback(AdapterItemClick adapterItemClick){
        itemClick = adapterItemClick;
    }

    public void unregisterCallback(){
        itemClick = null;
    }

    public interface AdapterItemClick{
        void onItemClick(String StockID);
    }

    public void registerLongClickCallback(AdapterItemLongClickCallback longClickCallback){
        mLongClickCallback = longClickCallback;
    }

    public void unregisterLongClickCallback(){

    }

    public interface  AdapterItemLongClickCallback{
        void onLongTouch(String StockID);
    }

}
