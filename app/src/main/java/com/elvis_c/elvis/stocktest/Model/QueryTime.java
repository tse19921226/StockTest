package com.elvis_c.elvis.stocktest.Model;

import com.elvis_c.elvis.stocktest.util.json.Serialize;

import java.io.Serializable;

public class QueryTime implements Cloneable, Serializable {
    int stockInfoItem = 0;
    String sessionKey = "";
    String sessionStr = "";
    String sysDate = "";
    long sessionFromTime = -1;
    long stockInfo = 0;
    boolean showChart = false;
    long sessionLatestTime = -1;
    String sysTime = "";

    public int getStockInfoItem() {
        return stockInfoItem;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getSessionStr() {
        return sessionStr;
    }

    public String getSysDate() {
        return sysDate;
    }

    public long getSessionFromTime() {
        return sessionFromTime;
    }

    public long getStockInfo() {
        return stockInfo;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public long getSessionLatestTime() {
        return sessionLatestTime;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setStockInfoItem(int stockInfoItem) {
        this.stockInfoItem = stockInfoItem;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setSessionStr(String sessionStr) {
        this.sessionStr = sessionStr;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public void setSessionFromTime(int sessionFromTime) {
        this.sessionFromTime = sessionFromTime;
    }

    public void setStockInfo(long stockInfo) {
        this.stockInfo = stockInfo;
    }

    public void setShowChart(boolean showChart) {
        this.showChart = showChart;
    }

    public void setSessionLatestTime(int sessionLatestTime) {
        this.sessionLatestTime = sessionLatestTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    @Override
    public String toString() {
        return Serialize.ObjectToJson(this);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static QueryTime loadString(String json) {
        return Serialize.JsonToObject(json, QueryTime.class);
    }
}
