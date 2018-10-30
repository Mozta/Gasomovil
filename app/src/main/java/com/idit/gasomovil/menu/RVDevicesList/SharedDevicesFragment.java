package com.idit.gasomovil.menu.RVDevicesList;

import android.content.Context;
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
 * {@link SharedDevicesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SharedDevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SharedDevicesFragment extends Fragment {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<BluetoothModel> data;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shared_devices, container, false);

        recyclerView = v.findViewById(R.id.rv_share_device_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<BluetoothModel>();
        for (int i = 0; i < DataDummy.nameArray.length-1; i++) {
            data.add(new BluetoothModel(
                    DataDummy.idArray[i],
                    DataDummy.macArray[i],
                    DataDummy.nameArray[i]
            ));
        }

        adapter = new SharedDevicesListAdapter(data);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
