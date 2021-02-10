package com.example.khatanotebook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Pk_child_AddInvoice extends AppCompatActivity {
    EditText pk_child_BillName,pk_child_totalAmount,pk_child_Advance,pk_child_totalItems;
    Button btn_addInvoice;
    String ID_No,PendingAmount = "0";
    DatabaseReference fireBaseDatabase;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_child__add_invoice);
        pk_child_BillName = findViewById(R.id.pk_child_BillName);
        pk_child_totalAmount = findViewById(R.id.pk_child_totalAmount);
        pk_child_Advance = findViewById(R.id.pk_child_Advance);
        pk_child_totalItems = findViewById(R.id.pk_child_totalItems);
        btn_addInvoice = findViewById(R.id.btn_addInvoice);
        fireBaseDatabase = FirebaseDatabase.getInstance().getReference().child("PakkaKhata");
        Intent intent = getIntent();
        ID_No = intent.getStringExtra("ID_NO");
        String date_time = java.time.LocalDate.now().toString();
        btn_addInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pk_child_BillName.getText().toString().trim().isEmpty()){
                    pk_child_BillName.setError("Field cannot be Empty");
                }
                else if (pk_child_totalAmount.getText().toString().trim().isEmpty()){
                        pk_child_totalAmount.setError("Field cannot be Empty");
                }
                else if (pk_child_Advance.getText().toString().trim().isEmpty()){
                        pk_child_Advance.setText("0");
                }
                else if (pk_child_totalItems.getText().toString().trim().isEmpty()){
                        pk_child_totalItems.setError("Field cannot be Empty");
                }
                else {
                    HashMap<String,String> data = new HashMap<>();
                    data.put("InvoiceName",pk_child_BillName.getText().toString());
                    data.put("TotalAmount",pk_child_totalAmount.getText().toString());
                    data.put("AdvanceAmount",pk_child_Advance.getText().toString());
                    data.put("TotalItems",pk_child_totalItems.getText().toString());
                    data.put("DateTime",date_time);
                    data.put("PendingAmount",PendingAmount);
                    fireBaseDatabase.child(ID_No).child("Total_BillsDetail").push().setValue(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Pk_child_AddInvoice.this,"Invoice Added Successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Pk_child_AddInvoice.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    onBackPressed();
                    finish();


                }
            }
        });


    }
}