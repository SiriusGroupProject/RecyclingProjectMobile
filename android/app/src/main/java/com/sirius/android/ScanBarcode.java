package com.sirius.android;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ScanBarcode extends AppCompatActivity {

    public static TextView resultTextView;
    Button  scanButton;
    private String userId;
    private String automatId;
    private Double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_barcode);
        Toolbar toolbar = findViewById(R.id.scanBarcodeToolbar);
        setSupportActionBar(toolbar);

        resultTextView = (TextView)findViewById(R.id.result_barcode);
        scanButton = (Button)findViewById(R.id.btnScanBarcode);

        Bundle b = getIntent().getExtras();
        userId = ""; // or other values
        automatId = "";
        balance = 0.0;
        if(b != null) {
            userId = b.getString("userID");
            automatId = b.getString("automatID");
            balance = b.getDouble("balance");

        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanBarcode.this, ScanBarcodeActivity.class);
                Bundle bu = new Bundle();
                bu.putString("userID",userId); //Your id
                bu.putString("automatID",automatId);
                bu.putDouble("balance",balance);
                // balance da eklencek
                intent.putExtras(bu);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
