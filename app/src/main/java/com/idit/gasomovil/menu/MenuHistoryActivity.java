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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idit.gasomovil.HistoryAdapter;
import com.idit.gasomovil.HistoryModel;
import com.idit.gasomovil.R;

import java.util.ArrayList;
import java.util.List;

public class MenuHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<HistoryModel> result;
    private HistoryAdapter adapter;

    private String userID;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        toolbar.setTitle("Historial");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Historial");

        result = new ArrayList<>();

        recyclerView = findViewById(R.id.history_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new HistoryAdapter(result);
        recyclerView.setAdapter(adapter);

        updateList();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 0:
                break;
            case 1:
                break;
        }
        return super.onContextItemSelected(item);
    }



    private void updateList(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                model.setKey(dataSnapshot.getKey());

                result.add(model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.remove(index);
                adapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(HistoryModel history){
        int index = -1;

        for (int i=0; i < result.size(); i++){
            if (result.get(i).key.equals(history.key)) {
                index = i;
                break;
            }
        }
        return index;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }
}
