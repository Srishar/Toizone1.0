package com.example.arvind.detailsui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Complain extends AppCompatActivity {

    Spinner spinner;
    Button subc;
    EditText unam,ptnam,cont;
    String category,username,ptname,content;
    ArrayAdapter<CharSequence>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        spinner=(Spinner)findViewById(R.id.spinner);
        unam=(EditText)findViewById(R.id.unam);
        ptnam=(EditText)findViewById(R.id.ptnam);
        cont=(EditText)findViewById(R.id.cont);
        subc=(Button) findViewById(R.id.subc);
        adapter=ArrayAdapter.createFromResource(this,R.array.problem,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayList<Double>arr=new ArrayList<>(2);
        ArrayList<String>arr1=new ArrayList<>(2);
        arr.add(1.0);
        arr.add(2.0);
        arr1.add("one");arr1.add("two");
        

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        category = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
        subc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=unam.getText().toString();
                ptname=ptnam.getText().toString();
                content=cont.getText().toString();
                Toast.makeText(getApplicationContext(),"Thanks for Your feedback, we will solve this issue soon",Toast.LENGTH_LONG).show();
            }
        });
    }
}
