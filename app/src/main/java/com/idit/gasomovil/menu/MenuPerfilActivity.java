package com.idit.gasomovil.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.R;
import com.idit.gasomovil.User;

public class MenuPerfilActivity extends AppCompatActivity {

    private static final String TAG = "";
    private EditText mPasswordView, mName, mLastName, mEmail, mBrand, mYear, mModel, mSerie;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_perfil);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
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

        // Creamos la referencia a la base de datps
        mUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("User").child(mUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null){
                    mName.setText(user.getName());
                    mLastName.setText(user.getLast_name());
                    mEmail.setText(user.getEmail());
                    mBrand.setText(user.getBrand());
                    mModel.setText(String.valueOf(user.getModel()));
                    mYear.setText(String.valueOf(user.getYear()));
                    mSerie.setText(user.getSerie());
                }
                else{
                    Log.e(TAG, "Snapshot error");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }


    /* Launch activity for individual date to modify perfil*/

    public void editName(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "name");
        tutorial.putExtra("value_perfil",mName.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editLastName(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "lastName");
        tutorial.putExtra("value_perfil",mLastName.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editEmail(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "email");
        tutorial.putExtra("value_perfil",mEmail.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editPsssword(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "password");
        //tutorial.putExtra("value_perfil",mBrand.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editBrand(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "brand");
        tutorial.putExtra("value_perfil",mBrand.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editModel(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "model");
        tutorial.putExtra("value_perfil",mModel.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editYear(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "year");
        tutorial.putExtra("value_perfil",mYear.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

    public void editSerie(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "serie");
        tutorial.putExtra("value_perfil",mSerie.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }
}
