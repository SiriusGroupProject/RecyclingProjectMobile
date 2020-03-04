package com.sirius.android;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class WaitingScreen extends AppCompatActivity {
    private ProgressBar pgsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_screen);
        pgsBar = (ProgressBar) findViewById(R.id.progressBar);
        pgsBar.setVisibility(View.VISIBLE);
    }
}
