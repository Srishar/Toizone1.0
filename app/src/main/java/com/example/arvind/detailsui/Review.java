package com.example.arvind.detailsui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Review extends AppCompatActivity {

    String link,time;

    Firebase ref,timeref;
    //int temp;
    static int cnt=0,temp;
    double ratt1;
    Map<String, String> map1;
    Firebase maindb;
    EditText rev;
    EditText uname;
    Button submit;
    RatingBar rb;
    Spinner spinner;
    ArrayAdapter<CharSequence>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        link=getIntent().getExtras().getString("ptlink");



        rev=(EditText)findViewById(R.id.rev);
        uname=(EditText)findViewById(R.id.uname);
        submit=(Button)findViewById(R.id.submit);
        rb=(RatingBar)findViewById(R.id.rb);
        spinner=(Spinner)findViewById(R.id.ts);
        adapter= ArrayAdapter.createFromResource(this,R.array.timing,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String deviceid= Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

        maindb = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/details");
        timeref = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/peak");

        maindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map1 = dataSnapshot.getValue(Map.class);

                String rat=map1.get("rating");
                ratt1=Double.parseDouble(rat);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ref=new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/Reviews/"+deviceid);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog1=new ProgressDialog(Review.this);
                progressDialog1.setMessage("Submitting review...");
                progressDialog1.show();
                float val=rb.getRating();
                String vall=val+"";
                String user=uname.getText().toString();
                String revval=rev.getText().toString();
                Firebase child=ref.child("name");
                child.setValue(user);
                Firebase child1=ref.child("rating");
                child1.setValue(vall);
                Firebase child2=ref.child("rev");
                child2.setValue(revval);
                Calendar cal=Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                String dateval= sdf.format(new Date());
                Firebase child3=ref.child("date");
                child3.setValue(dateval);
                if(time.equals("12AM-04AM"))
                {
                 //   cnt=countcalc("00");
                    //++cnt;
                    countcalc("00");
                    //timeref.child("00").setValue(cnt);
                }
                else if(time.equals("04AM-06AM"))
                {
                   // cnt=countcalc("04");
                    //++cnt;
                    countcalc("04");
                    //timeref.child("04").setValue(cnt);
                }
                else if(time.equals("06AM-07AM"))
                {
                 //   cnt=countcalc("06");
                  //  ++cnt;
                    countcalc("06");
                    // timeref.child("06").setValue(cnt);
                }
                else if(time.equals("07AM-08AM"))
                {
                   // cnt=countcalc("07");
                    //++cnt;
                    //timeref.child("07").setValue(cnt);
                    countcalc("07");
                }
                else if(time.equals("08AM-09AM"))
                {
                 //   cnt=countcalc("08");
                    //++cnt;
                    //timeref.child("08").setValue(cnt);
                    countcalc("08");
                }
                else if(time.equals("09AM-10AM"))
                {
                    countcalc("09");
                    Log.v("TAG","MSGSINFETCH:"+cnt);
                 //   ++cnt;
                    //Firebase tempref=timeref.child("09");
                    //tempref.setValue(cnt);
                    // timeref.child("09").setValue(cnt);
                   // setvaltime();
                }
                else if(time.equals("10AM-02PM"))
                {
                   // cnt=countcalc("14");
                    //++cnt;
                    //timeref.child("14").setValue(cnt);
                    countcalc("10");
                }
                else if(time.equals("02PM-06PM"))
                {
                   // cnt=countcalc("18");
                   // ++cnt;
                   // timeref.child("18").setValue(cnt);
                    countcalc("14");
                }
                else if(time.equals("06PM-08PM"))
                {
                   // cnt=countcalc("20");
                    //++cnt;
                    //timeref.child("20").setValue(cnt);
                    countcalc("18");
                }
                else if(time.equals("08PM-11PM"))
                {
                   //
                    // cnt=countcalc("23");
                    //++cnt;
                    //timeref.child("23").setValue(cnt);
                    countcalc("20");
                }
                else
                Toast.makeText(getApplicationContext(),"invalid",Toast.LENGTH_SHORT).show();

                progressDialog1.dismiss();
                Toast.makeText(getApplicationContext(),"Thanks for your review",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void countcalc(final String freq)
    {

/*
        timeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,Integer>map2;
                map2 = dataSnapshot.getValue(Map.class);

                temp=map2.get(freq);
                temp++;
                Log.v("TAG","MSGSISCNT:"+temp);
                // temp=Integer.parseInt(cnts);
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        return temp;*/
        timeref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String,Integer>map2;
                map2 = dataSnapshot.getValue(Map.class);

                temp=map2.get(freq);
                temp++;
                Log.v("TAG","MSGSISCNT:"+temp);
                // temp=Integer.parseInt(cnts);

                timeref.child(freq).setValue(temp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void setvaltime()
    {
        timeref.child("09").setValue(temp);
    }

}