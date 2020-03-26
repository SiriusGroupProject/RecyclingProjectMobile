package com.sirius.android;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class BottleNotVerified extends AppCompatActivity {
    private String userId;
    private String automatId;
    private double balance;
    private String barcode;
    private String getUrl = "http://192.168.1.6:8080/connections/getResult/";
    private Handler customHandler;
    private StringRequest closeOrNewOrRepeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_not_verified);
        Toolbar toolbar = findViewById(R.id.bottleInfoToolbar);
        setSupportActionBar(toolbar);
        customHandler = new Handler();


        Bundle b = getIntent().getExtras();
        userId = "";
        automatId = "";
        balance = 0.0;
        barcode = "";
        if(b != null) {
            userId = b.getString("userID");
            automatId = b.getString("automatID");
            balance = b.getDouble("balance");
            barcode = b.getString("barcode");
            System.out.println(userId);
            System.out.println(automatId);
            System.out.println(barcode);
            System.out.println(balance);
        }

        getUrl = getUrl + userId + "/" + automatId + "/" + barcode;

        System.out.println(getUrl);

        customHandler.postDelayed(updateTimerThread, 1000);


    }
    private Runnable updateTimerThread = new Runnable()
    {

        boolean stop = false;
        public void run()
        {

            // prepare the Request
            closeOrNewOrRepeat = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d(response,response);
                        System.out.println(response);
                        if(response.equals("3")){ // AYNI ŞİŞEYLE DEVAM
                            stop = true;
                            Intent intent = new Intent(BottleNotVerified.this, WaitingScreenBarcode.class);
                            Bundle bu = new Bundle();
                            bu.putString("userID",userId); //Your id
                            bu.putString("automatID",automatId);
                            bu.putDouble("balance",balance);
                            bu.putString("barcode",barcode);
                            // balance da eklencek
                            intent.putExtras(bu);
                            startActivity(intent);
                        }
                        else if(response.equals("2")){ // DAHA SEÇİM YAPILMADI
                            stop = false;
                        }
                        else if(response.equals("1")){ // YENİ İŞLEM YAPILACAK
                            stop = true;
                            Intent intent = new Intent(BottleNotVerified.this, ScanBarcode.class);
                            Bundle bu = new Bundle();
                            bu.putString("userID",userId); //Your id
                            bu.putString("automatID",automatId);
                            bu.putDouble("balance",balance);
                            // balance da eklencek
                            intent.putExtras(bu);
                            startActivity(intent);
                        }
                        else if(response.equals("0")){ // BALANCE EKRARNINA GEC
                            stop = true;
                            Intent intent = new Intent(BottleNotVerified.this, UserBalance.class);
                            Bundle bu = new Bundle();
                            bu.putString("userID",userId); //Your id
                            bu.putString("automatID",automatId);
                            bu.putDouble("balance",balance);
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
            Volley.newRequestQueue(BottleNotVerified.this).add(closeOrNewOrRepeat);
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
