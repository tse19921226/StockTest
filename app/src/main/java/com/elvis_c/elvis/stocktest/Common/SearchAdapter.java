package com.elvis_c.elvis.stocktest.Common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.Model.AllCompanyCode;
import com.elvis_c.elvis.stocktest.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<AllCompanyCode> codes;
    private ViewHolder viewHolder;
    private adapterCallback mCallback;

    public SearchAdapter (Context context){
        mContext = context;
    }

    public void upDateList(ArrayList<AllCompanyCode> list) {
        codes = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (position != 0) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 5);
            viewHolder.itemView.setLayoutParams(layoutParams);
        }
        viewHolder.tv_stockID.setText(codes.get(position).getCompanyCode());
        viewHolder.tv_stockName.setText(codes.get(position).getCompanyName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick(codes.get(viewHolder.getAdapterPosition()).getCompanyCode());
            }
        });
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_stockID, tv_stockName;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_stockID = itemView.findViewById(R.id.tv_stockID);
            tv_stockName = itemView.findViewById(R.id.tv_stockName);
        }
    }

    public void registerCallback(adapterCallback callback){
        mCallback = callback;
    }

    public void unregisterCallback(){
        mCallback = null;
    }

    public interface adapterCallback{
        void onClick(String stockID);
    }

}
