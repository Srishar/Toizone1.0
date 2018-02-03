package com.example.arvind.detailsui;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentSender;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;

        import android.net.Uri;
        import android.os.Build;
        import android.support.annotation.NonNull;

        import android.support.annotation.RequiresApi;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.AutoCompleteTextView;
        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.directions.route.AbstractRouting;
        import com.directions.route.Route;
        import com.directions.route.RouteException;
        import com.directions.route.Routing;
        import com.directions.route.RoutingListener;
        import com.firebase.client.ChildEventListener;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GoogleApiAvailability;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.LocationSettingsRequest;
        import com.google.android.gms.location.LocationSettingsResult;
        import com.google.android.gms.location.LocationSettingsStatusCodes;
        import com.google.android.gms.location.places.Places;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.app.Fragment;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;
        import java.util.TreeMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,RoutingListener, GoogleApiClient.OnConnectionFailedListener {
    // DECLARATIONS

    //CONSTANTS
    private static final String TAG = "MapsActivity";
    private static final float DEFAULT_ZOOM = 12.0f;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final LatLng DUMMY = new LatLng(14.5, 23.2);
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-49, -168), new LatLng(71, 136));
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    //vars
    private GoogleMap mMap;
    private AutoCompleteTextView input;
    private FusedLocationProviderClient mylocation;
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private ImageView gps;
    private GoogleApiClient googleApiClient;
    private ImageView restroom;
    private ImageView directions;
    private Location currentPosition;
    private LatLng nearestLatLng;
    private Place place;
    private Place pl;
    private String title;
    private ArrayList<Place> lst;
    private Map<Double,Place> map;
    private Double latitude;
    private ArrayList<LatLng> l;
    private Double longitude;
    private Double currentLat;
    private Double currentLon;
    Firebase mref;
    Map<String, String> armap;

    int i = 0;


    private Boolean mLocationPermissionsGranted = false;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //***************************************  PERMISSIONS   *********************************************************************************************//
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        @SuppressWarnings("deprecation") PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    // Storage Permissions
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 20) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
                //testing
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public  boolean isStoragePermissionGrantedRead() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }



//********************************************************** END of Permission *******************************************************//

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_maps);
        displayLocationSettingsRequest(getApplicationContext());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = (ImageView) findViewById(R.id.gps);
        input = (AutoCompleteTextView) findViewById(R.id.input);
        restroom = (ImageView) findViewById(R.id.Restroom);
        directions = (ImageView) findViewById(R.id.showDirections);
        polylines = new ArrayList<>();
