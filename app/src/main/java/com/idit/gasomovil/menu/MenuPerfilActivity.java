package com.idit.gasomovil.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idit.gasomovil.R;
import com.idit.gasomovil.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuPerfilActivity extends AppCompatActivity {

    private static final String TAG = "";
    private EditText mPasswordView, mName, mLastName, mEmail, mBrand, mYear, mModel, mSerie;
    private ImageView mProfile_image;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference myRef;

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    //Firabase
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_perfil);

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User").child(mUser.getUid());
        myRef.keepSynced(true);

        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();


        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up the login form.
        mProfile_image = (ImageView) findViewById(R.id.profile_image);
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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null){
                    // Load the image using Glide
                    Glide.with(mProfile_image.getContext())
                            .load(user.getProfile_image())
                            .placeholder(R.drawable.ic_account_circle_header)
                            .error(R.drawable.ic_account_circle_header)
                            .into(mProfile_image);
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

        mProfile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                //deleteImage();
                //uploadImage();
            }
        });

    }

    private void deleteImage() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference storagePrueba = FirebaseStorage.getInstance().getReferenceFromUrl(mUser.getPhotoUrl().toString());

        // Delete the file
        storagePrueba.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }

    private void uploadImage() {
        StorageReference ref = storageReference.child("profileImages/"+ UUID.randomUUID().toString());
        if(filePath != null) {
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Despues de subir se obtiene el enlace de descarga,
                            // el cual sera almacenado en el campo profile_image del usuario

                            //Se mapean los datos al modelo usuario y se suben
                            Map<String, Object> imageUpdate = new HashMap<>();
                            imageUpdate.put("profile_image", taskSnapshot.getDownloadUrl().toString());
                            myRef.updateChildren(imageUpdate);
                            //Se actualiza el perfil de usuario para enlazar la
                            // imagen subida a la BD con la imagen de perfil de firebase user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(Uri.parse(taskSnapshot.getDownloadUrl().toString()))
                                    .build();

                            mUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu imagen de perfil"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mProfile_image.setImageBitmap(bitmap);
                deleteImage();
                uploadImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }


    /* Launch activity for individual date to modify perfil*/
    public void editProfileImage(View view) {
        Intent tutorial = new Intent(MenuPerfilActivity.this, ModifyPerfilActivity.class);
        tutorial.putExtra("element", "name");
        tutorial.putExtra("value_perfil",mName.getText().toString());
        tutorial.putExtra("uid",mUser.getUid());
        startActivity(tutorial);
    }

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
