package com.idit.gasomovil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyPerfilActivity extends AppCompatActivity {

    private EditText mName, mLastName, mEmail, mPassword, mBrand, mModel, mYear, mSerie;

    private View mNameFormView, mLastNameFormView, mEmailFormView, mPasswordFormView, mBrandFormView,
    mModelFormView, mYearFormView, mSerieFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mName = findViewById(R.id.modify_perfil_name);
        mLastName = findViewById(R.id.modify_perfil_lastname);
        mEmail = findViewById(R.id.modify_perfil_email);
        mPassword = findViewById(R.id.modify_perfil_password);
        //mBrand = findViewById(R.id.spinner_brand);
        //mModel = findViewById(R.id.spinner_model);
        //mYear = findViewById(R.id.spinner_year);
        mSerie = findViewById(R.id.modify_perfil_serie);

        mNameFormView = findViewById(R.id.form_modify_name);
        mLastNameFormView = findViewById(R.id.form_modify_lastname);
        mEmailFormView = findViewById(R.id.form_modify_email);
        mPasswordFormView = findViewById(R.id.form_modify_password);
        mBrandFormView = findViewById(R.id.form_modify_brand);
        mModelFormView = findViewById(R.id.form_modify_model);
        mYearFormView = findViewById(R.id.form_modify_year);
        mSerieFormView = findViewById(R.id.form_modify_serie);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_brand);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.brand, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        Spinner spinnerModel = (Spinner) findViewById(R.id.spinner_model);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterModel = ArrayAdapter.createFromResource(this,
                R.array.model, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerModel.setAdapter(adapterModel);

        Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_year);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterYear = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerYear.setAdapter(adapterYear);

        String valueElement = (String) getIntent().getExtras().getString("element");
        String valuePerfil = (String) getIntent().getExtras().getString("value_perfil");

        switch (valueElement){
            case "name":
                mNameFormView.setVisibility(View.VISIBLE);
                mName.setText(valuePerfil);
                break;
            case "lastName":
                mLastNameFormView.setVisibility(View.VISIBLE);
                mLastName.setText(valuePerfil);
                break;
            case "email":
                mEmailFormView.setVisibility(View.VISIBLE);
                mEmail.setText(valuePerfil);
                break;
            case "password":
                mPasswordFormView.setVisibility(View.VISIBLE);
                mPassword.setText(valuePerfil);
                break;
            case "brand":
                mBrandFormView.setVisibility(View.VISIBLE);
                break;
            case "model":
                mModelFormView.setVisibility(View.VISIBLE);
                break;
            case "year":
                mYearFormView.setVisibility(View.VISIBLE);
                break;
            case "serie":
                mSerieFormView.setVisibility(View.VISIBLE);
                mSerie.setText(valuePerfil);
                break;
            default:
                break;
        }
        /*if (valor.equals(2)){
            mLastNameFormView.setVisibility(View.VISIBLE);
        }*/

        Button btnModify = (Button) findViewById(R.id.modifyPerfil_button);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                        Toast.LENGTH_SHORT).show();
                finish();
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
