package net.sprintasia.bayarind.lib;

import android.os.AsyncTask;

import net.sprintasia.bayarind.Constant;
import net.sprintasia.bayarind.listener.OnApiListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ops1 on 12/01/2018.
 */

public class ApiCall extends AsyncTask<String, Void, Void> {

    private URL url;
    private HttpURLConnection http;
    private HashMap<String, String> data;
    private OnApiListener callback;

    public ApiCall(String url, String method){
        try {
            this.url = new URL(Constant.API_ENDPOINT + url);
            http = (HttpURLConnection) this.url.openConnection();
            http.setDoInput(true);
            http.setDoOutput(false);
            http.setRequestMethod(method);
            data = new HashMap<>();
            if(method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")){
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestProperty("X-Bayarind-Secret", Constant.MOBILE_API_KEY);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHeader(String key, String value){
        if(http == null) return;
        http.setRequestProperty(key, value);
    }

    public void setHeaders(Map<String, String> headers){
        if(http == null) return;
        for (Map.Entry<String, String> header : headers.entrySet()){
            http.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    public void setParam(String key, String value){
        if(http == null) return;
        this.data.put(key, value);
    }

    public void setParams(HashMap<String, String> data) {
        if(http == null) return;
        if (data != null) {
            this.data.putAll(data);
        }
    }

    public void send(OnApiListener callback){
        execute(new String());
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... params) {
        OutputStreamWriter writer = null;
        Boolean status = null;
        String message = "Opps, failed when connecting to payment server.";
        JSONObject res = null;
        try {
            if (this.data != null && this.data.size() > 0) {
                writer = new OutputStreamWriter(http.getOutputStream());
                writer.write(new JSONObject(data).toString());
                writer.flush();
            }
            InputStream inputStream = new BufferedInputStream(http.getInputStream());
            String response = convertStreamToString(inputStream);
            res = new JSONObject(response);
            status = res.getBoolean("status");
            message = res.getString("message");

        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailed();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailed();
            return null;
        }

        Object data = null;
        if(status){
            try {
                data = res.get("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(data instanceof JSONArray){
            callback.onSuccess(status, message, (JSONArray) data);
        }else if(data instanceof  JSONObject){
            callback.onSuccess(status, message, (JSONObject) data);
        }else {
            callback.onSuccess(status, message, (String) data);
        }

        return null;
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
