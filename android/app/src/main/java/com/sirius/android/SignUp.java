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

public class SignUp extends AppCompatActivity {

    private String url = "http://172.20.10.10:8080/rest/users/addUser"; // ip edit
    private Button signUpButton;
    private EditText nameText, surnameText, emailText, passwordText;
    private TextView signInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        bindViews();
        sendAndRequestResponse();
    }

    private void bindViews() {
        nameText = (EditText) findViewById(R.id.name);
        surnameText = (EditText) findViewById(R.id.surname);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signInLink = (TextView) findViewById(R.id.sign_in_account_link);
    }

    private void sendAndRequestResponse() {

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String surname = surnameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                JSONObject js = new JSONObject();
                    try {
                        js.put("name",name);
                        js.put("surname",surname);
                        js.put("email",email);
                        js.put("password",password);
                        js.put("balance",(float)0);
                        js.put("isAdmin",false);
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
                    Volley.newRequestQueue(SignUp.this).add(jsonObjReq);

                }

        });

    }

}
