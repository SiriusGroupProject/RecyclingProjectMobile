package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class UserBalance extends AppCompatActivity {

    private String userId;
    private String automatId;
    private double balance;
    private String getUrl = "http://10.0.2.2:8080/rest/users/";
    private String putUrl = "http://10.0.2.2:8080/rest/users/updateBalance/";
    private double userOldBalance;
    private double earnedBalance;
    private Button totalBalanceButton;
    private TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_balance);
        Toolbar toolbar = findViewById(R.id.userBalanceToolbar);
        setSupportActionBar(toolbar);
        bindViews();

        Bundle b = getIntent().getExtras();
        userId = "";
        automatId = "";
        balance = 0.0;
        if(b != null) {
            userId = b.getString("userID");
            automatId = b.getString("automatID");
            balance = b.getDouble("balance");
            System.out.println(userId);
            System.out.println(automatId);
            System.out.println(balance);
        }

        getUrl = getUrl + userId;


        JsonObjectRequest getUserBalance = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            userOldBalance = response.getDouble("balance");
                            earnedBalance = balance - userOldBalance;
                            balanceText.setText(Double.toString(earnedBalance));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        Volley.newRequestQueue(UserBalance.this).add(getUserBalance);

        putUrl = putUrl + userId + "/" + earnedBalance;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, putUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        )
        ;

        Volley.newRequestQueue(UserBalance.this).add(putRequest);

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