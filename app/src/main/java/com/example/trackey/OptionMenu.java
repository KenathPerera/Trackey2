package com.example.trackey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OptionMenu extends AppCompatActivity implements TaskCompleted{
    Button btnLoadCampusMap, btnAddImageLocation,btnAddLocation,btnsignOut;
    String stu_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);

        Intent intent = getIntent();
        stu_id = intent.getStringExtra("ID");
        System.out.println(stu_id);

        btnAddLocation = (Button) findViewById(R.id.btnAddLocation);
        btnLoadCampusMap = (Button) findViewById(R.id.btnLoadCampusMap);
        btnAddImageLocation = (Button) findViewById(R.id.btnAddImageLocation);
        btnsignOut = (Button) findViewById(R.id.btnsignOut);

        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //login();
                Intent n1 = new Intent(OptionMenu.this, MapsActivity.class);
                n1.putExtra("ID",stu_id);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });

        btnLoadCampusMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //login();
                Intent n1 = new Intent(OptionMenu.this, LoadLocation.class);
                n1.putExtra("ID",stu_id);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });

        btnAddImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrive();
                Intent n1 = new Intent(OptionMenu.this, imageActivty.class);
                n1.putExtra("ID",stu_id);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });

        btnsignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrive();
                Intent n1 = new Intent(OptionMenu.this, Login.class);
                n1.putExtra("ID",stu_id);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });


    }

    public void retrive(){
        String type = "retrive";
        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type);


    }
    @Override
    public void onTaskComplete(String result) {

        Toast.makeText(this,"The result is " + result,Toast.LENGTH_LONG).show();
        try {
           // JSONObject finalObject = new JSONObject(result);
            JSONArray jsonObject = new JSONArray(result);

            for(int i = 0; i < jsonObject.length(); i++) {
                JSONObject obj = jsonObject.getJSONObject(i);
                Toast.makeText(this,"The result is " + obj.getString("table_id"),Toast.LENGTH_LONG).show();
                //store your variable
                //list.add(obj.getString("Name"));
            }

        }
        catch(JSONException ex){
            Toast.makeText(this,"The result is " + ex,Toast.LENGTH_LONG).show();
        }
//        if(result.equals("true")){
//            Intent n1 = new Intent(Login.this, OptionMenu.class);
//            startActivity(n1);
//            overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
//        }
//        else{
//            //FailTxt.setText("ID already being registerd");
//        }


    }
}
