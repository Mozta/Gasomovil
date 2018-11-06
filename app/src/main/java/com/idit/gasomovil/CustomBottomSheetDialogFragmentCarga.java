package com.idit.gasomovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.androidslidr.Slidr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.BluetoothService.BluetoothLeService;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.google.android.gms.internal.zzahn.runOnUiThread;

/**
 * Created by viper on 12/12/2017.
 */

public class CustomBottomSheetDialogFragmentCarga extends BottomSheetDialogFragment {

    private final static String TAG = CustomBottomSheetDialogFragmentCarga.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private ImageButton share_charge;
    int max_tank;
    float lts_actual, tank;
    TextView textCharge_actual, textCharge_faltante, textConnect, textDisconnect;
    Button btn_register_charge,btn_stop_charge;
    Slidr slidr0;

    private double amount;

    DatabaseReference ref, ref_fuel_station, ref_fuel_station_price, ref_fuel_station_service;
    String userID;
    String serviceID;
    double ltsCharged;

    String my_key;
    String name_station, key_station;
    double averageLts;

    Animation slideUp, slideDown;

    private ProgressBar progressBar_loading;
    Integer counter = 1;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Error al conectar");
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d(TAG,"Se conecta a dispositivo");

            boolean result = mBluetoothLeService.connect(mDeviceAddress);
            if (result){
                Log.e(TAG, "Connect request result=" + result);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(true);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(false);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        name_station = getArguments().getString("name_station");
        key_station = getArguments().getString("key_station");
        my_key = getArguments().getString("my_key");
        mDeviceAddress = getArguments().getString(EXTRAS_DEVICE_ADDRESS);

        Intent gattServiceIntent = new Intent(getContext(), BluetoothLeService.class);

        if(getContext().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE)){
            Log.d(TAG,"Se crea servicio");
            Toast.makeText(getContext(), "Se crea servicio", Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG,"Nel perro");
            Toast.makeText(getContext(), "Revisa tu conexión Bluetooth", Toast.LENGTH_SHORT).show();
        }

        averageLts = Double.parseDouble(getArguments().getString("averageLts"));
        //averageLts = Integer.parseInt(getArguments().getString("averageLts"));

        //References to database
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Historial");
        ref_fuel_station = FirebaseDatabase.getInstance().getReference("Stations/"+key_station+"/Comments");
        ref_fuel_station_service = FirebaseDatabase.getInstance().getReference("Stations/"+key_station+"/Services");
        ref_fuel_station_price = FirebaseDatabase.getInstance().getReference("Stations/"+key_station+"/Prices");

        //Save service in stations
        serviceID = ref_fuel_station_service.push().getKey();
        ref_fuel_station_service.child(serviceID).child("liters").setValue(averageLts);
        ref_fuel_station_price.child("magna").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long price_fuel = (Long) dataSnapshot.getValue();
                ref_fuel_station_service.child(serviceID).child("price").setValue(price_fuel * averageLts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref_fuel_station_service.child(serviceID).child("timestamp").setValue(System.currentTimeMillis()/1000);


        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.dialog_modal_pre, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        textCharge_actual = contentView.findViewById(R.id.textCharge_actual);
        textCharge_faltante = contentView.findViewById(R.id.textCharge_faltante);
        textConnect = contentView.findViewById(R.id.connect_bt_txt);
        textDisconnect = contentView.findViewById(R.id.disconnect_bt_txt);
        slidr0 = (Slidr) contentView.findViewById(R.id.slideure);

        max_tank = 45;
        lts_actual = 15;


        slidr0.setMax(max_tank);
        slidr0.addStep(new Slidr.Step("⚠", max_tank/8, Color.parseColor("#f44747"), Color.parseColor("#3cb941")));

        slidr0.setTextMin("E");
        slidr0.setTextMax("F");
        slidr0.setCurrentValue(lts_actual);
        slidr0.setListener(new Slidr.Listener() {
            @Override
            public void valueChanged(Slidr slidr, float currentValue) {
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);


                textCharge_actual.setText(df.format(currentValue) + " L");
                textCharge_faltante.setText(df.format(max_tank - currentValue) + " L");
                if(currentValue == max_tank)
                    Toast.makeText(getContext(), "Tanque lleno", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void bubbleClicked(Slidr slidr) {

            }
        });
        slidr0.setRegionTextFormatter(new Slidr.RegionTextFormatter() {
            @Override
            public String format(int region, float value) {
                switch (region){
                    case 0:
                        lts_actual = value;
                        return String.format("tanque : %d L", (int) value);

                    case 1:
                        return String.format("faltan : %d L", (int) value);

                }

                return null;
            }
        });
        slidr0.setTextFormatter(new Slidr.TextFormatter() {
            @Override
            public String format(float value) {
                return String.format("%d L", (int) value);
            }
        });

        share_charge = (ImageButton)contentView.findViewById(R.id.share_charge);
        share_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GASOMOVIL");
                //shareIntent.putExtra(Intent.EXTRA_TEXT, "Acabo de cargar "+ averageLts+" litros de gasolina en: " + name_station + " y la califique con: "+myRatingBar_qualify_station.getRating()+" estrellas");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });


        btn_register_charge = (Button)contentView.findViewById(R.id.register_charge);
        btn_stop_charge = (Button)contentView.findViewById(R.id.stop_charge);
        progressBar_loading = (ProgressBar) contentView.findViewById(R.id.progressBar_loading);

        btn_register_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Llamar al metodo para inyeccion de codigo al OBD para obtener la cantidad de combustible

                btn_register_charge.startAnimation(slideDown);
                btn_register_charge.setVisibility(View.GONE);

                counter = 1;
                progressBar_loading.setVisibility(View.VISIBLE);
                progressBar_loading.setProgress(0);
                new MyAsyncTask().execute(10);

                btn_stop_charge.startAnimation(slideUp);
                btn_stop_charge.setVisibility(View.VISIBLE);
            }
        });

        btn_stop_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            for (; counter <= params[0]; counter++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(counter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Tarea completa!. =)";
        }
        @Override
        protected void onPostExecute(String result) {
            //progressBar_loading.setVisibility(View.GONE);
            //Toast.makeText(getContext(), String.valueOf(counter), Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            //progressBar_loading.setProgress(values[0]);
            slidr0.setCurrentValue(lts_actual+values[0]);
        }
    }

    private void updateConnectionState(final Boolean conn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (conn){
                    textConnect.setVisibility(View.VISIBLE);
                    textDisconnect.setVisibility(View.GONE);
                }else{
                    textDisconnect.setVisibility(View.VISIBLE);
                    textConnect.setVisibility(View.GONE);
                }
            }
        });
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}