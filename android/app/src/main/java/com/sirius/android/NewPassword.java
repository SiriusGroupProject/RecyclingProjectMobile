package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    private Button confirmButton;
    private EditText passwordText;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://172.20.10.10:8080/rest/users/updateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);
        Toolbar toolbar = findViewById(R.id.userProfileToolbar);
        setSupportActionBar(toolbar);
        bindViews();
        sendAndRequestResponse();
    }

    private void bindViews() {

        passwordText = (EditText) findViewById(R.id.password);
        confirmButton = (Button) findViewById(R.id.loginButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent profile = new Intent(this, UserProfile.class);
            startActivity(profile);
            return true;
        }
        else if (id == R.id.action_logout) {
            // logout
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendAndRequestResponse() {

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String password = passwordText.getText().toString();

                if (password.length() != 0) {
                    JSONObject js = new JSONObject();
                    try {
                        js.put("password",password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            Request.Method.POST, url, js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("**", response.toString());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("**", error.toString());
                            Toast.makeText(getApplicationContext(), "Connection problem. Please check your connection", Toast.LENGTH_LONG).show();
                        }
                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }

                    };
                    Volley.newRequestQueue(NewPassword.this).add(jsonObjReq);
                }
                else{
                        // toast message
                }
            }
        });
    }

}
