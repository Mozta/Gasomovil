package com.idit.gasomovil.BluetoothRegister;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.BluetoothService.BluetoothLeService;
import com.idit.gasomovil.MainActivity;
import com.idit.gasomovil.R;

import java.util.HashMap;
import java.util.Map;

public class BluetoothFinishActivity extends Activity {

    private final static String TAG = BluetoothFinishActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_ID = "DEVICE_ID";


    private String mDeviceName;
    private String mDeviceAddress;
    private String mDeviceId;
    private BluetoothLeService mBluetoothLeService;

    private String userID;
    public static final String DEVICE_FIREBASE = "Device";

    DatabaseReference device_ref, ref;

    private boolean mConnected = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Error al conectar");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d(TAG,"Se conecta a dispositivo");

            mBluetoothLeService.connect(mDeviceAddress);
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
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_finish);

        // ========================== Start Firebase ==========================
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid(); // uid de usuario

        ref = FirebaseDatabase.getInstance().getReference();
        device_ref = ref.child("User").child(userID).child(DEVICE_FIREBASE); // ref a device

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        mDeviceId = intent.getStringExtra(EXTRAS_DEVICE_ID);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);

        if(bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE)){
            Log.d(TAG,"Se crea servicio");
            Toast.makeText(this, "Se crea servicio", Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG,"Nel perro");
            Toast.makeText(this, "Revisa tu conexión Bluetooth", Toast.LENGTH_SHORT).show();
        }

        Button finishButton = findViewById(R.id.finish_bt_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference bt_ref = device_ref.child(mDeviceAddress);
                final DatabaseReference device_bt_ref = ref.child("Device").child(mDeviceAddress);
                ValueEventListener e = new ValueEventListener() {
                    BluetoothModel newBT = new BluetoothModel(mDeviceId,mDeviceAddress,mDeviceName,userID);
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            try{
                                Map<String,Boolean> aux = new HashMap<>();
                                aux.put("zGmMNfI7Qxa4MqIjJPXBC9fzonj2", true);
                                aux.put("ESKjkuvJBxQsKf8ux4MoiCtCAIq1", true);
                                newBT.set_share(aux);
                                bt_ref.setValue(true);
                                device_bt_ref.setValue(newBT.toMap());
                            }catch (NullPointerException e){
                                Log.d(TAG, "Se ha borrado un dispositivo");
                            }
                            newBT=null;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                bt_ref.addValueEventListener(e);

                final Intent main = new Intent(BluetoothFinishActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mBluetoothLeService, resourceId, Toast.LENGTH_SHORT).show();
                // mConnectionState.setText(resourceId);
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
