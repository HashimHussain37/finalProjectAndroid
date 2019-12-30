package com.example.rias1.finalprojectandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class showAll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        ListView resList= (ListView) findViewById(R.id.listView);
        ArrayList<String> getData = getIntent().getExtras().getStringArrayList("fetchAll");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, getData);
        resList.setAdapter(arrayAdapter);
    }
}
