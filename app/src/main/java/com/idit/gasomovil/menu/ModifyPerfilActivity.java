package com.idit.gasomovil.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idit.gasomovil.R;

public class ModifyPerfilActivity extends AppCompatActivity {

    private static final String LOG = "";
    private static final String TAG = "";
    private EditText mName, mLastName, mEmail, mPassword, mSerie;
    private String mModel, mBrand;
    private Object mYear;

    private View mNameFormView, mLastNameFormView, mEmailFormView, mPasswordFormView, mBrandFormView,
    mModelFormView, mYearFormView, mSerieFormView;

    private DatabaseReference mDatabase;

    //TODO Is necesary modify components in your XML file to addapt board

    //TODO Missing add acitivity for modify password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_perfil);


        //database.getReference().child("User").child(mUser.getUid());

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
        final String valueUid = (String) getIntent().getExtras().getString("uid");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(valueUid);

        Button btnModify = (Button) findViewById(R.id.modifyPerfil_button);

        switch (valueElement){
            case "name":
                mNameFormView.setVisibility(View.VISIBLE);
                mName.setText(valuePerfil);

                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("name").setValue(String.valueOf(mName.getText()));
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "lastName":
                mLastNameFormView.setVisibility(View.VISIBLE);
                mLastName.setText(valuePerfil);
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("last_name").setValue(String.valueOf(mLastName.getText()));
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "email":
                mEmailFormView.setVisibility(View.VISIBLE);
                mEmail.setText(valuePerfil);
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("email").setValue(String.valueOf(mEmail.getText()));
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "password":
                mPasswordFormView.setVisibility(View.VISIBLE);
                mPassword.setText(valuePerfil);
                break;
            case "brand":
                mBrandFormView.setVisibility(View.VISIBLE);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mBrand = (String) parent.getItemAtPosition(position);
                        Toast.makeText(ModifyPerfilActivity.this, (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("brand").setValue(mBrand);
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "model":
                mModelFormView.setVisibility(View.VISIBLE);
                spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mModel = (String) parent.getItemAtPosition(position);
                        Toast.makeText(ModifyPerfilActivity.this, (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("model").setValue(mModel);
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "year":
                mYearFormView.setVisibility(View.VISIBLE);
                spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mYear = parent.getItemAtPosition(position);
                        //Toast.makeText(ModifyPerfilActivity.this, (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("year").setValue(Integer.parseInt((String) mYear));
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case "serie":
                mSerieFormView.setVisibility(View.VISIBLE);
                mSerie.setText(valuePerfil);
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.child("serie").setValue(String.valueOf(mSerie.getText()));
                        Toast.makeText(ModifyPerfilActivity.this, "Mofificación guardada con exito.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

}
