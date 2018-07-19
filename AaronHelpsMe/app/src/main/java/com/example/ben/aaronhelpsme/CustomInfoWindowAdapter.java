package com.example.ben.aaronhelpsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;


    public CustomInfoWindowAdapter (Context context){

        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);

    }


    private void rendowWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvtitle= (TextView)view.findViewById(R.id.title);


        if(!(title.equals(""))){
            tvtitle.setText(title);
        }

        String body = marker.getSnippet();
        TextView tvbody= (TextView)view.findViewById(R.id.body);
        if(!(title.equals(""))){
            tvbody.setText(body);
        }



    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

}
