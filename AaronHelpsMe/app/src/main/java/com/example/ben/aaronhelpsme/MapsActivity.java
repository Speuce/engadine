package com.example.ben.aaronhelpsme;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

//delete later
import java.util.Random;

import java.util.*;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    Bitmap bitmap;
    private Map<String, BitmapDescriptor> descriptors;
    private List<Flag> fires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(getApplicationContext());

        fires = new ArrayList<Flag>();
        ConnectionManager.updateActivity(this);
        descriptors = new HashMap<String, BitmapDescriptor>();
        this.makeDescriptors();


//
//        gpsTracker = new GPSTracker(getApplicationContext());
//        mLocation = gpsTracker.getLocation();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
                disaster = extras.getString("DISASTER");
                comment = extras.getString("COMMENT");
                bitmap = (Bitmap) extras.getParcelable("IMAGE");

            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
            disaster= (String) savedInstanceState.getSerializable("DISASTER");
        }
        CodeStuff.getCon().loadFlags(false);


    }
    public void makeDescriptors(){
        loadDescriptor("fire");
        loadDescriptor("flood");
        loadDescriptor("earthquake");
        loadDescriptor("tornado");
        loadDescriptor("hurricane");
        loadDescriptor("avalanche");
        loadDescriptor("accident");
        loadDescriptor("windstorm");
        loadDescriptor("riot");
        loadDescriptor("icestorm");
        loadDescriptor("volcano");
        loadDescriptor("tsunami");

    }
    private void loadDescriptor(String s){
        this.descriptors.put(s, getDescriptor(s));
    }
    public BitmapDescriptor getIcon(String s){
        if(this.descriptors.containsKey(s)){
            return descriptors.get(s);
        }else{
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }
    }

    private BitmapDescriptor getDescriptor(String s){
        switch (s){
            case "fire":
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.fire);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84, 84, false);
                return BitmapDescriptorFactory.fromBitmap(smallMarker);
            case "flood":
                BitmapDrawable bitmapdraw1 = (BitmapDrawable)getResources().getDrawable((R.drawable.flood));
                Bitmap b1 = bitmapdraw1.getBitmap();
                Bitmap smallMarker1 = Bitmap.createScaledBitmap(b1,84, 84, false);
                return BitmapDescriptorFactory.fromBitmap(smallMarker1);
            case "earthquake":
                BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.earthquake);
                Bitmap b2 = bitmapdraw2.getBitmap();
                Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2,84, 84, false);
                return BitmapDescriptorFactory.
                        fromBitmap(smallMarker2);
            case "accident":
                return BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            case "icestorm":
                BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
            case "hurricane":
                return BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
            case "tornado":
                BitmapDrawable bitmapdraw3 = (BitmapDrawable)getResources().getDrawable((R.drawable.tornado));
                Bitmap b3 = bitmapdraw3.getBitmap();
                Bitmap smallMarker3 = Bitmap.createScaledBitmap(b3,384, 84, false);
                return BitmapDescriptorFactory.
                        fromBitmap(smallMarker3);
            case "windstorm":
                return BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
            case "volcano":
                BitmapDrawable bitmapdraw4 = (BitmapDrawable)getResources().getDrawable(R.drawable.lcano);
                Bitmap b4 = bitmapdraw4.getBitmap();
                Bitmap smallMarker4 = Bitmap.createScaledBitmap(b4,84, 84, false);
                return BitmapDescriptorFactory.
                        fromBitmap(smallMarker4);
            case "avalanche":
                BitmapDrawable bitmapdraw5 = (BitmapDrawable)getResources().getDrawable((R.drawable.avalanche));
                Bitmap b5= bitmapdraw5.getBitmap();
                Bitmap smallMarker5 = Bitmap.createScaledBitmap(b5,84, 84, false);
                return BitmapDescriptorFactory.
                        fromBitmap(smallMarker5);
            case "tsunami":
                BitmapDrawable bitmapdraw6 = (BitmapDrawable)getResources().getDrawable(R.drawable.tsunami);
                Bitmap b6 = bitmapdraw6.getBitmap();
                Bitmap smallMarker6 = Bitmap.createScaledBitmap(b6,84, 84, false);
                return BitmapDescriptorFactory.
                        fromBitmap(smallMarker6);
            case "riot":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            default:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }


    }

    public void addMarker(Flag f){
        final LatLng sydney = new LatLng(f.getLat(), f.getLon());
        String disaster = f.getDisaster();
        if(disaster.equals("fire")){
            fires.add(f);
        }
        f.setMarker(mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(getIcon(disaster))
                .snippet(f.getComment())));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       // Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
       // latitude = currentLocation.getLatitude();
       // longitude = currentLocation.getLongitude();
        //latitude = mLocation.getLatitude();
        //longitude = mLocation.getLongitude();

        this.mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub
                latitude = arg0.getLatitude();
                longitude = arg0.getLongitude();
            }
        });

        CodeStuff.getCon().getFlags();

        final LatLng sydney = new LatLng(latitude, longitude);
        System.out.println("Lat: " + latitude + " long: " + longitude);
        ArrayList <Point> flood = new ArrayList<>();

        // Add a marker in Sydney and move the camera
        float zoomLevel = 14.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        addMarker = (Button) findViewById(R.id.add);
