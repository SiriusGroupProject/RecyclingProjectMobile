package com.sirius.android;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CloseDoor extends AppCompatActivity{

    Button  scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.close_door);
        Toolbar toolbar = findViewById(R.id.closeDoorToolbar);
        setSupportActionBar(toolbar);

        scanButton = (Button)findViewById(R.id.btnScanQR);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BİLGİ VER KAPILARIN KAPANMASI İÇİN
                startActivity(new Intent(getApplicationContext(),WaitingScreen.class));
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
