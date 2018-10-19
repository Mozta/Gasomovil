package com.idit.gasomovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.androidslidr.Slidr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

/**
 * Created by viper on 12/12/2017.
 */

public class CustomBottomSheetDialogFragmentCarga extends BottomSheetDialogFragment {
    private ImageButton share_charge;
    int max_tank;
    float lts_actual, tank;
    TextView textCharge_actual, textCharge_faltante;
    Slidr slidr0;

    private double amount;

    DatabaseReference ref, ref_fuel_station, ref_fuel_station_price, ref_fuel_station_service;
    String userID;
    String serviceID;
    double ltsCharged;

    String my_key;
    String name_station, key_station;
    double averageLts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        name_station = getArguments().getString("name_station");
        key_station = getArguments().getString("key_station");
        my_key = getArguments().getString("my_key");
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
                double price_fuel = (double) dataSnapshot.getValue();
                ref_fuel_station_service.child(serviceID).child("price").setValue(price_fuel * averageLts);
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




        Button btnSaveCharge = (Button)contentView.findViewById(R.id.save_comment);
        btnSaveCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float i = lts_actual;

                while (i < max_tank){
                    i = (float) (i+0.00001);
                }

                //dismiss();
            }
        });

    }
}