package com.sirius.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private String name;
    private String password;
    private String url = " ";
    boolean success = true; // default false
    private Button loginButton;
    private EditText nameText,passwordText;
    private TextView createAccountLink;
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

                JSONObject js = new JSONObject();
                try {
                    js.put("name",name);
                    js.put("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(js);
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, url, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("**", response.toString());
                                try {
                                    success = response.getBoolean("success");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(success == true){
                                    new Intent(Login.this, UserProfile.class);
                                }
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
                // Adding request to request queue
                Volley.newRequestQueue(Login.this).add(jsonObjReq);

            }

        });

    }
}
