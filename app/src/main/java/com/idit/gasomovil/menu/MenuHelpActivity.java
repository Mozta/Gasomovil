package com.idit.gasomovil.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.idit.gasomovil.R;

public class MenuHelpActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private View mDevice, mFaqs, mSupport;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_device:
                    mTextMessage.setText(R.string.title_device);
                    mDevice.setVisibility(View.VISIBLE);
                    mFaqs.setVisibility(View.GONE);
                    mSupport.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_faqs:
                    mTextMessage.setText(R.string.title_faqs);
                    mDevice.setVisibility(View.GONE);
                    mFaqs.setVisibility(View.VISIBLE);
                    mSupport.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_support:
                    mTextMessage.setText(R.string.title_support);
                    mDevice.setVisibility(View.GONE);
                    mFaqs.setVisibility(View.GONE);
                    mSupport.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDevice = findViewById(R.id.layout_device);
        mFaqs = findViewById(R.id.layout_faqs);
        mSupport = findViewById(R.id.layout_support);

        mTextMessage = (TextView) findViewById(R.id.message_help);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_help);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }
}
