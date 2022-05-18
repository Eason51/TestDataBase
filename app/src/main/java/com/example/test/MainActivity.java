package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView textLabel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLabel = findViewById(R.id.textContent);


        /*retrieve facility page content*/
//        String requestBaseURL = "https://api.weixin.qq.com/tcb/databasequery?";
//        String queryString = "db.collection(\"facilities\").where({type: \"hall\"}).limit(100).get()";


        /*retrieve comments for a particular facility*/
//        String requestBaseURL = "https://api.weixin.qq.com/tcb/databasequery?";
//        String facilityId = "b24b5641613340c801903ca6705ccf88";
//        String queryString = String.format("db.collection(\"comments\")" +
//                ".where({facilityId: \"%s\"})" +
//                ".limit(100).get()", facilityId);


        /*register a new user*/
        String requestBaseURL = "https://api.weixin.qq.com/tcb/databaseadd?";
        String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userName = "Eason";
        String itsc = "yhubf";

        String queryString = String.format("" +
                        "db.collection(\"users\").add({" +
                        "    data: [{" +
                        "        userName: \"%s\"," +
                        "        itsc: \"%s\"," +
                        "        androidID: \"%s\"" +
                        "    }]" +
                        "})"
                , userName, itsc, androidID);



        Log.d("queryString: ", queryString);
        Global.retrieveAccessToken();
        queryDatabase(queryString, requestBaseURL);
        
    }



    public void queryDatabase(String queryString, String requestBaseURL){

        new PostRequest(
                new PostRequest.PostRequestResult() {
                    @Override
                    public void processResult(JSONObject resultJSON) {
                        Log.d("request result: ", resultJSON.toString());
//                        try {
////                            JSONArray dataArr = resultJSON.getJSONArray("data");
////                            JSONObject dataJSON = new JSONObject(dataArr.getString(0));
////                            String description = dataJSON.getString("description");
////                            textLabel.setText(description);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
        ).execute(queryString, requestBaseURL);

    }

    public void buttonClicked(View view) {
        textLabel.setText("");
        Log.d("button clicked: ", "clicked");
//        queryDatabase();
    }
}