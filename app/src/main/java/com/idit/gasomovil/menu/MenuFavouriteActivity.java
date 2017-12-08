package com.idit.gasomovil.menu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idit.gasomovil.FavoriteAdapter;
import com.idit.gasomovil.FavoriteModel;
import com.idit.gasomovil.R;
import com.idit.gasomovil.Station;

import java.util.ArrayList;
import java.util.List;

public class MenuFavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FavoriteModel> result;
    private FavoriteAdapter adapter;

    private String userID;

    private TextView emptyText;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_station;

    public String key_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_favourite);
        toolbar.setTitle("Favoritos");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Favorites");


        emptyText = findViewById(R.id.text_no_data_favorite);

        result = new ArrayList<FavoriteModel>();

        recyclerView = findViewById(R.id.favorite_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FavoriteAdapter(result);
        recyclerView.setAdapter(adapter);

        updateList();

        checkIfEmpty();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 0:
                removeFavorite(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void updateList(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot_key, String s) {

                key_station = dataSnapshot_key.getKey();

                mDatabase_station = FirebaseDatabase.getInstance().getReference().child("Stations").child(key_station);
                mDatabase_station.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FavoriteModel model = dataSnapshot.getValue(FavoriteModel.class);
                        model.setKey(dataSnapshot.getKey());

                        result.add(model);
                        adapter.notifyDataSetChanged();

                        checkIfEmpty();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot_key, String s) {
                mDatabase_station = FirebaseDatabase.getInstance().getReference().child("Stations").child(key_station);
                mDatabase_station.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FavoriteModel model = dataSnapshot.getValue(FavoriteModel.class);
                        model.setKey(dataSnapshot.getKey());

                        int index = getItemIndex(model);
                        result.set(index, model);
                        adapter.notifyItemChanged(index);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot_key) {
                mDatabase_station = FirebaseDatabase.getInstance().getReference().child("Stations").child(key_station);
                mDatabase_station.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FavoriteModel model = dataSnapshot.getValue(FavoriteModel.class);
                        model.setKey(dataSnapshot.getKey());

                        int index = getItemIndex(model);
                        result.remove(index);
                        adapter.notifyItemRemoved(index);

                        checkIfEmpty();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot_key, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(FavoriteModel favorite){
        int index = -1;

        for (int i=0; i < result.size(); i++){
            if (result.get(i).key.equals(favorite.key)) {
                index = i;
                break;
            }
        }
        return index;

    }

    private void removeFavorite(int position){
        mDatabase.child(result.get(position).key).removeValue();
    }

    private void checkIfEmpty(){
        if (result.size() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

}
