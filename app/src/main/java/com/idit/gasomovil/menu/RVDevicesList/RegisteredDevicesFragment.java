package com.idit.gasomovil.menu.RVDevicesList;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idit.gasomovil.BluetoothRegister.BluetoothModel;
import com.idit.gasomovil.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisteredDevicesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisteredDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisteredDevicesFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<BluetoothModel> data;

    private OnFragmentInteractionListener mListener;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registered_devices, container, false);

        recyclerView = v.findViewById(R.id.rv_registered_device_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<BluetoothModel>();
        for (int i = 0; i < DataDummy.nameArray.length; i++) {
            data.add(new BluetoothModel(
                    DataDummy.idArray[i],
                    DataDummy.macArray[i],
                    DataDummy.nameArray[i]
            ));
        }

        adapter = new RegisteredDevicesListAdapter(data);
        recyclerView.setAdapter(adapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
