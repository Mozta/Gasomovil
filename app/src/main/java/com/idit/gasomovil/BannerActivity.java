package com.idit.gasomovil;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.idit.mylibrary.Step;
import com.idit.mylibrary.TutorialActivity;

public class BannerActivity extends TutorialActivity {

    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(com.idit.mylibrary.R.layout.activity_tutorial);

        btnSignIn = findViewById(R.id.sign_in);
        btnSignUp = findViewById(R.id.sign_up);

        addFragment(new Step.Builder().setTitle("Ahora sabrás cuantos litros te cargan en la gasolinera")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner1)
                .build());
        addFragment(new Step.Builder().setTitle("Identifica la gasolinera mejor evualuada")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner2)
                .build());
        addFragment(new Step.Builder().setTitle("Califica del 1 al 5 el servicio de las gasolineras")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner3)
                .build());
        addFragment(new Step.Builder().setTitle("Comparte tu calificación y comenta con otros usuarios")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner4)
                .build());
        addFragment(new Step.Builder().setTitle("Revisa cuánto te rinde el combustible despachado")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner5)
                .build());
        addFragment(new Step.Builder().setTitle("¡Disfruta éste y otros grandes beneficios que te brinda éste gadget y entra al futuro!")
                .setBackgroundColor(Color.parseColor("#99CC33"))
                .setDrawable(R.drawable.banner6)
                .build());

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activitymain = new Intent(BannerActivity.this, LoginActivity.class);
                startActivity(activitymain);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activitymain = new Intent(BannerActivity.this, RegisterActivity.class);
                startActivity(activitymain);
            }
        });

    }
}
