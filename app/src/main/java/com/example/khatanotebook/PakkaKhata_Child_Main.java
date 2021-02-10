package com.example.khatanotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PakkaKhata_Child_Main extends AppCompatActivity {
    String id_no, PhoneNumber, KhataName,Complete_Address;
    TextView pk_child_top_Name,pk_child_top_Cell,pk_child_top_Address,pk_child_top_TotalBills
            ,pk_child_top_TotalPendingBills,pk_child_top_PendingPayment;
    RecyclerView pk_child_recycler;
    ImageView pk_child_ivAddInvoice,pk_child_top_Location;
    PakkaKhata_Child_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakka_khata__child__main);
        init();
        if (ContextCompat.checkSelfPermission(PakkaKhata_Child_Main.this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PakkaKhata_Child_Main.this, new String[]{Manifest.permission.SEND_SMS},1);
        }

        Intent intent = getIntent();
        id_no = intent.getStringExtra("id_no");
        KhataName = intent.getStringExtra("Khata_Name");
        Complete_Address = intent.getStringExtra("Complete_Address");
        PhoneNumber = intent.getStringExtra("PhoneNumber");
        pk_child_recycler.setLayoutManager(new LinearLayoutManager(this));
        pk_child_recycler.setHasFixedSize(true);
        pk_child_top_Name.setText(KhataName);
        pk_child_top_Cell.setText("Phone no : "+PhoneNumber);
        pk_child_top_Address.setText("Address : "+Complete_Address);
        FirebaseRecyclerOptions<PakkaKhata_Child_ModelClass> options =
                new FirebaseRecyclerOptions.Builder<PakkaKhata_Child_ModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(id_no).child("Total_BillsDetail"),PakkaKhata_Child_ModelClass.class)
                        .build();
        adapter = new PakkaKhata_Child_Adapter(options,this,id_no,PhoneNumber);
        pk_child_recycler.setAdapter(adapter);
        pk_child_ivAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PakkaKhata_Child_Main.this,Pk_child_AddInvoice.class);
                intent.putExtra("ID_NO",id_no);
                startActivity(intent);
//                startActivity(new Intent(PakkaKhata_Child_Main.this,Pk_child_AddInvoice.class));
            }
        });
        FirebaseDatabase.getInstance().getReference().child("PakkaKhata").child(id_no).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pk_child_top_TotalBills.setText("Total Bills = "+snapshot.child("Total_Bills").getValue(String.class));
                pk_child_top_PendingPayment.setText("Pending Payments = "+snapshot.child("Pending_payment").getValue(String.class));
                pk_child_top_TotalPendingBills.setText("Pending Bills = "+snapshot.child("Pending_bills").getValue(String.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        pk_child_top_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PakkaKhata_Child_Main.this,"Location",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+Complete_Address));
                startActivity(intent);
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

    private void init() {
        pk_child_top_Name = findViewById(R.id.pk_child_top_Name);
        pk_child_top_Cell = findViewById(R.id.pk_child_top_Cell);
        pk_child_top_Address = findViewById(R.id.pk_child_top_Address);
        pk_child_recycler = findViewById(R.id.pk_child_recycler);
        pk_child_ivAddInvoice = findViewById(R.id.pk_child_ivAddInvoice);
        pk_child_top_TotalBills = findViewById(R.id.pk_child_top_TotalBills);
        pk_child_top_PendingPayment = findViewById(R.id.pk_child_top_PendingPayment);
        pk_child_top_TotalPendingBills = findViewById(R.id.pk_child_top_TotalPendingBills);
        pk_child_top_Location = findViewById(R.id.pk_child_top_Location);
    }
}