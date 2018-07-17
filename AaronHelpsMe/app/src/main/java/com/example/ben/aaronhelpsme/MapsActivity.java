package com.example.ben.aaronhelpsme;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;
    Button addMarker;
    boolean mark = false;
    String newString, disaster, comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
                disaster = extras.getString("DISASTER");
                comment = extras.getString("COMMENT");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
            disaster= (String) savedInstanceState.getSerializable("DISASTER");


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(latitude, longitude);
        float zoomLevel = 14.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        addMarker = (Button) findViewById(R.id.add);
//        MarkerOptions markerOpt = new MarkerOptions();


        addMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MapsActivity.this, MarkerAdd.class);
                startActivity(in);
            }
        });
        if(newString!=null){

            if(disaster.equals("flood")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable((R.drawable.flood));
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            } else if(disaster.equals("fire")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.fire);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

            } else if(disaster.equals("earthquake")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.earthquake);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

            } else if(disaster.equals("accident")){
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

            }else if(disaster.equals("icestorm")){
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

            }else if(disaster.equals("tornado")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable((R.drawable.tornado));
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

            }else if(disaster.equals("hurricane")){
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }else if(disaster.equals("windstorm")){
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }else if(disaster.equals("volcano")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.lcano);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }else if(disaster.equals("avalanche")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable((R.drawable.avalanche));
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }else if(disaster.equals("tsunami")){
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.tsunami);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                        fromBitmap(smallMarker)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
            }else if(disaster.equals("riot")){
               mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(BitmapDescriptorFactory.
                       defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).snippet(comment));
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
                drawShape();
            }
            System.out.print(disaster);

        }

    }

    public  void drawShape(){
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(latitude+0.01, longitude), new LatLng(latitude, longitude+0.01), new LatLng(latitude,longitude), new LatLng(latitude-0.01, longitude), new LatLng(latitude, longitude-0.01))
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));


    }
}