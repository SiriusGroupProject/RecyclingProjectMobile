package com.sirius.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DonationPopUp extends Activity {
    private EditText donation;
    private String array_spinner[];
    private Button dismiss;
    private Button donationButton;
    private String putUrl = "http://198.168.1.2/rest/users/updateBalance/";
    private String userId;
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_pop_up);
        donation = (EditText) findViewById(R.id.coinsDonation);
        dismiss = (Button) findViewById(R.id.dismiss);
        donationButton = (Button) findViewById(R.id.donationButton);

        Bundle b = getIntent().getExtras();
        userId = "";
        balance = 0.0;
        if(b != null) {
            userId = b.getString("userID");
            balance = b.getDouble("balance");
        }

        putUrl = putUrl + userId + "/";

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationPopUp.this, UserProfile.class);
                intent.putExtra("mail", userId);
                startActivity(intent);
            }
        });
        donationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!donation.getText().toString().equals("") && donation.getText().toString() != null){
                    double donationAmount = Double.parseDouble(donation.getText().toString());
                    donationAmount = donationAmount * -1;
                    putUrl = putUrl + donationAmount;

                    StringRequest putRequest = new StringRequest(Request.Method.PUT, putUrl,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Log.d("Error.Response", error.toString());
                                }
                            }
                    )
                            ;

                    Volley.newRequestQueue(DonationPopUp.this).add(putRequest);
                    Intent intent = new Intent(DonationPopUp.this, UserProfile.class);
                    intent.putExtra("mail", userId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Bağışınız yapılmıştır. Teşekkürler.", Toast.LENGTH_LONG).show();
                }

            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


    }

}