//        MarkerOptions markerOpt = new MarkerOptions();


        addMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MapsActivity.this, MarkerAdd.class);
                in.putExtra("lat", latitude);
                in.putExtra("lon", longitude);
                startActivity(in);
            }
        });
       // mMap.addMarker(new MarkerOptions().position(sydney).title(disaster).icon(getIcon(disaster))
       //         .snippet(comment));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent in = new Intent(MapsActivity.this, ShowPhoto.class);
                //in.putExtra("IMAGE", bitmap);
                in.putExtra("ID", CodeStuff.getCon().getFlag(marker).getId());
                startActivity(in);
            }
        });
        ArrayList<Point> points = new ArrayList<Point>();
        for(Flag f: fires){
            points.add(new Point(f.getLon(), f.getLat()));
        }
        List<Point> hull = calculateConcaveHull(points, 3);

        PolygonOptions ops = new PolygonOptions();
        int x = 0;
        for(Point p: hull){
            x++;
           ops.add(new LatLng(p.getY(), p.getX()));
        }
        if(x >= 3){
            Polygon polygon = mMap.addPolygon(ops.strokeColor(Color.BLACK).fillColor(Color
                    .argb(20, 200, 0, 0)));
        }


        //int count = 30;

//        points.add(new Point(sydney.longitude,sydney.latitude));

        // just a test with random points
