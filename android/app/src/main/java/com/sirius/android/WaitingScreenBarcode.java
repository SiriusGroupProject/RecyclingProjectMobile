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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WaitingScreenBarcode extends AppCompatActivity {
    private ProgressBar pgsBar;
    private String userId;
    private String automatId;
    private double balance;
    private String barcode;
    private String postUrl = "http://recyclingprojectsirius.herokuapp.com/connections/forwardScannedBarcode/";
    private String getUrl = "http://recyclingprojectsirius.herokuapp.com/connections/getBottleVerification/";
    private Handler customHandler;
    private RequestQueue queue;
    private StringRequest postBarcode;
    private StringRequest isVerified;
    private int counter=0;
    private boolean stop = false;
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
        postUrl = postUrl + userId + "/" + automatId + "/" + barcode;

        System.out.println(postUrl);
        System.out.println(getUrl);


        postBarcode = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
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
        Volley.newRequestQueue(WaitingScreenBarcode.this).add(postBarcode);

        customHandler.postDelayed(updateTimerThread, 10000);


    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            try {

                if (counter < 30) {
                    // prepare the Request
                    isVerified = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("waitingbarcoderesponse", response);
                                System.out.println(response);
                                if (response.equals("2")) {
                                    stop = false;
                                    counter++;
                                    System.out.println("Waiting Screen Barcode sayfasinda cevap false geldi.  counter: " + counter);
                                    Thread.sleep(2000);
                                    run();

                                } else if (response.equals("1")) { // VERIFIED
                                    stop = true;
                                    Intent intent = new Intent(WaitingScreenBarcode.this, BottleVerified.class);
                                    Bundle bu = new Bundle();
                                    bu.putString("userID", userId); //Your id
                                    bu.putString("automatID", automatId);
                                    bu.putDouble("balance", balance);
                                    bu.putString("barcode", barcode);
                                    // balance da eklencek
                                    intent.putExtras(bu);
                                    finish();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (response.equals("0")) { // NOT VERIFIED
                                    stop = true;
                                    Intent intent = new Intent(WaitingScreenBarcode.this, BottleNotVerified.class);
                                    Bundle bu = new Bundle();
                                    bu.putString("userID", userId); //Your id
                                    bu.putString("automatID", automatId);
                                    bu.putDouble("balance", balance);
                                    bu.putString("barcode", barcode);
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
                    Volley.newRequestQueue(WaitingScreenBarcode.this).add(isVerified);


                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.timeout_profile,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WaitingScreenBarcode.this, UserProfile.class);
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
