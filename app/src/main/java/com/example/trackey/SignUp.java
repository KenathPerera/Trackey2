package com.example.trackey;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements TaskCompleted{

    Button btnSignUp,btnBack;
    TextView txtUserId,txtPassword,FailTxt;
    boolean internet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = (Button) findViewById(R.id.btnSignUP);
        btnBack = (Button) findViewById(R.id.btnBack);

        txtUserId = (TextView) findViewById(R.id.txtUserId);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        FailTxt = (TextView) findViewById(R.id.FailTxt);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                internet = isNetworkConnected();
                if(internet == false){
                    AlertDialog.Builder adb = new AlertDialog.Builder(SignUp.this);
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
                FailTxt.setText("");
                if(txtUserId.getText().toString().equals("")||txtPassword.getText().toString().equals("")){
                    FailTxt.setText("Required fields are empty");
                    return;
                }else{
                    signUp();
                }


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n1 = new Intent(SignUp.this, Login.class);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });



    }


    public void signUp(){
        String type = "signUp";
        String studentId = txtUserId.getText().toString();
        String password = txtPassword.getText().toString();
        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type,studentId,password);

    }

    @Override
    public void onTaskComplete(String result) {

        if(result.equals("true")){
            FailTxt.setText("Successfully Registered");
            txtUserId.setText("");
            txtPassword.setText("");
        }
        else{
            FailTxt.setText("ID already being registered");
        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();



    }
}
