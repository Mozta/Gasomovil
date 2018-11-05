package com.idit.gasomovil.menu.RVDevicesList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idit.gasomovil.BluetoothRegister.BluetoothModel;
import com.idit.gasomovil.R;

import java.util.ArrayList;

public class RegisteredDevicesListAdapter extends RecyclerView.Adapter<RegisteredDevicesListAdapter.MyViewHolder> {

    private ArrayList<BluetoothModel> dataSet;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMAC;
        TextView textViewDate;
        TextView textViewUsers;
        ImageView imageViewIcon;

        MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.name_device_item_txt);
            this.textViewMAC = itemView.findViewById(R.id.mac_device_item_txt);
            this.textViewDate = itemView.findViewById(R.id.date_device_item_txt);
            this.textViewUsers = itemView.findViewById(R.id.share_device_item_txt);
            this.imageViewIcon = itemView.findViewById(R.id.imageView_device_item);
        }
    }

    RegisteredDevicesListAdapter(ArrayList<BluetoothModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_registered_device_list, parent, false);

        //view.setOnClickListener(MenuDevicesListActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewMAC = holder.textViewMAC;
        TextView textViewDate = holder.textViewDate;
        TextView textViewUsers = holder.textViewUsers;
        ImageView imageView = holder.imageViewIcon;


        textViewName.setText(dataSet.get(listPosition).getN());
        textViewMAC.setText(dataSet.get(listPosition).getM());
        textViewDate.setText(dataSet.get(listPosition).getC());
        textViewUsers.setText(Integer.toString(dataSet.get(listPosition).getS().size()));
        //imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
