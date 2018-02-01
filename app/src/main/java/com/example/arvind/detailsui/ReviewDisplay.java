package com.example.arvind.detailsui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class ReviewDisplay extends AppCompatActivity {

    String link;

    private RecyclerView recyclerView;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);
        link="Toilet 1";
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myref = FirebaseDatabase.getInstance().getReference().child("raipur").child(link).child("/Reviews");

        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                String temp = map.get("name");
                Log.v("TAG", "MSGISinside:" + temp);
                String temp1 = map.get("rev");
                Log.v("TAG", "MSGISinside:" + temp1);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
            Blog.class,
            R.layout.individual_row,
            BlogViewHolder.class,
            myref){

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setRevi(model.getRev());
                viewHolder.setStar(model.getRating());
                viewHolder.setDateval(model.getDate());

            }
        };

        recyclerView.setAdapter(adapter);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        TextView text,revi,date11;
        RatingBar stardisp;
        public BlogViewHolder(View itemview1){
            super(itemview1);
            text=(TextView)itemview1.findViewById(R.id.name11);
            revi=(TextView)itemview1.findViewById(R.id.rev11);
            date11=(TextView)itemview1.findViewById(R.id.date11);
            stardisp=(RatingBar)itemview1.findViewById(R.id.stardisp);
        }
        public void setName(String name)
        {
           text.setText(name+"");
        }
        public void setRevi(String rev)
        {
            revi.setText(rev+"");
        }
        public void setDateval(String dateval)
        {
            date11.setText(dateval+"");
        }
        public void setStar(String rating)
        {
            Float temp=Float.parseFloat(rating);
            stardisp.setRating(temp);
        }
    }



}
