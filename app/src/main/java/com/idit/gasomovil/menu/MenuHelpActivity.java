package com.idit.gasomovil.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.idit.gasomovil.Adapter.MyAdapter;
import com.idit.gasomovil.Model.Item;
import com.idit.gasomovil.R;

import java.util.ArrayList;
import java.util.List;

public class MenuHelpActivity extends AppCompatActivity {

    private View mDevice, mFaqs, mSupport;

    RecyclerView list;
    RecyclerView.LayoutManager layoutManager;
    List<Item> items = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_device:
                    mDevice.setVisibility(View.VISIBLE);
                    mFaqs.setVisibility(View.GONE);
                    mSupport.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_faqs:
                    mDevice.setVisibility(View.GONE);
                    mFaqs.setVisibility(View.VISIBLE);
                    mSupport.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_support:
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_help);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        list = (RecyclerView)findViewById(R.id.recycler);
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        setData();

        Button btnSupport = (Button) findViewById(R.id.support_button);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Here implements method for send a email

                Toast.makeText(MenuHelpActivity.this, "Correo enviado exitosamente.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setData() {
        Item item = new Item("¿Cómo califico una gasolinera?","Soy su hijo",true);
        items.add(item);
        Item item2 = new Item("No puedo conectar mi dispositivo","Soy su hijo",true);
        items.add(item2);
        Item item3 = new Item("¿Cómo localizar una gasolinera","Soy su hijo",true);
        items.add(item3);
        Item item4 = new Item("Actualizar datos de cuenta","Soy su hijo",true);
        items.add(item4);
        Item item5 = new Item("Autos compatibles con gasomovil","Soy su hijo",true);
        items.add(item5);
        Item item6 = new Item("No puedo inicar carga","Soy su hijo",true);
        items.add(item6);
        Item item7 = new Item("¿Cómo cambiar mi foto de perfil?","Soy su hijo",true);
        items.add(item7);
        Item item8 = new Item("Reporte de gasolinera","Soy su hijo",true);
        items.add(item8);
        Item item9 = new Item("¿Cómo comentar?","Soy su hijo",true);
        items.add(item9);
        /*for(int i=0;i<20;i++){
            Item item = new Item("¿Cómo califico una gasolinera?","Soy su hijo",true);
            item.setText("hola");
            item.setSubText("nooo");
            items.add(item);
            if(i%2==0){
                Item item = new Item("Soy Item"+(i+1),"Soy item hijo "+(i+1),true);
                items.add(item);
            }
            else {
                Item item = new Item("Soy Item"+(i+1),"",false);
                items.add(item);
            }
        }*/
        MyAdapter adapter = new MyAdapter(items);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }
}
