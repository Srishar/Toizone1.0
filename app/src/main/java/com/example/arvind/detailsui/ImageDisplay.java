package com.example.arvind.detailsui;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageDisplay extends AppCompatActivity {

    String link;

    private Firebase mref1;
    Map<String, String> map;
    ProgressDialog progressDialog;

    ViewPager viewPager;
    ViewPageAdapter adapter;
    //private String[] images={"https://firebasestorage.googleapis.com/v0/b/ptlocator.appspot.com/o/userpics%2FToilet%201%2Fpt1.jpg?alt=media&token=d2db94e0-2d30-4f5c-bece-406e84a24a65",
        //   "https://firebasestorage.googleapis.com/v0/b/ptlocator.appspot.com/o/userpics%2FToilet%201%2Ffile5710?alt=media&token=b43e6b10-8ee3-4085-9c0d-1ce1314de0a9"};
   //private String[] images=new String[3];
    private ArrayList<String>images=new ArrayList<String>(1);

    // private List<String> img;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
         link=getIntent().getExtras().getString("ptlink");
        progressDialog = new ProgressDialog(ImageDisplay.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        mref1 = new Firebase("https://ptlocator.firebaseio.com/raipur/"+link+"/Reviews");

        mref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                map = dataSnapshot.getValue(Map.class);
                String val = map.get("image");
                //images.add(map.get("image"));
                // Log.v("TAG","MSGIS:"+val);
                images.add(val);
               // images[i] = val;
                Log.v("TAG", "MSGIS:" +images.get(i));
                i++;

                proceed();
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
        });
    }

       void proceed() {
           viewPager = (ViewPager) findViewById(R.id.vp);
           adapter = new ViewPageAdapter(ImageDisplay.this, images);
           viewPager.setAdapter(adapter);
            progressDialog.dismiss();
       }

}
