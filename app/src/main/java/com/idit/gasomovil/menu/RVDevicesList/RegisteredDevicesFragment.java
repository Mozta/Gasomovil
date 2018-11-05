package com.idit.gasomovil.menu.RVDevicesList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.BluetoothRegister.BluetoothModel;
import com.idit.gasomovil.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RegisteredDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisteredDevicesFragment extends Fragment {

    public static final String DEVICE_FIREBASE = "Device";

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<BluetoothModel> data;

    private DatabaseReference ref, devices_bt_ref, user_ref, user_bt_ref;
    private String userID;

    public RegisteredDevicesFragment() {
        // Required empty public constructor
    }

    public static RegisteredDevicesFragment newInstance(String param1, String param2) {
        RegisteredDevicesFragment fragment = new RegisteredDevicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference();

        user_ref = ref.child("User").child(userID);
        user_bt_ref = user_ref.child(DEVICE_FIREBASE);

        devices_bt_ref = ref.child(DEVICE_FIREBASE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addDevices();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registered_devices, container, false);

        recyclerView = v.findViewById(R.id.rv_registered_device_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<BluetoothModel>();

        adapter = new RegisteredDevicesListAdapter(data);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void addDevices(){
        // Firebase Query
        user_bt_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Log.w("Dispositivos","Se agrega dispositivo");
                    final String macaddres = dataSnapshot.getKey();
                    if(Objects.equals(dataSnapshot.getValue(), true)){
                        // Dispositivos permitidos
                        devices_bt_ref.child(macaddres).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(Objects.equals(dataSnapshot.child("o").getValue(), userID)){
                                    BluetoothModel addBT = dataSnapshot.getValue(BluetoothModel.class);
                                    for (BluetoothModel bt : data)
                                        if (bt.getM().equals(macaddres)) data.remove(bt);
                                    data.add(addBT);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    Log.d("MAC","es prestado");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        // Dispositivos no permitidos
                        //  TODO Analizar que hacer con los no permitidos
                    }
                }else{
                    Log.w("Dispositivos", "no tiene");
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.w("Dispositivos", "Se modifica dispositivo");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.w("Dispositivos", "Se elimina dispositivo");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