// ******************************************** LISTENERS ******************************************************************************************//
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    getSearchLocation();
                }

                return false;
            }
        });


        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double lat = nearestLatLng.latitude;

                    double lng = nearestLatLng.longitude;

                    String format = "geo:0,0?q=" + lat + "," + lng + "( Location title)";
                    Uri uri = Uri.parse(format);


                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Please find a place to show direction", Toast.LENGTH_LONG).show();
                }

            }
        });


        restroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erasePolyLines();
                getcurrentLocation();


                Log.d(TAG, "Current Location Details : \n Lat : " + currentPosition.getLatitude() + "\nLng : " + currentPosition.getLongitude());
                //  Log.d(TAG, placesList.toString() + "\n");
                lst = new ArrayList<>();
                map = new TreeMap<>();
                l = new ArrayList<>();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference raipurRef = rootRef.child("raipur");
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentLat = currentPosition.getLatitude();
                        currentLon = currentPosition.getLongitude();
                        Double theta, dist, lon;
                        for (DataSnapshot ds:dataSnapshot.getChildren()) {
                            Log.v("TAG", "MSGIS:" + ds.getKey());
                            com.google.firebase.database.DataSnapshot ds2=  ds.child("details");
                            String lat1 = (String) ds2.child("lat").getValue();
                             latitude=Double.parseDouble(lat1);
                            String lon1 = (String) ds2.child("lng").getValue();
                             longitude=Double.parseDouble(lon1);
                            String title = (String) ds2.child("name").getValue();
                                int i = 0;
                                Log.d("TAG", latitude + " /n " + title + " /n " + longitude);
                                place = new Place(latitude, longitude, title);
                                lst.add(i, place);
                                // Double lat = currentPosition.getLatitude();
                                lon = currentPosition.getLongitude();
                                theta = lon - longitude;
                                dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(currentLat)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(currentLat)) * Math.cos(deg2rad(theta));
                                dist = Math.acos(dist);
                                dist = rad2deg(dist);
                                dist = dist * 60;
                                dist = dist * 1852;
                                map.put(dist, lst.get(i));
                            Log.v("TAG","MSGTESTMAP:DISTANCE IS:"+dist);

                        }

                        for (Map.Entry<Double, Place> entry : map.entrySet()) {

                            Log.v("TAG","MSGTESTMAP:INSIDEFOR");

                            System.out.println("Distance : " + entry.getKey() + "\tLat: " + entry.getValue().lat + "\tLong : " + entry.getValue().lon + "\tTitle :" + entry.getValue().title);

                        }

                        // nearest PT to our current location
                         if(!map.isEmpty()) {
                             Map.Entry<Double, Place> entry = map.entrySet().iterator().next();
                             System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());

                             nearestLatLng = new LatLng(entry.getValue().lat, entry.getValue().lon);
                             LatLng currentLatLng = new LatLng(currentLat, currentLon);
                             getRouteToNearestMarker(currentLatLng, nearestLatLng);

                             Log.v("TAG","MSGTESTMAP:INSIDE");

                             moveCamera(nearestLatLng, DEFAULT_ZOOM, title);

                         }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                };
                raipurRef.addListenerForSingleValueEvent(eventListener);
            }
        });

    }




    public boolean isServicesOk(){
        Log.d(TAG,"checking google services.....");

        int ServiceAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if(ServiceAvailable == ConnectionResult.SUCCESS){
            Log.d(TAG,"Google services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(ServiceAvailable)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this,ServiceAvailable,ERROR_DIALOG_REQUEST);
            dialog.show();
            Log.d(TAG,"Services unavailable but Error resolvable");
        }
        else
            Toast.makeText(this,"Google Services is not working",Toast.LENGTH_SHORT).show();


        return false;
    }

    private void getRouteToNearestMarker(LatLng start,LatLng end ) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .build();
        routing.execute();
    }

    private Double deg2rad(Double deg) {
        return (deg * Math.PI / 180.0);
    }

    private Double rad2deg(Double rad) {
        return (rad * 180.0 / Math.PI);
    }







    private void sendCurrentLocation(Location location) {
        currentPosition = location;
    }

    public void getMyLocation() {
        mylocation = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task loc = mylocation.getLastLocation();
        loc.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful() && task.getResult() != null) {

                    Location currentLocation = (Location) task.getResult();
                    sendCurrentLocation(currentLocation);
                    moveCameraCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);


                }
            }
        });
    }


    private void moveCameraCurrentLocation(LatLng addr, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addr, zoom));

    }

    public void getSearchLocation() {
        // get the searchstring

        String search = input.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addresses = new ArrayList<>();

        try {
            Log.d(TAG, "Geolocating");
            addresses = geocoder.getFromLocationName(search, 1);
        } catch (IOException e) {
            Log.d(TAG, "Geolocation IOexception " + e.getMessage());
        }


        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getLocality());

            Log.d(TAG, "Geolocating....\n Got a Location " + address);
        }
    }

    public void moveCamera(LatLng addr, float zoom, String Place) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addr, zoom));

        MarkerOptions marker = new MarkerOptions().position(addr).title(Place);
        mMap.addMarker(marker);
    }

    public void getcurrentLocation() {
        mylocation = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task loc = mylocation.getLastLocation();
        loc.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Location currentLocation = (Location) task.getResult();
                    sendCurrentLocation(currentLocation);
                    //moveCameraCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(isServicesOk()){

            mMap = googleMap;
            erasePolyLines();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Intent intent=new Intent(MapsActivity.this,MainActivity.class);
                    intent.putExtra("ptlink",marker.getTitle());
                    startActivity(intent);
                    return false;
                }
            });

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            googleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            autocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, LAT_LNG_BOUNDS, null);
            input.setAdapter(autocompleteAdapter);
            getMyLocation();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference raipurRef = rootRef.child("raipur");
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){
                       // Log.v("TAG", "MSGIS:" + ds.getKey());
                    com.google.firebase.database.DataSnapshot ds2 = ds.child("details");
                    String lat1 = (String) ds2.child("lat").getValue();
                    double lat = Double.parseDouble(lat1);
                    String lon1 = (String) ds2.child("lng").getValue();
                    double lon = Double.parseDouble(lon1);
                    String title = (String) ds2.child("name").getValue();
                  //  Log.v("TAG", "MASTEXT:" + title + lat1 + lon1);
                   // Log.d("TAG", lat + " /n " + title + " /n " + lon);
                    // Add a marker in Sydney and move the camera
                    LatLng Pt = new LatLng(lat, lon);
                    //Log.d(TAG,Integer.toString(placesList.size()));
                    mMap.addMarker(new MarkerOptions().position(Pt).title(title));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Pt));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Pt, 10));
                }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            raipurRef.addListenerForSingleValueEvent(eventListener);
       //    raipurRef.addListenerForSingleValueEvent(childEventListener);

            mref = new Firebase("https://ptlocator.firebaseio.com/raipur/Toilet 1/details");

            /*mref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    Map<String,String>tmap;
                    tmap=dataSnapshot.getValue(Map.class);
                    // for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //  armap=dataSnapshot.getValue(Map.class);
                    String temp=tmap.get("lat");
                    Log.v("TAG","TSETINGTEMP"+temp);
                    double lat = Double.parseDouble(temp);
                    //latitude = (Double) dataSnapshot.child("lat").getValue();
                    String temp1=tmap.get("lng");
                    double lon = Double.parseDouble(temp1);
                    //double lat = (double) dataSnapshot.child("lat").getValue();
                    //double lon = (double) dataSnapshot.child("lng").getValue();
                    String title = (String) dataSnapshot.child("title").getValue();
                    Log.d("TAG", lat + " /n " + title + " /n " + lon);
                    // Add a marker in Sydney and move the camera
                    LatLng Pt = new LatLng(lat, lon);
                    // Log.d(TAG,Integer.toString(placesList.size()));
                    mMap.addMarker(new MarkerOptions().position(Pt).title("Marker in " + title));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Pt));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Pt, DEFAULT_ZOOM));
                    //  }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/



          /* mref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                    Map<String,String>tmap;
                    tmap=dataSnapshot.getValue(Map.class);
                   // for (DataSnapshot ds : dataSnapshot.getChildren()) {
                  //  armap=dataSnapshot.getValue(Map.class);
                    String temp=tmap.get("lat");
                    Log.v("TAG","TSETINGTEMP"+temp);
                    double lat = Double.parseDouble(temp);
                    //latitude = (Double) dataSnapshot.child("lat").getValue();
                    String temp1=tmap.get("lng");
                    double lon = Double.parseDouble(temp1);
                        //double lat = (double) dataSnapshot.child("lat").getValue();
                        //double lon = (double) dataSnapshot.child("lng").getValue();
                        String title = (String) dataSnapshot.child("title").getValue();
                        Log.d("TAG", lat + " /n " + title + " /n " + lon);
                        // Add a marker in Sydney and move the camera
                        LatLng Pt = new LatLng(lat, lon);
                        // Log.d(TAG,Integer.toString(placesList.size()));
                        mMap.addMarker(new MarkerOptions().position(Pt).title("Marker in " + title));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pt));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Pt, DEFAULT_ZOOM));
                  //  }

                }


                @Override
                public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/



        }
    }

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary};
    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+" meters: duration - "+ route.get(i).getDurationValue()+" seconds",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolyLines(){
        for(Polyline p : polylines){
            p.remove();
        }
        polylines.clear();

    }
}


