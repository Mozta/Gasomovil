package com.idit.gasomovil.menu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.idit.gasomovil.R;

//import com.idit.gasomovil.MainActivity.sendFuelTankCommands;

public class MenuSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button scan = (Button) findViewById(R.id.scan_bluetooth);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCMDPointer = -1;
                //sendFuelTankCommands();
            }
        });

        Button reset = (Button) findViewById(R.id.reset_bluetooth);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

}
