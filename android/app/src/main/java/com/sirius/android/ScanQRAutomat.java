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

public class ScanQRAutomat extends AppCompatActivity{

    public static TextView resultTextView;
    Button  scanButton;
    private static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_automat);
        Toolbar toolbar = findViewById(R.id.listAutomatsToolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        userId = ""; // or other values
        if(b != null)
            userId = b.getString("userID");


        resultTextView = (TextView)findViewById(R.id.result_automat_code);
        scanButton = (Button)findViewById(R.id.btnScanQR);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQRAutomat.this, ScanCodeActivity.class);
                Bundle b = new Bundle();
                b.putString("userID",userId); //Your id
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent profile = new Intent(this, UserProfile.class);
            profile.putExtra("mail", userId);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bos, menu);
        return true;
    }


}
