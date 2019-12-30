package com.example.rias1.finalprojectandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    String JOSN_STRING;
    JSONObject jsonobject;
    JSONArray jsonArray;
    DBHelper mydb;
    String method;

    BackgroundWorker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        method = params[0];

        if(method.equals("insert")) {
            String users_url = "http://192.168.43.175/sqlphp/insert.php";
            try {
                String id = params[1];
                String name = params[2];
                String city = params[3];
                String phone = params[4];
                Log.v("Hashim:", "receive data in background" + id + " " + name);

                URL url = new URL(users_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String Id = "id=" + id;
                String Name = "name=" + name;
                String City = "city=" + city;
                String Phone = "phone=" + phone;
                Log.v("Hashim:", "check the data before send" + Id);
                bufferedWriter.write(Id + "&" + Name + "&" + City + "&" + Phone);
                Log.v("Hashim:", "send to php");
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.getInputStream();
                httpURLConnection.disconnect();
                return "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("get")){
            String users_url = "http://192.168.43.175/sqlphp/select.php";
            Log.v("Hashim:", "send url");
            try {
                URL url = new URL(users_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                Log.v("Hashim:", "try to take from MySQL");
                while((JOSN_STRING = bufferedReader.readLine())!= null) {
                    stringBuilder.append(JOSN_STRING+"\n");
                }
                Log.v("Hashim:", "Insert data in jason");
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

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
    }

    @Override
    protected void onPostExecute(String result) {
        if (method.equals("get")) {
            mydb = new DBHelper(context);

            try {
                Log.v("Hashim:", "take jason");
                jsonobject = new JSONObject(result.toString());
                Log.v("Hashim:", "zzzzzzzzzz");
                jsonArray = jsonobject.getJSONArray("server_response");

                int id;
                String name,city,phone;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject JO = jsonArray.getJSONObject(i);
                     id = JO.getInt("id");
                     name = JO.getString("name");
                     city = JO.getString("city");
                     phone = JO.getString("phone");
                    Log.v("Hashim:", "insert into local DB"+name);
                    mydb.insertContact(id, name, city, phone);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}