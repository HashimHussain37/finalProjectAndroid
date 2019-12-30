package com.example.rias1.finalprojectandroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.rias1.finalprojectandroid.DBHelper.CONTACTS_COLUMN_CITY;
import static com.example.rias1.finalprojectandroid.DBHelper.CONTACTS_COLUMN_ID;
import static com.example.rias1.finalprojectandroid.DBHelper.CONTACTS_COLUMN_NAME;
import static com.example.rias1.finalprojectandroid.DBHelper.CONTACTS_COLUMN_PHONE;

public class MainActivity extends AppCompatActivity {
    DBHelper mydb;
    String cities[]={"Riyadh","Jeddah","Mecca","Medina","Al-Ahsa","Ta'if","Dammam","Buraidah"};
    Button btnSearch,btnShowAll,btnInsert,btnDelete,btnUpdate,btnGetByID,btnSync;
    EditText editID,editName,editPhone;
    AutoCompleteTextView autoComTextCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //link the bttn & editText with XML
        mydb = new DBHelper(this);
        // define buttons
        this.btnInsert=(Button)findViewById(R.id.btnInsert);
        this.btnSearch=(Button)findViewById(R.id.btnSearch);
        this.btnShowAll=(Button)findViewById(R.id.btnShowAll);
        this.btnDelete=(Button)findViewById(R.id.btnDelete);
        this.btnUpdate=(Button)findViewById(R.id.btnUpdate);
        this.btnGetByID=(Button)findViewById(R.id.btnGetByID);
        this.btnSync=(Button)findViewById(R.id.btnSyn);

