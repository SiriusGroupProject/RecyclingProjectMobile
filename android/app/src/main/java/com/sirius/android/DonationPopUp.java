package com.sirius.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DonationPopUp extends Activity {
    private EditText donation;
    private String array_spinner[];
    private Button dismiss;
    private Button donationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_pop_up);
        donation = (EditText) findViewById(R.id.coinsDonation);
        dismiss = (Button) findViewById(R.id.dismiss);
        donationButton = (Button) findViewById(R.id.donationButton);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationPopUp.this, UserProfile.class);
                startActivity(intent);
            }
        });
        donationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationPopUp.this, UserProfile.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Bağışınız yapılmıştır. Teşekkürler.", Toast.LENGTH_LONG).show();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


    }

}
