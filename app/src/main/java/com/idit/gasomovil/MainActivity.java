package com.idit.gasomovil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.BluetoothRegister.BluetoothBannerActivity;
import com.idit.gasomovil.BottomSheet.GoogleMapsBottomSheetBehavior;
import com.idit.gasomovil.Model.Item;
import com.idit.gasomovil.Utility.MyLog;
import com.idit.gasomovil.menu.MenuDevicesListActivity;
import com.idit.gasomovil.menu.MenuDiagnosisActivity;
import com.idit.gasomovil.menu.MenuFavouriteActivity;
import com.idit.gasomovil.menu.MenuHelpActivity;
import com.idit.gasomovil.menu.MenuHistoryActivity;
import com.idit.gasomovil.menu.MenuPerfilActivity;
import com.idit.gasomovil.menu.MenuSettingsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Esta clase es la principal de la aplicación, en ella se encuentran todos los metodos necesarios
 * para el correcto funcionamiento de la misma, la cual incluye: la validación del inicio de sesión,
 * las consultas pertinentes a la base de datos y la integración en tiempo real a la nube, las
 * configuraciones necesarias para establecer la comunicación Bluetooth, los metodos que utiliza el
 * GPS para su funcionamiento asi como la integración con Geofire para poder enlazarlo con Firebase
 * y todos las demas instancias para dibujar la interfaz y la comunicación con las demás pantallas
 * de la aplicación.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public StationModel model;
    public boolean tankBeforeCharge;
    private double ltsAntesdeCarga = 0;
    private int iPromedio = 0;
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    View mapView;

    //Play services location
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 300193;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 1000; //5 secs
    private static int FASTEST_INTERVAL = 100;
    private static int DISPLACEMENT = 1;

    DatabaseReference ref, ref_fuel_station, device_ref;
    DatabaseReference mDatabase_comments;
    GeoFire geoFire;

    String userID;

    Marker mCurrent;

    private TextView textUsername;
    private ImageView imageView;
    private FloatingActionButton fab, fab2;
    private String number_panic = "2441216481";

    //BottomInfo
    TextView imgExpandable;
    BottomInfoFragment mBottomInfo;

    private GoogleMapsBottomSheetBehavior behavior;
    private ImageView parallax;

    private List<StationModel> result;

    private TextView name_station, premium_station, magna_station, diesel_station, address_station, phone_station;
    private RatingBar ranking_station;

    private String keyStationSelected;
    private Float latitudeSelected, longitudeSelected;

    private Menu menu;

    public static boolean isLaterCharge = false;
    public static boolean recargue = false;

    private double ltsLaterCharge;
    //private double[] ltsAfterCharge;
    ArrayList<Double> ltsAfterCharge = new ArrayList<Double>();
    private double averageLts = 0;

    private RecyclerView recyclerView;
    private List<CommentModel> resultComment;
    private CommentAdapter adapterComment;
    private TextView emptyText;


    /* ========== ELM327 ============= */
    private static final String TAG_DIALOG = "dialog";


    // ========================== Bluetooth ==========================
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    // Commands
    private static final String[] INIT_COMMANDS = {"AT SP 0", "012F"};
    private static int mCMDPointer = -1;

    // Intent request codes
    //private static final int REQUEST_ENABLE_BT = 101;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 102;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 103;

    // Message types accessed from the BluetoothIOGateway Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names accesses from the BluetoothIOGateway Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast_message";

    // Bluetooth
    private List<BluetoothDevice> mDeviceList;

    // Widgets
    private TextView mConnectionStatus;
    private TextView mMonitor;
    private EditText mCommandPrompt;
    private ImageButton mBtnSend;
    private Button mCargar;

    // Variable def
    private static boolean inSimulatorMode = false;
    private static StringBuilder mSbCmdResp;
    private static StringBuilder mPartialResponse;
    private String mConnectedDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        ref = FirebaseDatabase.getInstance().getReference("User").child(userID);
        ref.keepSynced(true);

        result = new ArrayList<>();

        ref_fuel_station = FirebaseDatabase.getInstance().getReference("Stations");

        device_ref = ref.child("Device");
        device_ref.keepSynced(true);

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



        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        device_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Intent tutorial = new Intent(MainActivity.this, BluetoothBannerActivity.class);
                    startActivity(tutorial);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        device_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    toolbar.setBackgroundColor(Color.RED);
                    toolbar.setTitle("Sin Dispositivos");
                }else {
                    // Toolbar :: Transparent
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    toolbar.setTitle("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // ========================== Start Bluetooth ==========================
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // ========================== End Bluetooth ==========================

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

                // TODO iniciar bluetooth, conectar con HM10, recibir información de "name_station", "key_station", "my_key", "averageLts", ltsAntesdeCarga, ltsCargados

                if (!mBluetoothAdapter.isEnabled()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }else{
                    Bundle args = new Bundle();
                    // Colocamos el String
                    args.putString("name_station", model.name);
                    args.putString("key_station", model.getKey());
                    //args.putString("my_key", my_key);
                    Double ltsCargados = averageLts-ltsAntesdeCarga;
                    Log.e("mio", "Litros antes: "+String.valueOf(ltsAntesdeCarga));
                    Log.e("mio", "Litros despues: "+String.valueOf(averageLts));
                    Log.e("mio", "En total: "+String.valueOf(ltsCargados));
                    args.putString("averageLts", String.valueOf(ltsCargados));

                    //Initializing a bottom sheet
                    BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragmentCarga();

                    bottomSheetDialogFragment.setArguments(args);
                    //show it
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                    bottomSheetDialogFragment.setCancelable(true);
                }

                Snackbar.make(view, "[Aqui va la conexión a bluetooth] Llenando tanque de combustible...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // TODO se deja visible para pruebas de Bluetooth
        //fab.setVisibility(View.INVISIBLE);

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
        imageView = (ImageView)view.findViewById(R.id.imageView);

        mapView = mapFragment.getView();

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

    // Listener de la botonera (ir/favoritos) al seleccionar alguna gasolinera
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
                    Toast.makeText(MainActivity.this, "Trazando ruta...", Toast.LENGTH_SHORT).show();
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
    protected void onStart(){
        super.onStart();
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Register EventBus
        EventBus.getDefault().register(this);

        // Load the image using Glide
        Glide.with(imageView.getContext())
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.ic_account_circle_header)
                .error(R.drawable.ic_account_circle_header)
                .into(imageView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Unregister EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Comprobamos si el Bluetooth esta activo y cambiamos el texto del
        updateMenuTitles();

        switch (item.getItemId())
        {
            case R.id.action_on_bluetooth:
                displayLog("Bluetooth esta disponible");
                return true;

            case R.id.action_off_bluetooth:
                return true;

            case R.id.action_scan:

                return true;

            case R.id.action_send_cmd:
                mCMDPointer = -1;
                sendDefaultCommands();
                return true;

            case R.id.menu_clr_scr:
                mSbCmdResp.setLength(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.action_on_bluetooth);
        if(true){
            bedMenuItem.setTitle("Habilitar Bluetooth");
        } else {
            bedMenuItem.setTitle("Deshabilitar Bluetooth");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Si no me quieres usar para que me instalas", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    private void showChooserDialog(DialogFragment dialogFragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null)
        {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, "dialog");
    }


    private void cancelScanning(){

    }

    /**
     * Callback method for once a new device detected.
     *
     * @param device BluetoothDevice
     */
    public void onEvent(BluetoothDevice device)
    {
        if (mDeviceList == null)
        {
            mDeviceList = new ArrayList<>(10);
        }

        mDeviceList.add(device);

        /*// create dialog
        final Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (fragment != null && fragment instanceof PairedDevicesDialog)
        {
            PairedListAdapter adapter = dialog.getAdapter();
            adapter.notifyDataSetChanged();
        }
        else
        {
            dialog = new PairedDevicesDialog();
            dialog.setAdapter(new PairedListAdapter(this, new HashSet<>(mDeviceList)), true);
            showChooserDialog(dialog);
        }*/
    }

    /*private static void displayMessage(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }*/

    private void displayLog(String msg)
    {
        Log.d(TAG, msg);
    }

    private void sendOBD2CMD(String sendMsg)
    {
        /*if (mIOGateway.getState() != BluetoothIOGateway.STATE_CONNECTED)
        {
            //Toast.makeText(context, "El dispositivo Bluetooth no está disponible", Toast.LENGTH_SHORT).show();
            //displayMessage(MainActivity.getAppContext(), "El dispositivo Bluetooth no está disponible");
            Toast.makeText(MainActivity.this, "El dispositivo Bluetooth no está disponible", Toast.LENGTH_SHORT).show();
            return;
        }*/

        String strCMD = sendMsg;
        strCMD += '\r';

        byte[] byteCMD = strCMD.getBytes();
        //mIOGateway.write(byteCMD);
    }

    private void sendDefaultCommands()
    {
        if(inSimulatorMode)
        {
            Toast.makeText(MainActivity.this, "¡Estás en modo simulador!", Toast.LENGTH_SHORT).show();
            //displayMessage(MainActivity.getAppContext(),"¡Estás en modo simulador!");
            return;
        }

        if (mCMDPointer >= INIT_COMMANDS.length)
        {
            mCMDPointer = -1;
            return;
        }

        // reset pointer
        if (mCMDPointer < 0)
        {
            mCMDPointer = 0;
        }

        sendOBD2CMD(INIT_COMMANDS[mCMDPointer]);
    }

    public void sendFuelTankCommands()
    {

        if (mCMDPointer >= INIT_COMMANDS.length)
        {
            mCMDPointer = -1;
            return;
        }

        // reset pointer
        if (mCMDPointer < 0)
        {
            mCMDPointer = 0;
        }

        while(ltsAntesdeCarga == 0){
            sendOBD2CMD("012F");
        }
        Toast.makeText(this, "Lo tengo: "+ltsAntesdeCarga, Toast.LENGTH_SHORT).show();

        if(isLaterCharge){
            for (int i=0; i<10; i++){
                sendOBD2CMD("012F");
                //averageLts += ltsAfterCharge.get(i);
            }
            averageLts = averageLts/iPromedio;
        }

    }

    private void parseResponse(String buffer)
    {
        switch (mCMDPointer)
        {
            case 0: // CMD: AT SP 0, no parse needed
                mSbCmdResp.append("R>>");
                mSbCmdResp.append(buffer);
                mSbCmdResp.append("\n");
                break;

            case 1: // CMD: 012F fuel tank
                double ft = showLitersFuelTank(buffer);
                if (ft > 1) {
                    mSbCmdResp.append("R>>");
                    mSbCmdResp.append(buffer);
                    mSbCmdResp.append(" (Tanque con: ");
                    mSbCmdResp.append(String.format("%.2f", ft));
                    mSbCmdResp.append("Lts)");
                    mSbCmdResp.append("\n");

                    if(MainActivity.recargue){
                        ltsAntesdeCarga = decimalFormat(ft);
                        Toast.makeText(MainActivity.this, "Tengo: "+ltsAntesdeCarga+" Lts", Toast.LENGTH_SHORT).show();
                        //MainActivity.recargue = false;
                        Log.e("mio","entre a recargue"+ ltsAntesdeCarga);
                    }

                    if(MainActivity.isLaterCharge){
                        //averageLts = decimalFormat(ft);
                        averageLts = (double) (Math.random()*30)+1;
                        //iPromedio += 1;
                        Toast.makeText(MainActivity.this, ltsAntesdeCarga+" / "+averageLts, Toast.LENGTH_SHORT).show();
                        Log.e("mio","ENTRE a despues de carga"+ averageLts);

                        ref.child("average_lts").setValue(averageLts-ltsAntesdeCarga);

                        String my_key = ref.push().getKey();

                        // Creamos un nuevo Bundle
                        Bundle args = new Bundle();
                        // Colocamos el String
                        args.putString("name_station", model.name);
                        args.putString("key_station", model.getKey());
                        args.putString("my_key", my_key);
                        Double ltsCargados = averageLts-ltsAntesdeCarga;
                        Log.e("mio", "Litros antes: "+String.valueOf(ltsAntesdeCarga));
                        Log.e("mio", "Litros despues: "+String.valueOf(averageLts));
                        Log.e("mio", "En total: "+String.valueOf(ltsCargados));
                        args.putString("averageLts", String.valueOf(ltsCargados));



                        //Initializing a bottom sheet
                        BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();

                        bottomSheetDialogFragment.setArguments(args);
                        //show it
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                        bottomSheetDialogFragment.setCancelable(false);

                        //Finalize asi que debo reiniciar los datos
                        MainActivity.recargue = false;
                        MainActivity.isLaterCharge = false;
                        averageLts = 0;
                    }
                }
                else{
                    mSbCmdResp.append("Calculando...");
                    mSbCmdResp.append("\n");
                }

                //ltsAntesdeCarga = decimalFormat(ft);

                break;

            default:
                mSbCmdResp.append("R>>");
                mSbCmdResp.append(buffer);
                mSbCmdResp.append("\n");
        }

        //mMonitor.setText(mSbCmdResp.toString());

        if (mCMDPointer >= 0)
        {
            mCMDPointer++;
            sendDefaultCommands();
        }
    }

    private double decimalFormat(double x){
        DecimalFormat df = new DecimalFormat(".##");
        return Double.parseDouble(df.format(x));
    }

    private String cleanResponse(String text)
    {
        text = text.trim();
        text = text.replace("\t", "");
        text = text.replace(" ", "");
        text = text.replace(">", "");

        return text;
    }

    private int showEngineCoolantTemperature(String buffer)
    {
        String buf = buffer;
        buf = cleanResponse(buf);

        if (buf.contains("4105"))
        {
            try
            {
                buf = buf.substring(buf.indexOf("4105"));

                String temp = buf.substring(4, 6);
                int A = Integer.valueOf(temp, 16);
                A -= 40;

                return A;
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                MyLog.e(TAG, e.getMessage());
            }
        }

        return -1;
    }

    private int showEngineRPM(String buffer)
    {
        String buf = buffer;
        buf = cleanResponse(buf);

        if (buf.contains("410C"))
        {
            try
            {
                buf = buf.substring(buf.indexOf("410C"));

                String MSB = buf.substring(4, 6);
                String LSB = buf.substring(6, 8);
                int A = Integer.valueOf(MSB, 16);
                int B = Integer.valueOf(LSB, 16);

                return  ((A * 256) + B) / 4;
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                MyLog.e(TAG, e.getMessage());
            }
        }

        return -1;
    }

    private int showVehicleSpeed(String buffer)
    {
        String buf = buffer;
        buf = cleanResponse(buf);

        if (buf.contains("410D"))
        {
            try
            {
                buf = buf.substring(buf.indexOf("410D"));

                String temp = buf.substring(4, 6);

                return Integer.valueOf(temp, 16);
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                MyLog.e(TAG, e.getMessage());
            }
        }

        return -1;
    }

    private int showDistanceTraveled(String buffer)
    {
        String buf = buffer;
        buf = cleanResponse(buf);

        if (buf.contains("4131"))
        {
            try
            {
                buf = buf.substring(buf.indexOf("4131"));

                String MSB = buf.substring(4, 6);
                String LSB = buf.substring(6, 8);
                int A = Integer.valueOf(MSB, 16);
                int B = Integer.valueOf(LSB, 16);

                return (A * 256) + B;
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                MyLog.e(TAG, e.getMessage());
            }
        }

        return -1;
    }

    private double showLitersFuelTank(String buffer)
    {
        String buf = buffer;
        buf = cleanResponse(buf);

        if (buf.contains("412F"))
        {
            try
            {
                buf = buf.substring(buf.indexOf("412F"));

                String temp = buf.substring(4, 6);
                double A = Integer.valueOf(temp, 16);
                A = ((A/2.55)/100)*44;

                return A;
            }
            catch (IndexOutOfBoundsException | NumberFormatException e)
            {
                MyLog.e(TAG, e.getMessage());
            }
        }

        return -1;
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
        } else if (id == R.id.device_list) {
            Intent tutorial = new Intent(MainActivity.this, MenuDevicesListActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_panic) {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 225);
            } else {
                Log.i("Mensaje", "Se tiene permiso!");
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number_panic));
                startActivity(intent);
            }


        } else if (id == R.id.nav_help) {
            Intent tutorial = new Intent(MainActivity.this, MenuHelpActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_settings) {
            Intent tutorial = new Intent(MainActivity.this, MenuSettingsActivity.class);
            startActivity(tutorial);
        } else if (id == R.id.nav_off) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, BannerActivity.class));
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
                model = dataSnapshot.getValue(StationModel.class);
                model.setKey(dataSnapshot.getKey());

                result.add(model);
                result.size();

                //checkIfEmpty();
                //Create fuel Station area
                LatLng fuel_station_area = new LatLng(model.latitude, model.longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(fuel_station_area)
                        .icon(vectorToBitmap(rankingIcon(model.score)))
                        .title(model.name));

                mMap.addCircle(new CircleOptions()
                        .center(fuel_station_area)
                        .radius(20) //in metters => 15m
                        .strokeColor(rankingColor(model.score))
                        .strokeWidth(5.0f));

                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(fuel_station_area.latitude, fuel_station_area.longitude), 0.020f);
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
                        //ref.child("average_lts").setValue(ltsLaterCharge);


                        //Log.e("mio","entre a despues de carga2"+ averageLts);

                        //if (mBluetoothAdapter.isEnabled()) {
                            if (MainActivity.recargue){
                                Log.e("mio","sali a despues de carga2"+ averageLts);
                                MainActivity.recargue = false;
                                MainActivity.isLaterCharge = true;
                                mCMDPointer = -1;
                                sendDefaultCommands();
                                Toast.makeText(MainActivity.this, ltsAntesdeCarga+" / "+averageLts, Toast.LENGTH_SHORT).show();
                                ref.child("average_lts").setValue(averageLts-ltsAntesdeCarga);
                            //
                            //Finalize asi que debo reiniciar los datos
                            //MainActivity.recargue = false;
                            //MainActivity.isLaterCharge = false;
                            //averageLts = 0;
                        }


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
                //result.notifyItemChanged(index);

                //Create fuel Station area
                LatLng fuel_station_area = new LatLng(model.latitude, model.longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(fuel_station_area)
                        .icon(vectorToBitmap(rankingIcon(model.score)))
                        .title(model.name));

                mMap.addCircle(new CircleOptions()
                        .center(fuel_station_area)
                        .radius(20) //in metters => 15m
                        .strokeColor(rankingColor(model.score))
                        //.fillColor(0x220000FF)
                        .strokeWidth(5.0f));
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
                premium_station.setText(result.get(i).Prices.get("premium").toString());
                magna_station.setText(result.get(i).Prices.get("magna").toString());
                diesel_station.setText(result.get(i).Prices.get("diesel").toString());
                ranking_station.setRating(result.get(i).score);
                address_station.setText(result.get(i).address);
                phone_station.setText(result.get(i).phone);

                //Comments
                ref_fuel_station.child(result.get(i).key).child("Probando").setValue("esneta");

                emptyText = findViewById(R.id.text_no_comment);
                resultComment = new ArrayList<>();

                recyclerView = findViewById(R.id.comment_list);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(linearLayoutManager);

                adapterComment = new CommentAdapter(resultComment);
                recyclerView.setAdapter(adapterComment);



                behavior.setAnchorColor(rankingColor(result.get(i).score));



                //Guardamos el id de la gasolinera seleccionada por si el usuario la agrega a favoritos
                keyStationSelected = result.get(i).key;
                latitudeSelected = result.get(i).latitude;
                longitudeSelected = result.get(i).longitude;

                updateList();

                checkIfEmpty();

                Toast.makeText(MainActivity.this, keyStationSelected, Toast.LENGTH_SHORT).show();

                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation2);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                //Image of fuel station
                // Load the image using Glide
                Glide.with(parallax.getContext())
                        .load(result.get(i).image_url)
                        .placeholder(R.drawable.fuel_station_1)
                        .error(R.drawable.fuel_station_1)
                        .into(parallax);

//                behavior.setParallax(parallax);
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

    private void updateList(){
        ref_fuel_station.child(keyStationSelected).child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CommentModel model = dataSnapshot.getValue(CommentModel.class);
                model.setKey(dataSnapshot.getKey());

                resultComment.add(model);
                adapterComment.notifyDataSetChanged();

                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CommentModel model = dataSnapshot.getValue(CommentModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndexComment(model);
                resultComment.set(index, model);
                adapterComment.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CommentModel model = dataSnapshot.getValue(CommentModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndexComment(model);
                resultComment.remove(index);
                adapterComment.notifyItemRemoved(index);

                checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndexComment(CommentModel model) {
        int index = -1;

        for (int i=0; i < resultComment.size(); i++){
            if (resultComment.get(i).key.equals(model.key)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void checkIfEmpty(){
        if (resultComment.size() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
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
                .setSmallIcon(R.drawable.ic_stat_name)
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
