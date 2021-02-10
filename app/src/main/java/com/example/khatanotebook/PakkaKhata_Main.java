package com.example.khatanotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class PakkaKhata_Main extends AppCompatActivity {
    RecyclerView pk_recyclerView;
    ImageView pk_addBill;
    PakkaKhata_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakka_khata__main);
        pk_recyclerView = findViewById(R.id.pk_recyclerView);
        pk_addBill = findViewById(R.id.pk_addBill);
        pk_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pk_recyclerView.setHasFixedSize(true);
        FirebaseRecyclerOptions<PakkaKhata_ModelClass> options =
                new FirebaseRecyclerOptions.Builder<PakkaKhata_ModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PakkaKhata"),PakkaKhata_ModelClass.class)
                        .build();
        adapter = new PakkaKhata_Adapter(options,this);
        pk_recyclerView.setAdapter(adapter);
        pk_addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PakkaKhata_Main.this,PakkaKhata_AddKhata.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}