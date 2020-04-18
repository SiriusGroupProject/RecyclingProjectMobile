package com.sirius.android;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    private Button confirmButton;
    private EditText passwordText;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String urlUpdate = "http://recyclingprojectsirius.herokuapp.com/rest/users/updateUser";
    private String urlGetUser = "http://recyclingprojectsirius.herokuapp.com/rest/users/";
    private String email;
    private String name;
    private String surname;
    private double balance;
    private boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);
        Toolbar toolbar = findViewById(R.id.userProfileToolbar);
        setSupportActionBar(toolbar);
        bindViews();
        setListeners();
        sendAndRequestResponse();
    }

    private void bindViews() {

        passwordText = (EditText) findViewById(R.id.password);
        confirmButton = (Button) findViewById(R.id.changePasswordButton);
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

    private void setListeners() {
        StringBuilder string = new StringBuilder();
        string.append(urlGetUser);
        string.append(getIntent().getExtras().getString("mail"));
        new background().execute(string.toString());
    }

    class background extends AsyncTask<String, String, String> {

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
                    dosya += satir;
                }
                Log.d("****errorr",dosya);
                return dosya;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hata var";
        }

        protected void onPostExecute(String s){
            try {
                final JSONObject js = new JSONObject(s);
                email = js.getString("email");
                name = js.getString("name");
                surname = js.getString("surname");
                balance = js.getDouble("balance");
                admin = js.getBoolean("admin");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAndRequestResponse() {

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String password = passwordText.getText().toString();
                if (password.length() != 0) {
                    JSONObject js = new JSONObject();
                    try {
                        js.put("name",name);
                        js.put("surname",surname);
                        js.put("email",email);
                        js.put("password",password);
                        js.put("balance",balance);
                        js.put("isAdmin",admin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(js);
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                            Request.Method.PUT, urlUpdate, js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("**", response.toString());
                                    Intent intent = new Intent(NewPassword.this, UserProfile.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Şifreniz başarıyla değiştirilmiştir.", Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("**", error.toString());
                            Toast.makeText(getApplicationContext(), "Bağlantı problemi. İnternet ayarlarınızı kontrol ediniz.", Toast.LENGTH_LONG).show();
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
                    Volley.newRequestQueue(NewPassword.this).add(jsonObjReq);

                }
                else{
                        // toast message
                }
            }
        });
    }

}
