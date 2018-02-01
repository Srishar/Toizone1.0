package com.example.arvind.detailsui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvind on 28-01-2018.
 */

public class ViewPageAdapter extends PagerAdapter{

    Activity activity;
    //String[] images;
    ArrayList<String>images;
    LayoutInflater inflater;

    public ViewPageAdapter(Activity activity,ArrayList images)
    {
        this.activity=activity;
        this.images=images;
    }

    @Override
    public int getCount() {
        //Log.v("TAG","MSGIS:"+images.length);
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.v("TAG","MSGIS:ISVIEW");
        return view==object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.v("TAG", "MSGIS:INSIDE");
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview = inflater.inflate(R.layout.viewpager_item, container, false);
        ImageView iv;
       // iv = (ImageView) itemview.findViewById(R.id.iv);
        PhotoView photoView = (PhotoView) itemview.findViewById(R.id.picv);
        //photoView.setImageResource(R.drawable.image);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int h = dis.heightPixels;
        int w = dis.widthPixels;
        photoView.setMinimumHeight(h);
        photoView.setMinimumWidth(w);
        Log.v("TAG", "MSGIS:before PICCASSO");
            Picasso.with(activity.getApplicationContext()).load(images.get(position)).into(photoView);
            container.addView(itemview);
            return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}
