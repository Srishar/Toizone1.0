package com.example.arvind.detailsui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.q42.android.scrollingimageview.ScrollingImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity {

    private static int GALLERY_INTENT = 2;

    String link;

    TextView ptname;
    TextView phno;
    TextView facildisp;
    TextView ohdisp;
    TextView pricedisp;
    TextView addisp;
    TextView ratdisp;
    TextView ratedisp,ratendisp;
    CircleButton went;
    String deviceid,phone,gone,spl;
    ImageView coverpic;
    BarChart barChart;
    CheckedTextView splfeatures;

    private Firebase mref, mref1,mref2;
    private StorageReference mstore;
    Map<String, String> map;
    Map<String, String> map1;
    Map<String, Integer> map2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

         link=getIntent().getExtras().getString("ptlink");

        ptname = (TextView) findViewById(R.id.ptname);
        phno = (TextView) findViewById(R.id.phno);
        facildisp = (TextView) findViewById(R.id.facildisp);
        ohdisp = (TextView) findViewById(R.id.ohdisp);
        pricedisp = (TextView) findViewById(R.id.pricedisp);
        addisp = (TextView) findViewById(R.id.addisp);
        ratdisp = (TextView) findViewById(R.id.ratdisp);
        ratendisp = (TextView) findViewById(R.id.ratendisp);
        went = (CircleButton) findViewById(R.id.went);
        coverpic = (ImageView) findViewById(R.id.coverpic);
        barChart=(BarChart)findViewById(R.id.graph);
        splfeatures=(CheckedTextView)findViewById(R.id.splfeatures);

        deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        mref = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/details");
        mref1 = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/Reviews");
        mref2 = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/peak");
        mstore = FirebaseStorage.getInstance().getReference();


        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map = dataSnapshot.getValue(Map.class);
                String imgu = map.get("img");
                Picasso.with(MainActivity.this).load(imgu).into(coverpic);
                String facil = map.get("facil");
                facildisp.setText(facil + "\n");
                String oh = map.get("opening");
                ohdisp.setText(oh);
                String name = map.get("name");
                ptname.setText(name);
                phone = map.get("phno");
                phno.setText(phone);
                String pri = map.get("price");
                pricedisp.setText(pri);
                String spl = map.get("add");
                splfeatures.setText(spl);
                String add = map.get("address");
                addisp.setText(add);
                String rat = map.get("rating");
                double ratt = Double.parseDouble(rat);
                 gone = map.get("went");

                if (ratt < 2.0) {
                    ratdisp.setBackgroundColor(Color.parseColor("#F01515"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(rat);
                } else if (ratt > 2.0 && ratt < 4.0) {
                    ratdisp.setBackgroundColor(Color.parseColor("#FFF700"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(rat);
                } else {
                    ratdisp.setBackgroundColor(Color.parseColor("#12F312"));
                    ratdisp.setTextColor(Color.parseColor("#FFFFFF"));
                    ratdisp.setText(rat);
                }
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        went.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=Integer.parseInt(gone);
                temp++;
                String temp1=temp+"";
                mref.child("went").setValue(temp1);
                Toast.makeText(getApplicationContext(),"Thanks, your response in noted",Toast.LENGTH_LONG).show();
            }
        });

        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map2 = dataSnapshot.getValue(Map.class);
                int num=map2.get("00");
                int num1=map2.get("04");
                int num2=map2.get("06");
                int num3=map2.get("07");
                int num4=map2.get("08");
                int num5=map2.get("09");
                int num6=map2.get("10");
                int num7=map2.get("14");
                int num8=map2.get("18");
                int num9=map2.get("20");
                int num10=map2.get("23");


                final ArrayList<BarEntry>barEntries=new ArrayList<>();
                barEntries.add(new BarEntry(num,0));
                barEntries.add(new BarEntry(num1,1));
                barEntries.add(new BarEntry(num2,2));
                barEntries.add(new BarEntry(num3,3));
                barEntries.add(new BarEntry(num4,4));
                barEntries.add(new BarEntry(num5,5));
                barEntries.add(new BarEntry(num6,6));
                barEntries.add(new BarEntry(num7,7));
                barEntries.add(new BarEntry(num8,8));
                barEntries.add(new BarEntry(num9,9));
                barEntries.add(new BarEntry(num10,10));
                BarDataSet barDataSet=new BarDataSet(barEntries,"Crowd");

                ArrayList<String>dates=new ArrayList<>();
                dates.add("12AM-04AM");
                dates.add("04AM-06AM");
                dates.add("06AM");
                dates.add("07AM");
                dates.add("08AM");
                dates.add("09AM");
                dates.add("10AM");
                dates.add("10AM-02PM");
                dates.add("02PM-06PM");
                dates.add("06PM-08PM");
                dates.add("08PM-11PM");
                BarData barData=new BarData(dates,barDataSet);
                barChart.setData(barData);
                barChart.isShown();
                barChart.invalidate();
                barChart.setTouchEnabled(true);


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        /*
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                map = dataSnapshot.getValue(Map.class);
                String facil = map.get("facil")+"\n";
                facildisp.setText(facil);
                String oh = map.get("opening");
                ohdisp.setText(oh);
                String name=map.get("name");
                ptname.setText(name);
                String phone=map.get("phno");
                phno.setText(phone);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

    }


    public void sendcomplain(View view) {
        Intent intent = new Intent(MainActivity.this, Complain.class);
        startActivity(intent);
    }
    public void seerev(View view) {
        Intent intent = new Intent(MainActivity.this, ReviewDisplay.class);
        startActivity(intent);
    }

    public void addReview(View view) {
        Intent intent = new Intent(MainActivity.this, Review.class);
        intent.putExtra("ptlink",link);
        startActivity(intent);
    }
    public void more(View view) {
        Intent intent = new Intent(MainActivity.this, ImageDisplay.class);
        intent.putExtra("ptlink",link);
        startActivity(intent);

    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }


    public void addPhoto(View view){
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
           Uri uri=data.getData();
            Random r=new Random();
            int num=0;
            for(int i=0;i<10000;i++)
            {
                num=r.nextInt(10000);
            }
            StorageReference filepath=mstore.child("userpics").child("Toilet 1").child("file"+num);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String duri =taskSnapshot.getDownloadUrl().toString();

                    mref1=new Firebase("https://ptlocator.firebaseio.com/raipur/Toilet 1/Reviews/"+deviceid);
                    Firebase child=mref1.child("image");
                    child.setValue(duri);

                    Toast.makeText(getApplication(),"completed",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }


    }

}
