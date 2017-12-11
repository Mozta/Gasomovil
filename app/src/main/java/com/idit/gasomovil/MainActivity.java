package com.idit.gasomovil;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idit.gasomovil.BottomSheet.GoogleMapsBottomSheetBehavior;
import com.idit.gasomovil.menu.MenuDiagnosisActivity;
import com.idit.gasomovil.menu.MenuFavouriteActivity;
import com.idit.gasomovil.menu.MenuHelpActivity;
import com.idit.gasomovil.menu.MenuHistoryActivity;
import com.idit.gasomovil.menu.MenuPerfilActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static final String TAG = "";
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    View mapView;

    //Play services location
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 300193;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; //5 secs
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference ref, ref_fuel_station;
    GeoFire geoFire;

    String userID;

    Marker mCurrent;

    private TextView textUsername;
    private FloatingActionButton fab, fab2;

    //BottomInfo
    TextView imgExpandable;
    BottomInfoFragment mBottomInfo;

    private GoogleMapsBottomSheetBehavior behavior;
    private View parallax;

    private List<StationModel> result;

    private TextView name_station, premium_station, magna_station, diesel_station, address_station, phone_station;
    private RatingBar ranking_station;

    private String keyStationSelected;
    private Float latitudeSelected, longitudeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference("User").child(userID);

        result = new ArrayList<>();

        ref_fuel_station = FirebaseDatabase.getInstance().getReference("Stations");

        System.out.println(ref_fuel_station);

        geoFire = new GeoFire(ref);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Llenando tanque de combustible...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View view = navigationView.getHeaderView(0);
        textUsername = (TextView) view.findViewById(R.id.nav_username);
        textUsername.setText(user.getEmail());

        mapView = mapFragment.getView();

        //Init view
        /*imgExpandable = (TextView) findViewById(R.id.imgExpandable);
        mBottomInfo = BottomInfoFragment.newInstance("Info bottom sheet");
        imgExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomInfo.show(getSupportFragmentManager(),mBottomInfo.getTag());
            }
        });*/
        setUpLocation();


        final View bottomsheet = findViewById(R.id.bottomsheet);
        //View bsheet = findViewById(R.id.bottomsheet);
        behavior = GoogleMapsBottomSheetBehavior.from(bottomsheet);
        parallax = findViewById(R.id.parallax);
        behavior.setParallax(parallax);
        behavior.anchorView(fab);
        behavior.anchorView(fab2);

        // wait for the bottomsheet to be laid out
        bottomsheet.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // set the height of the parallax to fill the gap between the anchor and the top of the screen
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(parallax.getMeasuredWidth(), behavior.getAnchorOffset() / 1);
                parallax.setLayoutParams(layoutParams);
                bottomsheet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        behavior.setBottomSheetCallback(new GoogleMapsBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @GoogleMapsBottomSheetBehavior.State int newState) {
                // each time the bottomsheet changes position, animate the camera to keep the pin in view
                // normally this would be a little more complex (getting the pin location and such),
                // but for the purpose of an example this is enough to show how to stay centered on a pin
                //map.animateCamera(CameraUpdateFactory.newLatLng(SYDNEY));
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_fuel_go:
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+longitudeSelected+","+ latitudeSelected);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    Toast.makeText(MainActivity.this, "Go", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_fuel_fav:
                    ref.child("Favorites").child(keyStationSelected).setValue(keyStationSelected);
                    Toast.makeText(MainActivity.this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    private int getItemIndex(StationModel stationModel){
        int index = -1;

        for (int i=0; i < result.size(); i++){
            if (result.get(i).key.equals(stationModel.key)) {
                index = i;
                break;
            }
        }
        return index;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void setUpLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else{
            if(checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();

            //Update to firebase
            geoFire.setLocation("Ubicacion", new GeoLocation(latitude, longitude),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            //Add marker
                            if(mCurrent != null)
                                mCurrent.remove(); //remove old marker
                            //mCurrent = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Tu"));
                            enableMyLocation();

                            //Move Camera to this position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18.0f));
                        }
                    });



            Log.d("GASOMOVIL", String.format("Tu ubicacion ha cambiado: %f / %f",latitude,longitude));
        }
        else
            Log.d("GASOMOVIL", "No se puede obtener tu ubicacion");
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMyLocationEnabled(true);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(this, "Este dispositivo no es soportado", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_account) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent tutorial = new Intent(MainActivity.this, MenuPerfilActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_history) {
            Intent tutorial = new Intent(MainActivity.this, MenuHistoryActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_favorite) {
            Intent tutorial = new Intent(MainActivity.this, MenuFavouriteActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_diagnostic) {
            Intent tutorial = new Intent(MainActivity.this, MenuDiagnosisActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_help) {
            Intent tutorial = new Intent(MainActivity.this, MenuHelpActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_off) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, BannerActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23, -102),4));
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition ));

        // Read from the database
        ref_fuel_station.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StationModel model = dataSnapshot.getValue(StationModel.class);
                model.setKey(dataSnapshot.getKey());

                result.add(model);
                result.size();

                //checkIfEmpty();
                //Create fuel Station area
                LatLng fuel_station_area = new LatLng(model.longitude,model.latitude);
                mMap.addMarker(new MarkerOptions()
                        .position(fuel_station_area)
                        .icon(vectorToBitmap(rankingIcon(model.score)))
                        .title(model.name));

                mMap.addCircle(new CircleOptions()
                        .center(fuel_station_area)
                        .radius(15) //in metters => 15m
                        .strokeColor(rankingColor(model.score))
                        .fillColor(0x220000FF)
                        .strokeWidth(5.0f));

                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(fuel_station_area.latitude, fuel_station_area.longitude), 0.015f);
                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        sendNotification("GASOMOVIL",String.format("Has entrado a una estación de carga"));
                        fab.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onKeyExited(String key) {
                        sendNotification("GASOMOVIL",String.format("¿Cómo ha estado el servicio de la estación?",key));
                        fab.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {
                        Log.d("MOVIMIENTO", String.format("%s moviendose dentro de la estacion de carga [%f/%f]",key,location.latitude,location.longitude));

                    }

                    @Override
                    public void onGeoQueryReady() {

                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                        Log.e("ERROR",""+error);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StationModel model = dataSnapshot.getValue(StationModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.set(index, model);
                //adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                StationModel model = dataSnapshot.getValue(StationModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.remove(index);
                //adapter.notifyItemRemoved(index);

                //checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Floating button for center position map
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLastLocation != null){
                    final double latitude = mLastLocation.getLatitude();
                    final double longitude = mLastLocation.getLongitude();

                    LatLng target = new LatLng(latitude, longitude);
                    CameraPosition position = mMap.getCameraPosition();

                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(18);
                    builder.target(target);

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                }
            }
        });

        //mMap.setTrafficEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                behavior.setState(GoogleMapsBottomSheetBehavior.STATE_COLLAPSED);
                behavior.setHideable(false);

                View bsheet = findViewById(R.id.bottomsheet);

                name_station = bsheet.findViewById(R.id.bottom_sheet_title);
                premium_station = bsheet.findViewById(R.id.premium_price_station);
                magna_station = bsheet.findViewById(R.id.magna_price_station);
                diesel_station = bsheet.findViewById(R.id.diesel_price_station);
                ranking_station = bsheet.findViewById(R.id.myRatingBar_station);
                address_station = bsheet.findViewById(R.id.address_fuel_station);
                phone_station = bsheet.findViewById(R.id.phone_fuel_station);

                int i = Integer.parseInt(marker.getId().substring(1));

                name_station.setText(result.get(i).name);
                premium_station.setText(result.get(i).prices.get("premium").toString());
                magna_station.setText(result.get(i).prices.get("magna").toString());
                diesel_station.setText(result.get(i).prices.get("diesel").toString());
                ranking_station.setRating(result.get(i).score);
                address_station.setText(result.get(i).address);
                phone_station.setText(result.get(i).phone);

                behavior.setAnchorColor(rankingColor(result.get(i).score));

                //Guardamos el id de la gasolinera seleccionada por si el usuario la agrega a favoritos
                keyStationSelected = result.get(i).key;
                latitudeSelected = result.get(i).latitude;
                longitudeSelected = result.get(i).longitude;

                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation2);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                behavior.setHideable(true);
                behavior.setState(GoogleMapsBottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private int rankingColor(Float score) {
        if (score < 2)
            return Color.parseColor("#FF6C46");
        else if (score < 4)
            return Color.parseColor("#FBAF25");
        else
            return Color.parseColor("#99CC33");
    }

    private @DrawableRes int rankingIcon(Float score){
        if (score < 2)
            return R.drawable.ic_gasolinera_rojo;
        else if (score < 4)
            return R.drawable.ic_gasolinera_amarillo;
        else
            return R.drawable.ic_gasolinera_verde;
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void sendNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content);

        NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(new Random().nextInt(), notification);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
