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

public class MenuDiagnosisActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private View mEfficiency, mMotor, mOther;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_efficiency:
                    mTextMessage.setText(R.string.title_efficiency);
                    mEfficiency.setVisibility(View.VISIBLE);
                    mMotor.setVisibility(View.GONE);
                    mOther.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_engine:
                    mTextMessage.setText(R.string.title_engine);
                    mEfficiency.setVisibility(View.GONE);
                    mMotor.setVisibility(View.VISIBLE);
                    mOther.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_other:
                    mTextMessage.setText(R.string.title_other);
                    mEfficiency.setVisibility(View.GONE);
                    mMotor.setVisibility(View.GONE);
                    mOther.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_diagnosis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_diagnosis);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mEfficiency = findViewById(R.id.layout_effiency);
        mMotor = findViewById(R.id.layout_motor);
        mOther = findViewById(R.id.layout_other);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }
}
