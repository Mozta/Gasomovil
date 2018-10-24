package com.idit.gasomovil.BluetoothRegister;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private int currentItem;

    private Button searchBT;

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


        searchBT = findViewById(R.id.register_bluetooth_button);

        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(BluetoothBannerActivity.this, "Cambia a la lista de dispositivos cercanos", Toast.LENGTH_SHORT).show();
                /*Intent searchActivity = new Intent(BluetoothBannerActivity.this, null);
                startActivity(searchActivity);*/
            }
        });
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor(int backgroundColor) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(backgroundColor);
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

    public void addFragment(Step step, int position) {
        steps.add(position, step);
        adapter.notifyDataSetChanged();
        notifyIndicator();
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
