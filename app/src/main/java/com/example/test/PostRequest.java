package com.example.test;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class PostRequest extends AsyncTask<String, Void, String> {

    public interface PostRequestResult{
        void processResult(JSONObject resultJSON);
    }

    public PostRequestResult postRequestResult = null;

    public PostRequest(PostRequestResult prr){
        this.postRequestResult = prr;
    }


    public String convertInputToString(InputStream stream)
            throws IOException, UnsupportedEncodingException {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);

        String result = IOUtils.toString(stream, StandardCharsets.UTF_8);
        return result;
    }


    @Override
    protected String doInBackground(String... queryStrings) {
        //https://api.weixin.qq.com/tcb/databasequery?access_token=ACCESS_TOKEN

        String queryString = queryStrings[0];
        String requestBaseURL = queryStrings[1];

        final String access_token = "access_token";

        while(Global.accessToken == "");

        Log.d("globalAccessToken: ", Global.accessToken);

        Uri uri = Uri.parse(requestBaseURL).buildUpon()
                .appendQueryParameter(access_token, Global.accessToken)
                .build();

        String uriString = uri.toString();
        HttpsURLConnection conn;
        conn = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(uriString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject postJson = new JSONObject();
//            postJson.put("access_token", Global.accessToken);
            postJson.put("env", Global.env);
            postJson.put("query", queryString);

            Log.d("postJson: ", postJson.toString());

            String postJsonString = postJson.toString();

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postJsonString.getBytes());
            outputStream.flush();

            conn.connect();
            int response = conn.getResponseCode();
            Log.d("responseCode: ", Integer.toString(response));
            inputStream = conn.getInputStream();

            String contentString = convertInputToString(inputStream);

            return contentString;

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        } finally {
            conn.disconnect();
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void onPostExecute(String result){
        try {
            JSONObject response = new JSONObject(result);
            Log.d("result: ", result);
            postRequestResult.processResult(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

