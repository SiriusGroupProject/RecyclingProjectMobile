package com.sirius.android;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.JSONObject;

public class UserBalance extends AppCompatActivity {

    private double balance;
    private String finalBalance;
    private TextView textView;
    JSONObject jsonObj;
    private String url = "http://www.mocky.io/v2/5e3e8878330000e91f8b0a31";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_balance);
        sendAndRequestResponse();
    }


    private void sendAndRequestResponse() {

        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(response);
                    balance = jsonObject.getDouble("balance");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TextView textView = (TextView) findViewById(R.id.balance);
                finalBalance = ""+balance;
                textView.setText(finalBalance);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}
