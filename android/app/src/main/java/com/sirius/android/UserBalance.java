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
        else if(id == R.id.action_logout){
            Intent profile = new Intent(this, Login.class);
            startActivity(profile);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendAndRequestResponse() {

        totalBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserBalance.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

}