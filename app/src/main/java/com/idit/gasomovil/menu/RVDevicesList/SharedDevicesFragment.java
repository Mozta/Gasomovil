package com.idit.gasomovil.menu.RVDevicesList;

import android.content.Context;
import android.net.Uri;
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
 * {@link SharedDevicesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SharedDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SharedDevicesFragment extends Fragment {

    public static final String DEVICE_FIREBASE = "Device";

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<BluetoothModel> data;

    private DatabaseReference ref, devices_bt_ref, user_ref, user_bt_ref;
    private String userID;

    public SharedDevicesFragment() {
        // Required empty public constructor
    }

    public static SharedDevicesFragment newInstance(String param1, String param2) {
        SharedDevicesFragment fragment = new SharedDevicesFragment();
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

        getDevices();
        View v = inflater.inflate(R.layout.fragment_shared_devices, container, false);

        recyclerView = v.findViewById(R.id.rv_share_device_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data= new ArrayList<>();

        adapter = new SharedDevicesListAdapter(data);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getDevices(){
        // Firebase Query
        user_bt_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    final String macaddres = dataSnapshot.getKey();
                    if(Objects.equals(dataSnapshot.getValue(), true)){
                        // Dispositivos permitidos
                        devices_bt_ref.child(macaddres).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BluetoothModel addBT = dataSnapshot.getValue(BluetoothModel.class);
                                if(Objects.equals(dataSnapshot.child("s").child(userID).getValue(), true)){
                                    for (BluetoothModel bt : data)
                                        if (bt.getM().equals(macaddres)) data.remove(bt);
                                    data.add(addBT);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    for (BluetoothModel bt : data)
                                        if (bt.getM().equals(macaddres)) data.remove(bt);
                                    adapter.notifyDataSetChanged();
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
                if (Objects.equals(dataSnapshot.getValue(), false)){
                    String macremove = dataSnapshot.getKey();
                    devices_bt_ref.child(macremove).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BluetoothModel btremove = dataSnapshot.getValue(BluetoothModel.class);
                            if (btremove != null){
                                Log.w("Bluetoothdeshabilitado", btremove.toMap().toString());
                                for (BluetoothModel bt : data)
                                    if (bt.getM().equals(btremove.getM())) data.remove(bt);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    String macremove = dataSnapshot.getKey();
                    devices_bt_ref.child(macremove).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            BluetoothModel btrenew = dataSnapshot.getValue(BluetoothModel.class);
                            if (btrenew != null){
                                for (BluetoothModel bt : data)
                                    if (bt.getM().equals(btrenew.getM())) data.remove(bt);
                                data.add(btrenew);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
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
