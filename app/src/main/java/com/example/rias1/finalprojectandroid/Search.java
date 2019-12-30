package com.example.rias1.finalprojectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    Button btnnSearch;
    EditText searchValu;
    RadioButton r1,r2,r3,r4;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ArrayAdapter<String> arrayAdapter;

        mydb = new DBHelper(this);
        btnnSearch=(Button)findViewById(R.id.bttnS);
        searchValu=(EditText)findViewById(R.id.editSerach);
        r1=(RadioButton)findViewById(R.id.radioButton1);//id
        r2=(RadioButton)findViewById(R.id.radioButton2);//name
        r3=(RadioButton)findViewById(R.id.radioButton3);//city
        r4=(RadioButton)findViewById(R.id.radioButton4);//phone
        Log.v("Hashim ", "connect the widest with XML File");

        btnnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sortType="";

                Log.v("Hashim", "clicked on search  Button");
                ArrayList<String> fetchAll = new ArrayList<String>();
                String getSearchValue = searchValu.getText().toString();
                if(r1.isChecked()) {
                    fetchAll = mydb.getAllContacts3(getSearchValue, "id");
                    for (String a : fetchAll)
                        Log.v("Hashim:", a.toString());

                    Intent intent = new Intent(getApplicationContext(), showAll.class);
                    Log.v("Hashim:", "intent executed");
                    intent.putStringArrayListExtra("fetchAll", fetchAll);
                    Log.v("Hashim:", "fetchALL executed");
                    startActivity(intent);
                    Log.v("Hashim:", "startActivity executed");
                }
                else if(r2.isChecked()) {
                    fetchAll = mydb.getAllContacts3(getSearchValue, "name");
                    for (String a : fetchAll)
                        Log.v("Hashim:", a.toString());

                    Intent intent = new Intent(getApplicationContext(), showAll.class);
                    Log.v("Hashim:", "intent executed");
                    intent.putStringArrayListExtra("fetchAll", fetchAll);
                    Log.v("Hashim:", "fetchALL executed");
                    startActivity(intent);
                    Log.v("Hashim:", "startActivity executed");
                }
                else if(r3.isChecked()) {
                    fetchAll = mydb.getAllContacts3(getSearchValue, "city");
                    for (String a : fetchAll)
                        Log.v("Hashim:", a.toString());

                    Intent intent = new Intent(getApplicationContext(), showAll.class);
                    Log.v("Hashim:", "intent executed");
                    intent.putStringArrayListExtra("fetchAll", fetchAll);
                    Log.v("Hashim:", "fetchALL executed");
                    startActivity(intent);
                    Log.v("Hashim:", "startActivity executed");
                }
                else if(r4.isChecked()) {
                    fetchAll = mydb.getAllContacts3(getSearchValue, "phone");
                    for (String a : fetchAll)
                        Log.v("Hashim:", a.toString());

                    Intent intent = new Intent(getApplicationContext(), showAll.class);
                    Log.v("Hashim:", "intent executed");
                    intent.putStringArrayListExtra("fetchAll", fetchAll);
                    Log.v("Hashim:", "fetchALL executed");
                    startActivity(intent);
                    Log.v("Hashim:", "startActivity executed");
                }
            }
        });

    }
}
