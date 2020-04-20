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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BottleVerified extends AppCompatActivity {
    private String userId;
    private String automatId;
    private double balance;
    private String barcode;
    private String getUrl = "http://recyclingprojectsirius.herokuapp.com/connections/getResult/";
    private String bottleInfoUrl = "http://recyclingprojectsirius.herokuapp.com/rest/bottles/";
    private Handler customHandler;
    private StringRequest closeOrNew;
    private JsonObjectRequest bottlePrice;
    private TextView balanceText;
    private double addToBalance;
    private int counter=0;
    private boolean stop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottle_verified);
        Toolbar toolbar = findViewById(R.id.bottleInfoToolbar);
        setSupportActionBar(toolbar);
        balanceText = (TextView) findViewById(R.id.balance);
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

        getUrl = getUrl + userId + "/" + automatId + "/" + barcode + "/1";
        bottleInfoUrl = bottleInfoUrl + barcode;


        bottlePrice = new JsonObjectRequest(Request.Method.GET, bottleInfoUrl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            addToBalance = response.getDouble("price");
                            balanceText.setText(Double.toString(addToBalance));
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

        Volley.newRequestQueue(BottleVerified.this).add(bottlePrice);


        System.out.println(getUrl);

        customHandler.postDelayed(updateTimerThread, 8000);


    }
    private Runnable updateTimerThread = new Runnable()
    {

        public void run() {
            try {
                if (counter < 30) {
                    // prepare the Request
                    closeOrNew = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("bottle vrified respse", response);
                                System.out.println(response);
                                if (response.equals("2")) { // DAHA CEVAP GELMEDİ
                                    stop = false;
                                    counter++;
                                    System.out.println("Bottle Verified sayfasinda cevap false geldi.  counter: " + counter);
                                    Thread.sleep(2000);
                                    run();
                                } else if (response.equals("1")) { // YENİ İŞLEM YAPILACAK
                                    stop = true;
                                    Intent intent = new Intent(BottleVerified.this, ScanBarcode.class);
                                    Bundle bu = new Bundle();
                                    bu.putString("userID", userId); //Your id
                                    bu.putString("automatID", automatId);
                                    bu.putDouble("balance", balance + addToBalance); // balance a ekle
                                    // balance da eklencek
                                    intent.putExtras(bu);
                                    finish();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (response.equals("0")) { // BALANCE EKRANINA GEC
                                    stop = true;
                                    Intent intent = new Intent(BottleVerified.this, UserBalance.class);
                                    Bundle bu = new Bundle();
                                    bu.putString("userID", userId); //Your id
                                    bu.putString("automatID", automatId);
                                    bu.putDouble("balance", balance + addToBalance);
                                    // balance da eklencek
                                    intent.putExtras(bu);
                                    finish();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    Volley.newRequestQueue(BottleVerified.this).add(closeOrNew);


                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.timeout_profile,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BottleVerified.this, UserProfile.class);
                    Bundle bu = new Bundle();
                    bu.putString("mail", userId); //Your id
                    intent.putExtras(bu);
                    finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bos, menu);
        return true;
    }
}