        //edite text
        this.autoComTextCity=(AutoCompleteTextView)findViewById(R.id.autoComTextCity);
        this.editID=(EditText)findViewById(R.id.editID);
        this.editName=(EditText)findViewById(R.id.editName);
        this.editPhone=(EditText)findViewById(R.id.editPhone);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,cities);
        autoComTextCity.setThreshold(2);
        autoComTextCity.setAdapter(adapter);
    }

    public void Insert(View v) {
        Log.v("Hashim", "clicked on inserst localDB");
        if(!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")) {
            int id = Integer.parseInt(editID.getText().toString());
            String name = editName.getText().toString();
            String city = autoComTextCity.getText().toString();
            String phone = editPhone.getText().toString();

            if (mydb.insertContact(id, name, city, phone)) {
                Log.v("Hashim", "Successfully inserted record to localdb");
                Toast.makeText(getApplicationContext(),"Inserted: " + name , Toast.LENGTH_SHORT).show();
            } else {
                Log.v("Hashim", "failed to inserte record to localdb");
                Toast.makeText(getApplicationContext(), "Did NOT insert to localdb :-(", Toast.LENGTH_SHORT).show();
            }
            clearFields();
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();
        }
    }
    public void ShowAll(View v){
        Log.v("Hashim", "clicked on Show all");
        ArrayList<String> fetchAll = new ArrayList<String>();
        fetchAll = mydb.getAllContacts();
        Intent intent = new Intent(this, showAll.class);
        intent.putStringArrayListExtra("fetchAll", fetchAll);
        startActivity(intent);
        if(!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearFields(){
        editID.setText("");
        editName.setText("");
        editPhone.setText("");
        autoComTextCity.setText("");
        editID.setEnabled(true);
        btnShowAll.setEnabled(true);
        btnSearch.setEnabled(true);
        btnInsert.setEnabled(true);
        btnSync.setEnabled(true);
    }

    public void sershBtn(View v){
        Intent intent = new Intent(getApplicationContext(),Search.class);
        Log.v("Hashim:", "intent executed");
        Log.v("Hashim:","change the Activity to search ");
        startActivity(intent);
        Log.v("Hashim:", "startActivity executed");
        if(!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();
        }
    }

    public void syncBtn(View v){
        Log.v("Hashim:", "click on sync");
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        Log.v("Hashim:", "get all data");
        Cursor res = mydb.getAllData();
        if (res.moveToFirst()) {
            do{
                String id = res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ID))+"";
                String name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME))+"";
                String city = res.getString(res.getColumnIndex(CONTACTS_COLUMN_CITY))+"";
                String phone = res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
                Log.v("Hashim:", "send data to background"+id+" "+name);
                backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute("insert",id,name,city,phone);
            }while (res.moveToNext());
            Toast.makeText(getApplicationContext(), "Data sent to MySql", Toast.LENGTH_LONG).show();
        }

        if(mydb.deleteAllData()){
            Toast.makeText(getApplicationContext(), "local DB is dropped", Toast.LENGTH_LONG).show();
        }

        else if (!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),
                        "Please complete all data", Toast.LENGTH_SHORT).show();

        }
        // take the all data from MySQL
        getDataFromMySQL();
    }

    public void getDataFromMySQL(){
        Toast.makeText(getApplicationContext(), "Take all data from MySQL", Toast.LENGTH_LONG).show();
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker = new BackgroundWorker(this);
        Log.v("Hashim", "Go to background");
        backgroundWorker.execute("get");
        if (!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();

        }
    }

    public void getByIDBtn(View v){
        if (!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();
        }
        Log.v("Hashim", "clicked on GetByID Button");
        //editTextID.setVisibility(View.VISIBLE);
        String getID = editID.getText().toString();
        int intID=Integer.parseInt(getID);
        Log.v("Hashim", "clicked on fetch");
        Cursor getData=mydb.getData(intID); //specific record (id=1)

        if (getData.moveToNext()) {// data?
            Log.v("Hashim", "data found in DB...");
            String dName = getData.getString(getData.getColumnIndex("name"));
            //  String dPhone = getData.getString(getData.getColumnIndex("phone"));
            // String dEmail = getData.getString(getData.getColumnIndex("email"));
            Log.v("Hashim", "set the string in edite text");
            editName.setText(dName);
            autoComTextCity.setText(getData.getString(getData.getColumnIndex("city")));
            editPhone.setText(getData.getString(getData.getColumnIndex("phone")));
            editID.setEnabled(false);
            btnShowAll.setEnabled(false);
            btnSearch.setEnabled(false);
            btnInsert.setEnabled(false);
            btnSync.setEnabled(false);
        }
        else
            Toast.makeText(getApplicationContext(),
                    "did not get any data...:-(", Toast.LENGTH_LONG).show();
        getData.close();
    }

    public void deleteBtn(View v){
        Log.v("Hashim", "clicked on Delete Button");

        Log.v("Hashim", "Get the ID");
        String getID = editID.getText().toString();
        int intID=Integer.parseInt(getID);
        Log.v("Hashim", "send the ID");
        if(mydb.deleteContact(intID)){
            Toast.makeText(getApplicationContext(),
                    "1 record has been deleted", Toast.LENGTH_LONG).show();
        }
        else if (!editID.getText().toString().trim().equals("")&&!editName.getText().toString().trim().equals("")&&!autoComTextCity.getText().toString().trim().equals("")&&!editPhone.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),
                    "Please complete all data", Toast.LENGTH_SHORT).show();

        }
        Log.v("Hashim", "clear fileds ");
        clearFields();
    }

    public void updateBtn(View v){
        Log.v("Hashim", "clicked on Update  Button");
        String getID = editID.getText().toString();
        int intID=Integer.parseInt(getID);
        String getName = editName.getText().toString();
        String getPhone = editPhone.getText().toString();
        String getCity = autoComTextCity.getText().toString();

        if (mydb.updateContact(intID,getName,getCity,getPhone)) {
            Log.v("Hashim", "Successfully update record to db");
            Toast.makeText(getApplicationContext(),
                    "upadteing:" + getName + ", " + getCity + "," , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "DID NOT update the record :-(", Toast.LENGTH_SHORT).show();
        }
        clearFields();
    }
}

