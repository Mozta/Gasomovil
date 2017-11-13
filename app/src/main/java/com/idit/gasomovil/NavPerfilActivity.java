package com.idit.gasomovil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class NavPerfilActivity extends AppCompatActivity {

    private EditText mPasswordView, mName, mLastName, mEmail, mBrand, mYear, mModel, mSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up the login form.
        mName = (EditText) findViewById(R.id.perfil_name);
        mLastName = (EditText) findViewById(R.id.perfil_lastname);
        mEmail = (EditText) findViewById(R.id.perfil_email);
        mPasswordView = (EditText) findViewById(R.id.perfil_password);
        mBrand = (EditText) findViewById(R.id.perfil_brand);
        mModel = (EditText) findViewById(R.id.perfil_model);
        mYear = (EditText) findViewById(R.id.perfil_year);
        mSerie = (EditText) findViewById(R.id.perfil_serie);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }


    /* Launch activity for individual date to modify perfil*/

    public void editName(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editLastName(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editEmail(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editPsssword(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editBrand(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editModel(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editYear(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }

    public void editSerie(View view) {
        Intent tutorial = new Intent(NavPerfilActivity.this, ForgotPasswordActivity.class);
        startActivity(tutorial);
    }
}
