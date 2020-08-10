package com.elvis_c.elvis.stocktest.Model;

import com.elvis_c.elvis.stocktest.util.json.Serialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockInfo implements Cloneable, Serializable {
    QueryTime queryTime;
    String referer = "";
    String rtmessage = "";
    int userDelay = 5000;
    List<Company> msgArray = new ArrayList<>();
    String rtcode = "1001";

    public void setQueryTime(QueryTime queryTime) {
        this.queryTime = queryTime;
    }

    public QueryTime getQueryTime() {
        return queryTime;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getReferer() {
        return referer;
    }

    public void setRtmessage(String rtmessage) {
        this.rtmessage = rtmessage;
    }

    public String getRtmessage() {
        return rtmessage;
    }

    public void setUserDelay(int userDelay) {
        this.userDelay = userDelay;
    }

    public int getUserDelay() {
        return userDelay;
    }

    public void setMsgArray(List<Company> msgArray) {
        this.msgArray = msgArray;
    }

    public List<Company> getMsgArray() {
        return msgArray;
    }

    public void setRtcode(String rtcode) {
        this.rtcode = rtcode;
    }

    public String getRtcode() {
        return rtcode;
    }

    @Override
    public String toString() {
        return Serialize.ObjectToJson(this);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static StockInfo loadString(String json) {
        return Serialize.JsonToObject(json, StockInfo.class);
    }

    public static StockInfo LoadConfig(String FileName) throws Exception {
        return Serialize.LoadJsonFileToObject(FileName, StockInfo.class);
    }
}
