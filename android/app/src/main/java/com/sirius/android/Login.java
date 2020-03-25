package com.sirius.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    private String name;
    private String password;
    private String url = "http://172.20.10.3:8080/rest/users/login";
    private Button loginButton;
    private EditText nameText, passwordText;
    private TextView createAccountLink;

    private static final int REQUEST_LOCATION = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener mlocListener = new MyLocationListener();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        setContentView(R.layout.login);
        bindViews();
        setListeners();
    }

    public class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location loc)
        {
            //System.out.println(("My current location is: " +"Latitud = " + loc.getLatitude() +"Longitud = " + loc.getLongitude()));
            if (loc != null) {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());
            }
        }

        private class GeocoderHandler extends Handler {
            public void handleMessage(Message message) {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = null;
                }
                //System.out.println((locationAddress));
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps erişilemiyor", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }


    private void bindViews() {
        nameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountLink = (TextView) findViewById(R.id.create_account_link);

    }

    private void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString();
                password = passwordText.getText().toString();
                StringBuilder string = new StringBuilder();
                string.append(url);
                string.append("?email=");
                string.append(name);
                string.append("&password=");
                string.append(password);
                new sendAndRequestResponse().execute(string.toString());
            }
        });
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
    class sendAndRequestResponse extends AsyncTask<String, String, String> {

        protected String doInBackground (String ...params){
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya = "";
                while ((satir = br.readLine()) != null) {
                    Log.d("satir:", satir);
                    dosya += satir;
                }
                return dosya;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hata var";
        }
        protected void onPostExecute(String s){
            if (s.equals("true")) {
                FirebaseMessaging.getInstance().subscribeToTopic("a");
                Intent intent = new Intent(Login.this, ListAutomats.class);
                Bundle b = new Bundle();
                b.putString("userID",nameText.getText().toString()); //Your id
                intent.putExtras(b);
                startActivity(intent);
                Intent intentTransfer = new Intent(Login.this, UserProfile.class);
                intentTransfer.putExtra("mail", nameText.getText().toString()); // nameText.getText().toString()
            }else if(s.equals("false")){
                Toast.makeText(getApplicationContext(), "Yanlış şifre ya da email. Lüften tekrar deneyiniz.", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "Bağlantı problemi. İnternet ayarlarınızı kontrol ediniz.", Toast.LENGTH_LONG).show();
            }
        }
    }
}