package com.idit.gasomovil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by viper on 12/12/2017.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private TextView textChargue;

    DatabaseReference ref;
    String userID;
    double ltsCharged;

    String my_key;
    String name_station;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Historial");
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

        name_station = getArguments().getString("name_station");
        my_key = getArguments().getString("my_key");

        textChargue = contentView.findViewById(R.id.textCharge);
        textChargue.setText("Litros despachados: 35");

        Button btnSaveCharge = (Button)contentView.findViewById(R.id.save_comment);
        btnSaveCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child(my_key).child("name").setValue(name_station);
                ref.child(my_key).child("liters").setValue(25);
                ref.child(my_key).child("price").setValue(827);
                ref.child(my_key).child("score").setValue(5);
                ref.child(my_key).child("timestamp").setValue(1512668702);
            }
        });

    }
}