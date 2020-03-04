package com.sirius.android;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScanBarcode extends AppCompatActivity {

    public static TextView resultTextView;
    Button  scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_barcode);

        resultTextView = (TextView)findViewById(R.id.result_barcode);
        scanButton = (Button)findViewById(R.id.btnScanBarcode);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScanBarcodeActivity.class));
            }
        });
    }

}
