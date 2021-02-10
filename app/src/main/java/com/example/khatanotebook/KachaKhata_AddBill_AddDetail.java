package com.example.khatanotebook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class KachaKhata_AddBill_AddDetail extends AppCompatActivity {
    EditText kk_edKhataName,ed_kkProductName,et_kkTotalBill,et_kkAdvance,et_kkPhoneNumber;
    Button btn_kkAdd;
    DatabaseReference firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kacha_khata__add_bill__add_detail);
        kk_edKhataName = findViewById(R.id.kk_edKhataName);
        ed_kkProductName = findViewById(R.id.ed_kkProductName);
        et_kkTotalBill = findViewById(R.id.et_kkTotalBill);
        et_kkAdvance = findViewById(R.id.et_kkAdvance);
        et_kkPhoneNumber = findViewById(R.id.et_kkPhoneNumber);
        btn_kkAdd = findViewById(R.id.btn_kkAdd);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("KachaKhata");
        btn_kkAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (kk_edKhataName.getText().toString().trim().isEmpty()){
                    kk_edKhataName.setError("Field should not empty");
                }
                else if (ed_kkProductName.getText().toString().trim().isEmpty()){
                    ed_kkProductName.setError("Field should not empty");
                }
                else {
                    if (et_kkTotalBill.getText().toString().trim().isEmpty()){
                        et_kkTotalBill.setText("0");
                    }
                    if (et_kkAdvance.getText().toString().trim().isEmpty()){
                        et_kkAdvance.setText("0");
                    }
                    if (et_kkPhoneNumber.getText().toString().trim().isEmpty()){
                        et_kkPhoneNumber.setText("");
                    }
//                    int total = Integer.parseInt(et_kkTotalBill.getText().toString());
//                    int advance = Integer.parseInt(et_kkAdvance.getText().toString());
//                    int pending = total-advance;
                    String datetime = java.time.LocalDate.now().toString();
                    HashMap<String,String> data= new HashMap<>();
                    data.put("Khata_Name",kk_edKhataName.getText().toString().trim());
                    data.put("Product_Name",ed_kkProductName.getText().toString().trim());
                    data.put("Total_Bill",et_kkTotalBill.getText().toString().trim());
                    data.put("Advance",et_kkAdvance.getText().toString().trim());
                    data.put("Phone_Number",et_kkPhoneNumber.getText().toString().trim());
                    data.put("Pending","0");
                    data.put("ID_No",firebaseDatabase.push().getKey());
                    data.put("DateTime",datetime);


                    firebaseDatabase.push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(KachaKhata_AddBill_AddDetail.this,"Bill Added to Khata NoteBook",Toast.LENGTH_SHORT).show();
                            kk_edKhataName.setText("");
                            ed_kkProductName.setText("");
                            et_kkTotalBill.setText("");
                            et_kkAdvance.setText("");
                            et_kkPhoneNumber.setText("");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(KachaKhata_AddBill_AddDetail.this,e.getMessage()+"Failure",Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });
    }
}