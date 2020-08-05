package com.elvis_c.elvis.stocktest.controller;

import android.content.Context;
import android.util.Log;

import com.elvis_c.elvis.stocktest.Model.AllCompanyCode;
import com.elvis_c.elvis.stocktest.Model.StockInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DataManagement {
    public String TAG = getClass().getSimpleName();
    private static DataManagement mInstance;
    private Context mContext = null;
    public static String urlData = "https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=";
    public static String test = "tse_1101.tw";
    public static String twIndex = "tse_t01.tw";//發行量加權股價指數
    public static String ALL_COMPANY_CSV = "http://moneydj.emega.com.tw/js/StockTable.xls";
    public static String ALL_COMPANY_CSV2 = "https://quality.data.gov.tw/dq_download_csv.php?nid=18419&md5_url=9791ec942cbcb925635aa5612ae95588";
    public static String CsvFloder = "/csvData";
    public static String CompanyCsv = "/CompanyCsv.csv";
    public static String DownloadDate = "/downloadDate.txt";
    public static String stockindextUrl = "https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=%s&type=IND";

    private StockInfo stockInfo;
    private ArrayList<AllCompanyCode> allCompanyCodes = new ArrayList<>();
    //https://www.twse.com.tw/exchangeReport/MI_INDEX?response=csv&date=20200401&type=MS
    //http://isin.twse.com.tw/isin/C_public.jsp?strMode=2
    //http://isin.twse.com.tw/isin/C_public.jsp?strMode=5

    //type ALL, 1 ~ 20
    //https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20200401&type=01&_=1586060606932
    //https://www.twse.com.tw/exchangeReport/MI_INDEX?response=json&date=20200401&type=ALL&_=1586060606934

    private CsvDownloadCallBack mCsvDownloadCallBack;

    public static DataManagement getInstance(Context context){
        if (mInstance == null) {
            mInstance = new DataManagement();
            mInstance.mContext = context;
        }
        return mInstance;
    }

    public void setStockInfo(StockInfo stockInfo){
        this.stockInfo = stockInfo;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public ArrayList<AllCompanyCode> getAllCompanyCodes(){
        return allCompanyCodes;
    }

    public void readCsvFile(){
        try {
            File file = new File(mContext.getApplicationContext().getFilesDir().getPath() + CsvFloder + CompanyCsv);
            if (file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

                String line = "";
                bufferedReader.readLine();
                Log.d(TAG, "readCsvFile, line.length = " + line.length());
                while ((line = bufferedReader.readLine()) != null) {

                    String[] reLines = line.split(",");

                    AllCompanyCode allCompanyCode = new AllCompanyCode();
                    allCompanyCode.setDate(reLines[0]);
                    allCompanyCode.setCompanyCode(reLines[1]);
                    allCompanyCode.setCompanyName(reLines[2]);
                    allCompanyCodes.add(allCompanyCode);
                }
                Log.d(TAG, "readCsvFile, allCompanyCodes.size() = " + allCompanyCodes.size());
                Log.d(TAG, "readCsvFile, load all company code finish");
            } else {
                Log.e(TAG, "file not exists");
            }
//            InputStream inputStream =
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadCsv(){
        BufferedInputStream bis =null;
        BufferedOutputStream bos=null;
        String csvPath = mInstance.mContext.getApplicationContext().getFilesDir().getPath() + CsvFloder;
        File csvFolder = new File(csvPath);
        if (!csvFolder.exists()) {
           csvFolder.mkdirs();
        }

        if (mInstance.checkCsvDateToDownload()) {
            if (csvFolder.isDirectory()) {
                for (File file : csvFolder.listFiles()) {
                    file.delete();
                }
            }
            try {
                URL url = new URL(ALL_COMPANY_CSV2);
                HttpsURLConnection connection =  (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Log.d(mInstance.TAG, "connection.getResponseCode() = " + connection.getResponseCode());
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    InputStream is= connection.getInputStream();
                    bis = new BufferedInputStream(is);
                    FileOutputStream fos = new FileOutputStream(csvFolder + CompanyCsv);
                    bos= new BufferedOutputStream(fos);
                    int b = 0;
                    byte[] byArr = new byte[1024];
                    while((b= bis.read(byArr))!=-1){
                        bos.write(byArr, 0, b);
                    }
                    Log.d(mInstance.TAG, "Download Finish");

                }
                File downloadDate = new File(mInstance.mContext.getApplicationContext().getFilesDir().getPath() + CsvFloder + DownloadDate);
                if (!downloadDate.exists()) {
                    Log.d(mInstance.TAG, "create txt file");
                    downloadDate.createNewFile();
                }
                mInstance.writeDate2Txt(downloadDate);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    if(bis !=null){
                        bis.close();
                    }
                    if(bos !=null){
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mInstance.mCsvDownloadCallBack.downloadFinish();

    }

    private void writeDate2Txt(File file){
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            String s = String.valueOf(System.currentTimeMillis());
            outputStream.write(s.getBytes());
            outputStream.close();
            Log.d(TAG, "writeDate2Txt, write finish");
        } catch (Exception e) {
            Log.d(TAG, "writeDate2Txt, write fail");
            e.printStackTrace();
        }
    }

    private String readDateFromTxt(File file){
        String value = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readString = "";
            Log.d(TAG, "readDateFromTxt, bufferedReader.readLine() = " + bufferedReader.readLine());
            while ((readString = bufferedReader.readLine()) != null) {
                Log.d(TAG, "readDateFromTxt, readString = " + readString);
                value = readString;
                Log.d(TAG, "readDateFromTxt, value = " + value);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
//            return readString;
        }
        Log.d(TAG, "readDateFromTxt, value = " + value);
        return value;
    }

    public boolean checkCsvDateToDownload(){
        File file = new File(mContext.getApplicationContext().getFilesDir().getPath() + CsvFloder + DownloadDate);
        if (file.exists()) {
            long lastTime = Long.parseLong(readDateFromTxt(file));
            Log.d(TAG, "checkCsvDateToDownload, lastTime = " + lastTime);
            long nowTime = System.currentTimeMillis();
            if ((nowTime - lastTime) / (24 * 60 * 60 * 1000) >= 30) {
                Log.d(TAG, "checkCsvDateToDownload, over 30 days");
                return true;
            } else {
                Log.d(TAG, "checkCsvDateToDownload, not over 30 days");
                return false;
            }
        } else {
            Log.d(TAG, "checkCsvDateToDownload, file not exists");
            return true;
        }
    }

    public String currentTime2DateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String DateStr = "";
        try {
            DateStr = dateFormat.format(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DateStr;
    }

    public void registerCsvDownloadCallback(CsvDownloadCallBack csvDownloadCallBack){
        mCsvDownloadCallBack = csvDownloadCallBack;
    }

    public void unregisterCsvDownloadCallback(){
        mCsvDownloadCallBack = null;
    }

    public interface CsvDownloadCallBack{
        void downloadFinish();
    }

}
