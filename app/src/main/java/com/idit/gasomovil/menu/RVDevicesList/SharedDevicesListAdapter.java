package com.idit.gasomovil.menu.RVDevicesList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.BluetoothRegister.BluetoothModel;
import com.idit.gasomovil.R;

import java.util.ArrayList;

public class SharedDevicesListAdapter extends RecyclerView.Adapter<SharedDevicesListAdapter.MyViewHolder> {

    private ArrayList<BluetoothModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMAC;
        TextView textViewDate;
        TextView textViewOwner;
        ImageView imageViewIcon;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.name_device_item_txt);
            this.textViewMAC = itemView.findViewById(R.id.mac_device_item_txt);
            this.textViewDate = itemView.findViewById(R.id.date_device_item_txt);
            this.textViewOwner = itemView.findViewById(R.id.owner_device_item_txt);
            this.imageViewIcon = itemView.findViewById(R.id.imageView_device_item);
        }
    }

    SharedDevicesListAdapter(ArrayList<BluetoothModel> data) {
        this.dataSet = data;
    }

    @Override
    public SharedDevicesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_shared_device_list, parent, false);

        //view.setOnClickListener(MenuDevicesListActivity.myOnClickListener);

        SharedDevicesListAdapter.MyViewHolder myViewHolder = new SharedDevicesListAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SharedDevicesListAdapter.MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewMAC = holder.textViewMAC;
        TextView textViewDate = holder.textViewDate;
        final TextView textViewUsers = holder.textViewOwner;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getN());
        textViewMAC.setText(dataSet.get(listPosition).getM());
        textViewDate.setText(dataSet.get(listPosition).getC());

        DatabaseReference owner = FirebaseDatabase.getInstance().getReference().child("User").child(dataSet.get(listPosition).getO()).child("email");
        owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewUsers.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
