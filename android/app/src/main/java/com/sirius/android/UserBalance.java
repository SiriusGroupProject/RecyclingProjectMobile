package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public class UserBalance extends AppCompatActivity {

    private String finalBalance;
    private Button donationButton;
    private Button totalBalanceButton;
    private TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_balance);
        Toolbar toolbar = findViewById(R.id.userBalanceToolbar);
        setSupportActionBar(toolbar);
        bindViews();
        sendAndRequestResponse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void bindViews() {

        donationButton = (Button) findViewById(R.id.donation);
        totalBalanceButton = (Button) findViewById(R.id.balanceAdd);
        // intentle total balance verisi alınacak
        balanceText = (TextView) findViewById(R.id.balance);
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

        donationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // eklenen tasarıma göre yazılacak
            }
        });

        totalBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // total balance a eklenecek
            }
        });
    }

}