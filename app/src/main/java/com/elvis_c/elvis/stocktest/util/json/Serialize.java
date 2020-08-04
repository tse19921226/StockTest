package com.elvis_c.elvis.stocktest.util.json;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialize {
    public Serialize(){
    }

//    public static void SaveXmlFile(String FileName, Object obj) throws Exception {
//        String jsonString = ObjectToJson(obj);
//        String xmlString = JsonToXml(jsonString);
//        SaveText(FileName, xmlString);
//    }

//    public static void SaveXmlFile(String FileName, String xmlString) throws Exception {
//        SaveText(FileName, xmlString);
//    }

//    public static <T> T LoadXmlFileToObject(String FileName, Class<T> classOfT) throws Exception {
//        String xmlString = LoadText(FileName);
//        String jsonString = XmlToJson(xmlString);
//        return JsonToObject(jsonString, classOfT);
//    }

//    public static String LoadXmlFileToString(String FileName) throws Exception {
//        return LoadText(FileName);
//    }

    public static void SaveJsonFile(String FileName, Object obj) throws Exception {
        String json = ObjectToJson(obj);
        SaveText(FileName, json);
    }

    public static void SaveJsonFile(String FileName, String jsonString) throws Exception {
        SaveText(FileName, jsonString);
    }

    public static <T> T LoadJsonFileToObject(String FileName, Class<T> classOfT) throws Exception {
        String jsonString = LoadText(FileName);
        return JsonToObject(jsonString, classOfT);
    }

    public static String LoadJsonFileToString(String FileName) throws Exception {
        return LoadText(FileName);
    }

    public static <T> T JsonToObject(String json, Class<T> classOfT) {
        return (new Gson()).fromJson(json, classOfT);
    }

    public static String ObjectToJson(Object obj) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        return jsonString;
    }

    public static String ObjectToJsonPretty(Object obj) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String jsonString = gson.toJson(obj);
        return jsonString;
    }

//    public static String XmlToJson(String xmlString) {
//        JSONObject jsonObj = null;
//
//        try {
//            jsonObj = XML.toJSONObject(xmlString);
//        } catch (JSONException var3) {
//            Log.e("JSON exception", var3.getMessage());
//            var3.printStackTrace();
//        }
//
//        Log.d("XML", xmlString);
//        Log.d("JSON", jsonObj.toString());
//        return jsonObj.toString();
//    }
//
//    public static String JsonToXml(String JsonString) {
//        JSONObject json;
//        try {
//            json = new JSONObject(JsonString);
//        } catch (JSONException var5) {
//            var5.printStackTrace();
//            return "";
//        }
//
//        String xml = "";
//
//        try {
//            xml = XML.toString(json);
//        } catch (JSONException var4) {
//            var4.printStackTrace();
//        }
//
//        return xml;
//    }

    private static void SaveText(String FileName, String text) throws Exception {
        File file = new File(FileName);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream stream = null;
        stream = new FileOutputStream(file);
        stream.write(text.getBytes());
        stream.close();
    }

    private static String LoadText(String FileName) throws Exception {
        File file = new File(FileName);
        int length = (int)file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;
        in = new FileInputStream(file);
        in.read(bytes);
        in.close();
        return new String(bytes);
    }

    public static <T extends Serializable> void saveSerializable(Context context, T objectToSave, String fileName) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, 0);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectToSave);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static <T extends Serializable> T readSerializable(Context context, String fileName) {
        Serializable objectToReturn = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (Serializable)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return (T) objectToReturn;
        } catch (ClassNotFoundException var5) {
            var5.printStackTrace();
            return null;
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void removeSerializable(Context context, String filename) {
        context.deleteFile(filename);
    }

}
