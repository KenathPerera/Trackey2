package com.example.trackey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String>{

    Context context;
    AlertDialog alertDialog;
    String type = "";
    String overallResult = "";
    private TaskCompleted mCallback;

    BackgroundWorker(Context ctx){
        context = ctx;
        this.mCallback = (TaskCompleted) ctx;
    }
    @Override
    protected String doInBackground(String... params) {

        String login_url = "http://glacss.com.au/trackey/login.php";
        String signUP_url = "http://glacss.com.au/trackey/SignUp.php";
        String location_url = "http://glacss.com.au/trackey/Location.php";
        String retrive_url = "http://glacss.com.au/trackey/locationRetrive.php";
        type = params[0];

        if (type.equals("login")) {
            try {
                String studentId = params[1];
                String Password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("studentId", "UTF-8") + "=" + URLEncoder.encode(studentId, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(type.equals("signUp")){
            try {
                String studentID = params[1];
                String password = params[2];
                URL url = new URL(signUP_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("studentId", "UTF-8") + "=" + URLEncoder.encode(studentID, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                System.out.println(post_data);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("savedLoaction")){
            try {
                String lontitude = params[1];
                String latitude = params[2];
                String name = params[3];
                String description = params[4];
                String stu_id = params[5];
                URL url = new URL(location_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("lontitude", "UTF-8") + "=" + URLEncoder.encode(lontitude, "UTF-8") + "&"
                        + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8")+ "&"
                        + URLEncoder.encode("locName", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")
                        + "&"+ URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(stu_id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                //System.out.println(result);
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if(type.equals("retrive")){
            try {
                String stu_id = params[1];
                URL url = new URL(retrive_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(stu_id, "UTF-8") ;
                System.out.println(post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        if (type.equals("login")){
            alertDialog.setTitle("Login Status");
        }
        else{
            alertDialog.setTitle("Resistration Status");
        }



    }

    @Override
    protected void onPostExecute(String result) {

        mCallback.onTaskComplete(result);
//       overallResult = result;
//        if (type.equals("login") ){
//            if (result.equals("true")){
//                alertDialog.setMessage("Login successful");
//
//                //overallResult = true;
//            }
//            else {
//                alertDialog.setMessage("Login Fail");
//                //overallResult = false;
//            }
//        }
//        else if(type.equals("signUp")){
//            if (result.equals("true")){
//                alertDialog.setMessage("signUp successful");
//                //overallResult = true;
//            }
//            else {
//                alertDialog.setMessage("SignUP Fail");
//                //overallResult = false;
//            }
//        }
//
//        alertDialog.show();


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

//    public boolean result(){
//        if (overallResult == true){
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }


}
