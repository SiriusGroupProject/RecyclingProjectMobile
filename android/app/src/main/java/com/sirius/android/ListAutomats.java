package com.sirius.android;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ListAutomats extends AppCompatActivity{

    //private String url = "http://www.mocky.io/v2/5e4788f93000007900294963";
    private String url = "http://www.mocky.io/v2/5e479ea73000007400294989";
    private static String[] tableHeaders={"İsim","Konum","Uzaklık","Doluluk"};
    private static RequestQueue mRequestQueue;
    private static JsonArrayRequest mArrayRequest;
    private static String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_automats);
        Toolbar toolbar = findViewById(R.id.listAutomatsToolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        userId = ""; // or other values
        if(b != null)
            userId = b.getString("userID");

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        final ImageView addButton = (ImageView) findViewById(R.id.addButton);

        tableView.setHeaderBackgroundColor(Color.parseColor("#28c678"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tableHeaders));
        tableView.setColumnCount(4);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try{

                    String[][] array = new String[response.length()][4];

                    for (int i=0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        array[i][0] = obj.getString("automat_name");
                        array[i][1] = obj.getString("automat_location");
                        array[i][2] = String.valueOf(obj.getDouble("automat_distance"));
                        array[i][3] = obj.getString("automat_availability");
                    }
                    tableView.setDataAdapter(new SimpleTableDataAdapter(ListAutomats.this, array));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });

        mRequestQueue.add(mArrayRequest);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAutomats.this, ScanQRAutomat.class);
                Bundle b = new Bundle();
                b.putString("userID",userId); //Your id
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent profile = new Intent(this, UserProfile.class);
            profile.putExtra("mail", userId);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
