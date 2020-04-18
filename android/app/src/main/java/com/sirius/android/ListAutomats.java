package com.sirius.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

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

public class ListAutomats extends AppCompatActivity implements
        OnMarkerClickListener,
        OnInfoWindowClickListener,
        OnInfoWindowLongClickListener,
        OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private static final LatLng SOGUTOZU = new LatLng(39.97999954223633, 32.75);
    private static final LatLng DEMETEVLER = new LatLng(39.96727752685547, 32.76835632324219);
    private static final LatLng SALIHLER = new LatLng(40.13569259643555, 32.164833068847656);
    private static final LatLng MEBUSEVLERI = new LatLng(39.926185607910156, 32.83745574951172);

    //NOT ACTIVE SO DONT SHOW
    //private static final LatLng KUCUKESAT = new LatLng(39.90989685058594, 32.867218017578125);

    //DATABASE'TEN CEKILDIGI ZAMAN
    // private String getUrl = "http://192.168.1.6:8080/rest/automats/listAutomats";
    private static RequestQueue mRequestQueue;
    private static String mStringRequest;
    private static String userId;

    private GoogleMap mMap;

    private Marker mSogutozu;
    private Marker mDemet;
    private Marker mSalihler;
    private Marker mMebusevleri;
    //private Marker mKucukesat;

    private Marker mLastSelectedMarker;

    private List<String> automatInfos = new ArrayList<>();
    private List<Marker> mMarkerRainbow = new ArrayList<>(); // list olarak

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            // what
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            badge = R.drawable.washing_machine_32;

            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                //degisik renkler istersen: snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }


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

        final ImageView addButton = (ImageView) findViewById(R.id.addButton);

        if (mLastSelectedMarker != null && mLastSelectedMarker.isInfoWindowShown()) {
            // Refresh the info window when the info window's content has changed.
            mLastSelectedMarker.showInfoWindow();
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
        //IDK WHAT mapFragment.getMapAsync(this);

        /*mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONArray objectArray = new JSONArray(response);
                    final int length = objectArray.length();
                    for (int i = 0; i < length; ++i) {
                        final JSONObject automat = objectArray.getJSONObject(i);
                        String id = automat.getString("id");
                        if(id.length()==8){
                            String username = user.getString("name");
                            String usersurname = user.getString("surname");
                            name = username+ " "+usersurname;
                            name = name.toUpperCase();
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

        mRequestQueue.add(mStringRequest);*/

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
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls as the button panel will cover it. FALSE'tu true yaptım idk
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        enableMyLocation();

        // Add lots of markers to the map.
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Automat Locations");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(SOGUTOZU)
                .include(DEMETEVLER)
                .include(SALIHLER)
                .include(MEBUSEVLERI)
                //.include(KUCUKESAT)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50)); // ZOOM ICIN BURAYI DEGISTIR 14 yapmis insanlar
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private void addMarkersToMap() {
        /* Uses a colored icon.
        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        */
        // Uses a custom icon with the info window popping out of the center of the icon.
        mSogutozu = mMap.addMarker(new MarkerOptions()
                .position(SOGUTOZU)
                .title("Söğütözü")
                .snippet("Kapasite: 95.5 Yer: Söğütözü,Çankaya,Ankara")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle_32))
                .infoWindowAnchor(0.5f, 0.5f));
        // Uses a custom icon with the info window popping out of the center of the icon.
        mDemet = mMap.addMarker(new MarkerOptions()
                .position(DEMETEVLER)
                .title("Demetevler")
                .snippet("Kapasite: 45 Yer: Demetevler,Yenimahalle,Ankara")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle_32))
                .infoWindowAnchor(0.5f, 0.5f));
        // Uses a custom icon with the info window popping out of the center of the icon.
        mSalihler = mMap.addMarker(new MarkerOptions()
                .position(SALIHLER)
                .title("Salihler")
                .snippet("Kapasite: 45 Yer: Salihler,Güdül,Ankara")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle_32))
                .infoWindowAnchor(0.5f, 0.5f));
        // Uses a custom icon with the info window popping out of the center of the icon.
        mMebusevleri = mMap.addMarker(new MarkerOptions()
                .position(MEBUSEVLERI)
                .title("Mebusevleri")
                .snippet("Kapasite: 45 Yer: Mebusevleri,Çankaya,Ankara")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle_32))
                .infoWindowAnchor(0.5f, 0.5f));

    }
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        /*if (marker.equals(mPerth)) {
            // This causes the marker at Perth to bounce into position when it is clicked.
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(
                            1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        } else if (marker.equals(mAdelaide)) {
            // This causes the marker at Adelaide to change color and alpha.
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(mRandom.nextFloat() * 360));
            marker.setAlpha(mRandom.nextFloat());
        }

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(this, marker.getTitle() + " z-index set to " + zIndex,
                Toast.LENGTH_SHORT).show();*/

        mLastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, R.string.info_window_click, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(this, R.string.info_window_click, Toast.LENGTH_SHORT).show();
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
