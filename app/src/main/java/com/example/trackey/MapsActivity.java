package com.example.trackey;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,TaskCompleted {

    private GoogleMap mMap;
    TextView txtLon,txtLat,txtName,txtDescription;
    Button btn_savedLocation;
    String stu_id;
    boolean internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_savedLocation = (Button) findViewById(R.id.btn_savedLocation);
        txtLon = (TextView) findViewById(R.id.txtLon);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtName = (TextView) findViewById(R.id.txtName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        btn_savedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                internet = isNetworkConnected();
                if(internet == false){
                    AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setTitle("Error");
                    adb.setMessage("Internet Connection Required");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // internet = isNetworkConnected()
                            dialog.dismiss();
                        }
                    });
                    adb.show();
                    return;
                }
                if(txtLon.getText().toString().equals("")||txtLat.getText().toString().equals("")||txtName.getText().toString().equals("")||txtDescription.getText().toString().equals("")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
                    //adb.setView(alertDialogView);
                    adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setTitle("Error");
                    adb.setMessage("Please Fill All the fields");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    adb.show();
                    return;
                }else {
                    savedLocation();
                }
            }
        });


        retrive();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-33.430827, 149.562633);
        float zoomLevel = 16.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                txtLon.setText(String.valueOf(latLng.longitude));
                txtLat.setText(String.valueOf(latLng.latitude));
            }
        });
    }

    public void savedLocation(){
        Intent intent = getIntent();
        stu_id = intent.getStringExtra("ID");
        String type = "savedLoaction";
        String lontitude = txtLon.getText().toString();
        String latitude = txtLat.getText().toString();
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type,lontitude,latitude,name,description,stu_id);
    }

    public void retrive(){
        String type = "retrive";
        Intent intent = getIntent();
        stu_id = intent.getStringExtra("ID");
        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type,stu_id);
    }
    @Override
    public void onTaskComplete(String result) {
        try {
            JSONArray jsonObject = new JSONArray(result);

            for(int i = 0; i < jsonObject.length(); i++) {
                JSONObject obj = jsonObject.getJSONObject(i);

                double lon = Double.valueOf(obj.getString("lon"));
                double lat = Double.valueOf(obj.getString("lat"));
                String Image_url = obj.getString("image");
                String rightImage_url = "http://glacss.com.au/trackey/"+obj.getString("image");
                LatLng mark = new LatLng(lat, lon);
                float zoomLevel = 16.0f;
                if(Image_url.equals("")){
                    mMap.addMarker(new MarkerOptions().position(mark).title(obj.getString("name")).snippet(obj.getString("description")));
                }
                else{
                    Bitmap bp = getBitmapFromURL(rightImage_url);
                    Bitmap nbp = Bitmap.createScaledBitmap(bp,100,100,false);

                    mMap.addMarker(new MarkerOptions().position(mark).title(obj.getString("name")).snippet(obj.getString("description")).icon(BitmapDescriptorFactory.fromBitmap(nbp)));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark,zoomLevel));
            }

        }
        catch(JSONException ex){

            txtLon.setText("");
            txtLat.setText("");
            txtName.setText("");
            txtDescription.setText("");
            AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
            //adb.setView(alertDialogView);
            adb.setIcon(android.R.drawable.ic_dialog_info);
            adb.setTitle("Successful");
            adb.setMessage("Sucessfully Added to Location");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    retrive();
                }
            });
            adb.show();
        }
    }

    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();



    }
}
