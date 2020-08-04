package com.elvis_c.elvis.stocktest.Model;

import com.elvis_c.elvis.stocktest.util.json.Serialize;

import java.io.Serializable;

public class Company implements Cloneable, Serializable {
//    ['c','n','z','tv','v','o','h','l','y'] ;
//    ['股票代號','公司簡稱','當盤成交價','當盤成交量','累積成交量','開盤價','最高價','最低價','昨收價'];
    String c = "";//股票代號
    String n = "";//公司簡稱
    String z = "";//當盤成交價
    String tv = "";//當盤成交量
    String v = "";//累積成交量
    String o = "";//開盤價
    String h = "";//最高價
    String l = "";//最低價
    String y = "";//昨收價

    public void setC(String c) {
        this.c = c;
    }

    public void setN(String n) {
        this.n = n;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public void setV(String v) {
        this.v = v;
    }

    public void setO(String o) {
        this.o = o;
    }

    public void setH(String h) {
        this.h = h;
    }

    public void setL(String l) {
        this.l = l;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getC() {
        return c;
    }

    public String getN() {
        return n;
    }

    public String getZ() {
        return z;
    }

    public String getTv() {
        return tv;
    }

    public String getV() {
        return v;
    }

    public String getO() {
        return o;
    }

    public String getH() {
        return h;
    }

    public String getL() {
        return l;
    }

    public String getY() {
        return y;
    }

    @Override
    public String toString() {
        return Serialize.ObjectToJson(this);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static Company loadString(String json) {
        return Serialize.JsonToObject(json, Company.class);
    }
}
