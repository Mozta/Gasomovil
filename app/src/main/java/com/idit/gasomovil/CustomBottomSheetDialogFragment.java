package com.idit.gasomovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by viper on 12/12/2017.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private TextView textChargue;
    private RatingBar myRatingBar_qualify_station;
    private EditText txtComment;
    private ImageButton share_charge;

    private double amount;

    DatabaseReference ref, ref_fuel_station, ref_fuel_station_price, ref_fuel_station_service;
    String userID;
    String serviceID;
    double ltsCharged;

    String my_key;
    String name_station;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //References to database
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Historial");
        ref_fuel_station = FirebaseDatabase.getInstance().getReference("Stations/123456a/Comments");
        ref_fuel_station_service = FirebaseDatabase.getInstance().getReference("Stations/123456a/Services");
        ref_fuel_station_price = FirebaseDatabase.getInstance().getReference("Stations/123456a/Prices");

        //Save service in stations
        serviceID = ref_fuel_station_service.push().getKey();
        ref_fuel_station_service.child(serviceID).child("liters").setValue(25);
        ref_fuel_station_price.child("magna").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double price_fuel = (double) dataSnapshot.getValue();
                ref_fuel_station_service.child(serviceID).child("price").setValue(price_fuel * 25);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref_fuel_station_service.child(serviceID).child("timestamp").setValue(System.currentTimeMillis()/1000);
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

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.dialog_modal, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        share_charge = (ImageButton)contentView.findViewById(R.id.share_charge);
        share_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GASOMOVIL");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Acabo de cargar gasolina en: " + name_station + " y la califique con: "+myRatingBar_qualify_station.getRating()+" estrellas");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });

        name_station = getArguments().getString("name_station");
        my_key = getArguments().getString("my_key");

        textChargue = contentView.findViewById(R.id.textCharge);
        textChargue.setText("Litros suministrados: 35");

        myRatingBar_qualify_station = contentView.findViewById(R.id.myRatingBar_qualify_station);
        txtComment = contentView.findViewById(R.id.write_comment);

        Button btnSaveCharge = (Button)contentView.findViewById(R.id.save_comment);
        btnSaveCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save historial user
                ref.child(my_key).child("name").setValue(name_station);
                ref.child(my_key).child("liters").setValue(25);

                ref_fuel_station_price.child("magna").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        amount = (double) dataSnapshot.getValue();

                        ref.child(my_key).child("price").setValue(amount * 25);
                        ref.child(my_key).child("score").setValue(myRatingBar_qualify_station.getRating());
                        ref.child(my_key).child("timestamp").setValue(System.currentTimeMillis()/1000);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Save comment in stations
                ref_fuel_station.child(userID).child("comment").setValue(txtComment.getText().toString());
                ref_fuel_station.child(userID).child("score").setValue(myRatingBar_qualify_station.getRating());
                ref_fuel_station.child(userID).child("timestamp").setValue(System.currentTimeMillis()/1000);

                dismiss();
            }
        });

    }
}