package com.sirius.android;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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

public class Login extends AppCompatActivity {
    private String name;
    private String password;
    private String url = "http://172.20.10.10:8080/rest/users/login";
    boolean success = false; // default false
    private Button loginButton;
    private EditText nameText,passwordText;
    private TextView createAccountLink;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bindViews();
        sendAndRequestResponse();
    }

    private void bindViews() {
        nameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountLink = (TextView) findViewById(R.id.create_account_link);

    }

    private void sendAndRequestResponse() {

        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameText.getText().toString();
                password = passwordText.getText().toString();
                if (name.length() != 0 && password.length() != 0) {

                    mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                if(response .equals("true")){
                                    Intent intent = new Intent(Login.this, ListAutomats.class);
                                    startActivity(intent);
                                } else if(response.equals("false")){
                                    Toast.makeText(getApplicationContext(), "Yanlış şifre ya da e-mai. Tekrar deneyiniz!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Bağlantı problemi", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                        }
                    });

                    mRequestQueue.add(mStringRequest);

                }
                else{

                }
            }
        });

    }
}
