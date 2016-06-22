package com.example.gwon.javachip.Javafile;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gwon on 2016-06-18.
 */
public class MakeJsonObject {

    HashMap<String,String> data;
    HttpURLConnection connection = null;
    OutputStream os;
    String json;
    BufferedWriter bufferedWriter;
    BufferedReader reader = null;
    StringBuffer buffer = null;
    InputStream stream;
    String urlPath;

    public MakeJsonObject(String urlPath){
        this.urlPath = urlPath;
    }
    public MakeJsonObject(String urlPath,HashMap<String,String> data){
        this.urlPath = urlPath;
        this.data = data;

    }

    public JSONObject makemakehttpUrlConnection_no_params(){
        URL url = null;

        try {
            url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject(finalJson);

            return  parentObject;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();

    }
    public JSONObject makehttpUrlConnection() {

        try {
            URL url = new URL(urlPath);

            connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            os = connection.getOutputStream();

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
            bufferedWriter.write(getQueryData(data));
            bufferedWriter.flush();
            bufferedWriter.close();
            connection.connect();
            stream = connection.getInputStream();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
            buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {


                buffer.append(line + "\n");
                Log.i("data", line + "        ");
            }

            json = buffer.toString();

            stream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject parentObject = null;
        try {
            parentObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parentObject;
    }

    public String getQueryData(HashMap<String, String> data) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");


            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

        }

        return result.toString();
    }

}
