package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserProfile extends AppCompatActivity {

    private String mail;
    private String mailTemp;
    private String name;
    private double balance;
    private String balanceFinal;
    private String url = "http://172.20.10.10:8080/rest/users/listUsers";
    private TextView mailText;
    private TextView nameText;
    private TextView balanceText;
    private Button edit;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = findViewById(R.id.userProfileToolbar);
        setSupportActionBar(toolbar);
        bindViews();
        sendAndRequestResponse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_for_profile, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            // logout
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {
        edit = (Button) findViewById(R.id.settings);
        mailText = (TextView)findViewById(R.id.mail);
        mailText.setText(getIntent().getExtras().getString("mail"));
        mail = mailText.toString();
    }


    private void sendAndRequestResponse() {

        name = mailText.getText().toString();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, NewPassword.class);
                startActivity(intent);
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONArray objectArray = new JSONArray(response);
                    final int length = objectArray.length();
                    for (int i = 0; i < length; ++i) {
                        final JSONObject user = objectArray.getJSONObject(i);
                        mailTemp = user.getString("email");
                        if(mailTemp.equals(mail)){
                            name = user.getString("name"+" "+"surname");
                            balance = user.getDouble("balance");
                            balanceFinal = Double.toString(balance);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nameText = (TextView) findViewById(R.id.name);
                balanceText = (TextView) findViewById(R.id.balance);
                nameText.setText(name);
                balanceText.setText(balanceFinal);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
