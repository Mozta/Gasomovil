package com.idit.gasomovil.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.idit.gasomovil.BluetoothRegister.BluetoothBannerActivity;
import com.idit.gasomovil.BluetoothRegister.BluetoothModel;
import com.idit.gasomovil.R;
import com.idit.gasomovil.menu.RVDevicesList.DataDummy;
import com.idit.gasomovil.menu.RVDevicesList.DevicesListAdapter;

import java.util.ArrayList;

public class MenuDevicesListActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<BluetoothModel> data;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_registered_devices:
                    return true;
                case R.id.navigation_shared_devices:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_devices_list);

        Toolbar toolbar = findViewById(R.id.toolbar_devices_list);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        BottomNavigationView navigation = findViewById(R.id.navigation_list);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = findViewById(R.id.rv_device_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
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

        adapter = new DevicesListAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_device_menu:
                Intent tutorial = new Intent(MenuDevicesListActivity.this, BluetoothBannerActivity.class);
                startActivity(tutorial);
                return true;
            default:
                onSupportNavigateUp();
                return true;
        }
    }

        @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

}
