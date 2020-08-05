package com.elvis_c.elvis.stocktest.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.elvis_c.elvis.stocktest.Model.StockInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SyncData extends AsyncTask<String, String, String> {
    private String TAG = getClass().getSimpleName();
    private Context mCtx;
    private SyncDataCallback mSyncDataCallback;

    public void setmCtx(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            Log.d(TAG, "doInBackground, strings" + strings[0]);
            URL url = new URL(strings[0]);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            Log.d(TAG, "conn.getResponseCode = " + conn.getResponseCode());
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                result = stringBuilder.toString();
            }else  {
                result = "error";
            }
            Log.d(TAG, "result = " + result);
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute, s = " + s);
        try {
//            JSONObject object = new JSONObject(s);
//            StockInfo stockInfo = new StockInfo();
//            stockInfo.setReferer(object.getString("referer"));

            StockInfo stockInfo = new StockInfo().loadString(s);
            Log.d(TAG, "onPostExecute, stockInfo.getRtcode() = " + stockInfo.getRtcode());
            Log.d(TAG, "onPostExecute, getSysDate = " + stockInfo.getQueryTime().getSysDate());
            Log.d(TAG, "onPostExecute, getC = " + stockInfo.getMsgArray().get(0).getC());
            DataManagement.getInstance(mCtx).setStockInfo(stockInfo);
            mSyncDataCallback.SyncFinish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerSyncDataCallback(SyncDataCallback syncDataCallback) {
        mSyncDataCallback = syncDataCallback;
    }

    public void unregisterSyncDataCallback(){
        mSyncDataCallback = null;
    }

    public interface SyncDataCallback{
        void SyncFinish();
    }
}
