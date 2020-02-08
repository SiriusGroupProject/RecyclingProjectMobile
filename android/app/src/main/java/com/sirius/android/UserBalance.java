package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class UserBalance extends AppCompatActivity {

    private double balance;
    private String finalBalance;
    private TextView textView;
    private String url = "http://www.mocky.io/v2/5e3e8878330000e91f8b0a31";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_balance);
        sendAndRequestResponse();
        Toolbar toolbar = findViewById(R.id.userBalanceToolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent profile = new Intent(this, UserProfile.class);
            startActivity(profile);
            return true;
        }
        // logout

        return super.onOptionsItemSelected(item);
    }

    private void sendAndRequestResponse() {

        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Total price execution

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(response);
                    balance = jsonObject.getDouble("balance");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TextView textView = (TextView) findViewById(R.id.balance);
                finalBalance = "" + balance + "  ₺";
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