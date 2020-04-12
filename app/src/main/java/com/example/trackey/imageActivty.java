package com.example.trackey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class imageActivty extends AppCompatActivity implements View.OnClickListener,TaskCompleted{

    public static final String UPLOAD_URL = "http://glacss.com.au/trackey/imageUpload.php";
    public static final String UPLOAD_KEY = "image";



    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonBack;
    private Spinner spinner1;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
    String stu_id;
    boolean internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_activty);
        Intent intent = getIntent();
        stu_id = intent.getStringExtra("ID");

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        retrive();
    }

    public void retrive(){

        String type = "retrive";
        Intent intent = getIntent();
        stu_id = intent.getStringExtra("ID");
        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type,stu_id);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String location = String.valueOf(spinner1.getSelectedItem());


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(imageActivty.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                data.put("name", location);
                data.put("id", stu_id);
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }

        if(v == buttonUpload){
            internet = isNetworkConnected();
            if(internet == false){
                AlertDialog.Builder adb = new AlertDialog.Builder(imageActivty.this);
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
            String location = String.valueOf(spinner1.getSelectedItem());
            System.out.println(location);
            if(location.equals("No Location")){
                AlertDialog.Builder adb = new AlertDialog.Builder(imageActivty.this);
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setTitle("Error");
                adb.setMessage("Please select a Location or if not Add a new one");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // internet = isNetworkConnected()
                        dialog.dismiss();
                    }
                });
                adb.show();
                return;
            }

            if(imageView.getDrawable() == null){
                AlertDialog.Builder adb = new AlertDialog.Builder(imageActivty.this);
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setTitle("Error");
                adb.setMessage("Please add a image");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // internet = isNetworkConnected()
                        dialog.dismiss();
                    }
                });
                adb.show();
                return;
            }
            uploadImage();
        }
        if(v == buttonBack){
            Intent n1 = new Intent(imageActivty.this, OptionMenu.class);
            n1.putExtra("ID",stu_id);
            startActivity(n1);
            overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
        }

    }

    @Override
    public void onTaskComplete(String result) {
        spinner1 = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);

        spinner1.setAdapter(adapter);
        try {
            JSONArray jsonObject = new JSONArray(result);

            for(int i = 0; i < jsonObject.length(); i++) {
                JSONObject obj = jsonObject.getJSONObject(i);
                String name = obj.getString("name");
                list.add(name);
                adapter.notifyDataSetChanged();
            }
            if(jsonObject.length() == 0){
                list.add("No Location");
                adapter.notifyDataSetChanged();
            }
        }
        catch(JSONException ex){
           // Toast.makeText(this,"Sucessfully Added to Location" ,Toast.LENGTH_LONG).show();

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }

}
