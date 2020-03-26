package com.sirius.android;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitingScreenQR extends AppCompatActivity {
    private ProgressBar pgsBar;
    private String userId;
    private String automatId;
    private double balance;
    private String postUrl = "http://192.168.1.6:8080/connections/connection/";
    private String getUrl = "http://192.168.1.6:8080/connections/waitingForConnection/";
    private String getUserBalance = "http://192.168.1.6:8080/rest/users/";
    private Handler customHandler;
    private RequestQueue queue;
    private StringRequest postQRCode;
    private StringRequest isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_screen_qr);
        Toolbar toolbar = findViewById(R.id.waitingScreenToolbar);
        setSupportActionBar(toolbar);
        pgsBar = (ProgressBar) findViewById(R.id.progressBar);
        pgsBar.setVisibility(View.VISIBLE);
        customHandler = new Handler();


        Bundle b = getIntent().getExtras();
        userId = "";
        automatId = "";
        if(b != null) {
            userId = b.getString("userID");
            automatId = b.getString("automatID");
            System.out.println(userId);
            System.out.println(automatId);
        }

        getUrl = getUrl + userId + "/" + automatId;
        postUrl = postUrl + userId + "/" + automatId;
        getUserBalance = getUserBalance + userId;

        System.out.println(postUrl);

        JsonObjectRequest getBalance = new JsonObjectRequest(Request.Method.GET, getUserBalance, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            balance = response.getDouble("balance");
                            System.out.println("balance: " + balance );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        Volley.newRequestQueue(WaitingScreenQR.this).add(getBalance);

        postQRCode = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(response,response);
                    System.out.println(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("postError");
            }
        });// Adding request to request queue
        Volley.newRequestQueue(WaitingScreenQR.this).add(postQRCode);

        customHandler.postDelayed(updateTimerThread, 1000);


    }
    private Runnable updateTimerThread = new Runnable()
    {

        boolean stop = false;
        public void run()
        {

            // prepare the Request
            isConnected = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d(response,response);
                        if(response.equals("true")){
                            stop = true;
                            Intent intent = new Intent(WaitingScreenQR.this, ScanBarcode.class);
                            Bundle bu = new Bundle();
                            bu.putString("userID",userId); //Your id
                            bu.putString("automatID",automatId);
                            bu.putDouble("balance",balance); // kullanıcının bilgisi çekilecek ????**1*1*1*
                            // balance da eklencek
                            intent.putExtras(bu);
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error getrequest");
                    stop = true;
                }
            });

            // add it to the RequestQueue
            Volley.newRequestQueue(WaitingScreenQR.this).add(isConnected);
            //write here whaterver you want to repeat
            if(!stop)
                customHandler.postDelayed(this, 2000);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
