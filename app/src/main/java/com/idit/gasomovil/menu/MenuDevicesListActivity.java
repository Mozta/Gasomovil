package com.idit.gasomovil.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.idit.gasomovil.BluetoothRegister.BluetoothBannerActivity;
import com.idit.gasomovil.R;
import com.idit.gasomovil.menu.RVDevicesList.RegisteredDevicesFragment;
import com.idit.gasomovil.menu.RVDevicesList.SharedDevicesFragment;

public class MenuDevicesListActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_registered_devices:
                    fragment = new RegisteredDevicesFragment();
                    break;
                case R.id.navigation_shared_devices:
                    fragment = new SharedDevicesFragment();
                    break;
            }

            return loadFragment(fragment);
        }

    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_devices_list);

        Toolbar toolbar = findViewById(R.id.toolbar_devices_list);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //loading the default fragment
        loadFragment(new RegisteredDevicesFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation_list);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
