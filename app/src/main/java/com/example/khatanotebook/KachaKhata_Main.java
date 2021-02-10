package com.example.khatanotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class KachaKhata_Main extends AppCompatActivity {
    ImageView kacha_addBill;
    RecyclerView recyclerView;
    KachaKhata_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kacha_khata__main);
        kacha_addBill = findViewById(R.id.kacha_addBill);
        recyclerView = findViewById(R.id.kachakhata_recyclerView);
        if (ContextCompat.checkSelfPermission(KachaKhata_Main.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(KachaKhata_Main.this, new String[]{Manifest.permission.SEND_SMS},1);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerOptions<KachaKhata_ModelClass> options =
                new FirebaseRecyclerOptions.Builder<KachaKhata_ModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("KachaKhata"),KachaKhata_ModelClass.class)
                        .build();
        adapter = new KachaKhata_Adapter(options,this);
        recyclerView.setAdapter(adapter);
        kacha_addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KachaKhata_Main.this,KachaKhata_AddBill_AddDetail.class));
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