//        Random rand = new Random();
//        for (int i =1; i<count; i++){
//            flood.add(new Point((double)Math.random()*2+latitude,(double)Math.random()*2+longitude));
//        }
//
//        ArrayList<Point> temp = calculateConcaveHull(flood,3);
//
//        ArrayList<LatLng> temp2 = new ArrayList<LatLng>();
//
//        for (int i =0; i<temp.size();i++){
//            temp2.add(new LatLng(temp.get(i).getX(),temp.get(i).getY()));
//        }
//
//        PolygonOptions opts=new PolygonOptions();
//
//        for (LatLng location : temp2) {
//            opts.add(location);
//        }
//
//        drawShape(temp2);
//        for (int i =0; i<flood.size(); i++) {
////            mMap.addMarker(new MarkerOptions().position(new LatLng(points.get(i).getX(),points.get(i).getY())).title(disaster));
//        System.out.println("Running");
//        }

    }

    public  void drawShape(ArrayList<LatLng> t){
        PolygonOptions opts=new PolygonOptions();

        for (LatLng location : t) {
            opts.add(location);
        }

        Polygon polygon = mMap.addPolygon(opts.strokeColor(Color.RED).fillColor(Color.BLUE));


    }




    public static class Point {

        private final Double x;
        private final Double y;

        public Point(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public String toString() {
            return "(" + x + " " + y + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                if (x.equals(((Point) obj).getX()) && y.equals(((Point) obj).getY())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            // http://stackoverflow.com/questions/22826326/good-hashcode-function-for-2d-coordinates
            // http://www.cs.upc.edu/~alvarez/calculabilitat/enumerabilitat.pdf
            int tmp = (int) (y + ((x + 1) / 2));
            return Math.abs((int) (x + (tmp * tmp)));
        }
    }

    private static Double euclideanDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    private static ArrayList<Point> kNearestNeighbors(ArrayList<Point> l, Point q, Integer k) {
        ArrayList<Pair<Double, Point>> nearestList = new ArrayList<>();
        for (Point o : l) {
            nearestList.add(new Pair<>(euclideanDistance(q, o), o));
        }

        Collections.sort(nearestList, new Comparator<Pair<Double, Point>>() {
            @Override
            public int compare(Pair<Double, Point> o1, Pair<Double, Point> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        ArrayList<Point> result = new ArrayList<>();

        for (int i = 0; i < Math.min(k, nearestList.size()); i++) {
            result.add(nearestList.get(i).getValue());
        }

        return result;
    }

    private static Point findMinYPoint(ArrayList<Point> l) {
        Collections.sort(l, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.getY().compareTo(o2.getY());
            }
        });
        return l.get(0);
    }

    private static Double calculateAngle(Point o1, Point o2) {
        return Math.atan2(o2.getY() - o1.getY(), o2.getX() - o1.getX());
    }

    private static Double angleDifference(Double a1, Double a2) {
        // calculate angle difference in clockwise directions as radians
        if ((a1 > 0 && a2 >= 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if ((a1 >= 0 && a2 > 0) && a1 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if ((a1 < 0 && a2 <= 0) && a1 < a2) {
            return 2 * Math.PI + a1 + Math.abs(a2);
        } else if ((a1 <= 0 && a2 < 0) && a1 > a2) {
            return Math.abs(a1 - a2);
        } else if (a1 <= 0 && 0 < a2) {
            return 2 * Math.PI + a1 - a2;
        } else if (a1 >= 0 && 0 >= a2) {
            return a1 + Math.abs(a2);
        } else {
            return 0.0;
        }
    }

    private static ArrayList<Point> sortByAngle(ArrayList<Point> l, final Point q, final Double a) {
        // Sort by angle descending
        Collections.sort(l, new Comparator<Point>() {
            @Override
            public int compare(final Point o1, final Point o2) {
                Double a1 = angleDifference(a, calculateAngle(q, o1));
                Double a2 = angleDifference(a, calculateAngle(q, o2));
                return a2.compareTo(a1);
            }
        });
        return l;
    }

    private static Boolean intersect(Point l1p1, Point l1p2, Point l2p1, Point l2p2) {
        // calculate part equations for line-line intersection
        Double a1 = l1p2.getY() - l1p1.getY();
        Double b1 = l1p1.getX() - l1p2.getX();
        Double c1 = a1 * l1p1.getX() + b1 * l1p1.getY();
        Double a2 = l2p2.getY() - l2p1.getY();
        Double b2 = l2p1.getX() - l2p2.getX();
        Double c2 = a2 * l2p1.getX() + b2 * l2p1.getY();
        // calculate the divisor
        Double tmp = (a1 * b2 - a2 * b1);

        // calculate intersection point x coordinate
        Double pX = (c1 * b2 - c2 * b1) / tmp;

        // check if intersection x coordinate lies in line line segment
        if ((pX > l1p1.getX() && pX > l1p2.getX()) || (pX > l2p1.getX() && pX > l2p2.getX())
                || (pX < l1p1.getX() && pX < l1p2.getX()) || (pX < l2p1.getX() && pX < l2p2.getX())) {
            return false;
        }

        // calculate intersection point y coordinate
        Double pY = (a1 * c2 - a2 * c1) / tmp;

        // check if intersection y coordinate lies in line line segment
        if ((pY > l1p1.getY() && pY > l1p2.getY()) || (pY > l2p1.getY() && pY > l2p2.getY())
                || (pY < l1p1.getY() && pY < l1p2.getY()) || (pY < l2p1.getY() && pY < l2p2.getY())) {
            return false;
        }

        return true;
    }

    private static boolean pointInPolygon(Point p, ArrayList<Point> pp) {
        boolean result = false;
        for (int i = 0, j = pp.size() - 1; i < pp.size(); j = i++) {
            if ((pp.get(i).getY() > p.getY()) != (pp.get(j).getY() > p.getY()) &&
                    (p.getX() < (pp.get(j).getX() - pp.get(i).getX()) * (p.getY() - pp.get(i).getY()) / (pp.get(j).getY() - pp.get(i).getY()) + pp.get(i).getX())) {
                result = !result;
            }
        }
        return result;
    }



    public static ArrayList<Point> calculateConcaveHull(ArrayList<Point> pointArrayList, Integer k) {

        // the resulting concave hull
        ArrayList<Point> concaveHull = new ArrayList<>();

        // optional remove duplicates
        HashSet<Point> set = new HashSet<>(pointArrayList);
        ArrayList<Point> pointArraySet = new ArrayList<>(set);

        // k has to be greater than 3 to execute the algorithm
        Integer kk = Math.max(k, 3);

        // return Points if already Concave Hull
        if (pointArraySet.size() < 3) {
            return pointArraySet;
        }

        // make sure that k neighbors can be found
        kk = Math.min(kk, pointArraySet.size() - 1);

        // find first point and remove from point list
        Point firstPoint = findMinYPoint(pointArraySet);
        concaveHull.add(firstPoint);
        Point currentPoint = firstPoint;
        pointArraySet.remove(firstPoint);

        Double previousAngle = 0.0;
        Integer step = 2;

        while ((currentPoint != firstPoint || step == 2) && pointArraySet.size() > 0) {

            // after 3 steps add first point to dataset, otherwise hull cannot be closed
            if (step == 5) {
                pointArraySet.add(firstPoint);
            }

            // get k nearest neighbors of current point
            ArrayList<Point> kNearestPoints = kNearestNeighbors(pointArraySet, currentPoint, kk);

            // sort points by angle clockwise
            ArrayList<Point> clockwisePoints = sortByAngle(kNearestPoints, currentPoint, previousAngle);

            // check if clockwise angle nearest neighbors are candidates for concave hull
            Boolean its = true;
            int i = -1;
            while (its && i < clockwisePoints.size() - 1) {
                i++;

                int lastPoint = 0;
                if (clockwisePoints.get(i) == firstPoint) {
                    lastPoint = 1;
                }

                // check if possible new concave hull point intersects with others
                int j = 2;
                its = false;
                while (!its && j < concaveHull.size() - lastPoint) {
                    its = intersect(concaveHull.get(step - 2), clockwisePoints.get(i), concaveHull.get(step - 2 - j), concaveHull.get(step - 1 - j));
                    j++;
                }
            }

            // if there is no candidate increase k - try again
            if (its) {
                return calculateConcaveHull(pointArrayList, k + 1);
            }

            // add candidate to concave hull and remove from dataset
            currentPoint = clockwisePoints.get(i);
            concaveHull.add(currentPoint);
            pointArraySet.remove(currentPoint);

            // calculate last angle of the concave hull line
            previousAngle = calculateAngle(concaveHull.get(step - 1), concaveHull.get(step - 2));

            step++;

        }

        // Check if all points are contained in the concave hull
        Boolean insideCheck = true;
        int i = pointArraySet.size() - 1;

        while (insideCheck && i > 0) {
            insideCheck = pointInPolygon(pointArraySet.get(i), concaveHull);
            i--;
        }

        // if not all points inside -  try again
        if (!insideCheck) {
            return calculateConcaveHull(pointArrayList, k + 1);
        } else {
            return concaveHull;
        }

    }
}