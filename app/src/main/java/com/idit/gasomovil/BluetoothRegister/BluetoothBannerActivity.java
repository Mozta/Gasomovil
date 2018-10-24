package com.idit.gasomovil.BluetoothRegister;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.idit.gasomovil.R;
import com.idit.mylibrary.Step;
import com.idit.mylibrary.StepPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BluetoothBannerActivity extends AppCompatActivity {

    private List<Step> steps;
    private StepPagerAdapter adapter;

    private ViewPager pager;
    private LinearLayout indicatorLayout;
    private FrameLayout containerLayout;
    private RelativeLayout buttonContainer;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private int currentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(com.idit.mylibrary.R.style.TutorialStyle);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth_banner);
        steps = new ArrayList<>();
        initViews();
        initAdapter();


        addFragment(new Step.Builder().setTitle("Activa Bluetooth")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner1)
                .build());
        addFragment(new Step.Builder().setTitle("Busca tu dispositivo")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner4)
                .build());
        addFragment(new Step.Builder().setTitle("Selecciona para registrar")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner2)
                .build());
        addFragment(new Step.Builder().setTitle("Conecta automáticamente al iniciar")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner3)
                .build());


        Button searchBT = findViewById(R.id.register_bluetooth_button);

        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }else{
                    Intent searchActivity = new Intent(BluetoothBannerActivity.this, BluetoothListActivity.class);
                    startActivity(searchActivity);
                }
            }
        });

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAdapter() {
        adapter = new StepPagerAdapter(getSupportFragmentManager(), steps);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                controlPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void controlPosition(int position) {
        notifyIndicator();

        containerLayout.setBackgroundColor(steps.get(position).getBackgroundColor());
        buttonContainer.setBackgroundColor(steps.get(position).getBackgroundColor());
    }

    private void initViews() {
        currentItem = 0;

        pager = findViewById(com.idit.mylibrary.R.id.viewPager);
        indicatorLayout = findViewById(com.idit.mylibrary.R.id.indicatorLayout);
        containerLayout = findViewById(com.idit.mylibrary.R.id.containerLayout);
        buttonContainer = findViewById(com.idit.mylibrary.R.id.buttonContainer);
    }

    public void addFragment(Step step) {
        steps.add(step);
        adapter.notifyDataSetChanged();
        notifyIndicator();
        controlPosition(currentItem);
    }

    public void notifyIndicator() {
        if (indicatorLayout.getChildCount() > 0)
            indicatorLayout.removeAllViews();

        for (int i = 0; i < steps.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setPadding(8, 8, 8, 8);
            int drawable = com.idit.mylibrary.R.drawable.circle_black;
            if (i == currentItem)
                drawable = com.idit.mylibrary.R.drawable.circle_white;

            imageView.setImageResource(drawable);

            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(finalI);
                }
            });

            indicatorLayout.addView(imageView);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void changeFragment(int position) {
        pager.setCurrentItem(position, true);
    }


}
