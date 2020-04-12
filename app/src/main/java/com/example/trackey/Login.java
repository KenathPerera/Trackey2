package com.example.trackey;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;

public class Login extends AppCompatActivity implements TaskCompleted{
    private BroadcastReceiver MyReceiver = null;

    Button btnSignIn, btnSignUp;
    TextView txtUserId,txtPassword;
    boolean internet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        MyReceiver = new MyReceiver();
        broadcastIntent();

        txtUserId = (TextView) findViewById(R.id.txtUserId);
        txtPassword = (TextView) findViewById(R.id.txtPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                internet = isNetworkConnected();
                if(internet == false){
                    System.out.println("here");
                    AlertDialog.Builder adb = new AlertDialog.Builder(Login.this);
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
                else{
                    login();
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n1 = new Intent(Login.this, SignUp.class);
                startActivity(n1);
                overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
            }
        });
    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    public void login(){
        String type = "login";
        String studentId = txtUserId.getText().toString();
        String password = txtPassword.getText().toString();

        BackgroundWorker BackgroundWorker = new BackgroundWorker(this);
        BackgroundWorker.execute(type,studentId,password);

    }

    @Override
    public void onTaskComplete(String result) {

        //Toast.makeText(this,"The result is " + result,Toast.LENGTH_LONG).show();

        if(result.equals("true")){

            Intent n1 = new Intent(Login.this, OptionMenu.class);
            n1.putExtra("ID",txtUserId.getText().toString());
            txtUserId.setText("");
            txtPassword.setText("");
            startActivity(n1);
            overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_left);
        }
        else{
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            //adb.setView(alertDialogView);
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setTitle("Error");
            adb.setMessage("Please Enter Valid Student ID and a Password");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            adb.show();
        }


    }
//
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();



    }

}
