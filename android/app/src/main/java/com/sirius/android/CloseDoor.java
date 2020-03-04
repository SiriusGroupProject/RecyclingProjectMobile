package com.sirius.android;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CloseDoor extends AppCompatActivity{

    Button  scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_automat);

        scanButton = (Button)findViewById(R.id.btnScanQR);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BİLGİ VER KAPILARIN KAPANMASI İÇİN
                startActivity(new Intent(getApplicationContext(),WaitingScreen.class));
            }
        });
    }


}
