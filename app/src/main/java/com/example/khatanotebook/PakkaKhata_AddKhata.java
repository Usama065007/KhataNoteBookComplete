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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PakkaKhata_AddKhata extends AppCompatActivity {
    EditText pk_edKhataName, pk_edCell, pk_edCity, pk_edArea, pk_edStreetAddress, pk_edIDCard;
    Button btn_pk_AddKhata;
    DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakka_khata__add_khata);
        init();
        btn_pk_AddKhata.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (pk_edKhataName.getText().toString().trim().isEmpty()){
                    pk_edKhataName.setError("Field must not empty");
                }
                else if (pk_edCell.getText().toString().trim().isEmpty()){
                    pk_edCell.setError("Field must not empty");
                }
                else if (pk_edIDCard.getText().toString().trim().isEmpty()){
                    pk_edIDCard.setError("Field must not empty");
                }
                else if (pk_edCity.getText().toString().trim().isEmpty()){
                    pk_edCity.setError("Field must not empty");
                }
                else if (pk_edArea.getText().toString().trim().isEmpty()){
                    pk_edArea.setError("Field must not empty");
                }
                else if (pk_edStreetAddress.getText().toString().trim().isEmpty()){
                    pk_edStreetAddress.setError("Field must not empty");
                }
                else {
                    String ID_no = firebaseDatabase.push().getKey();
                    String date_time = java.time.LocalDate.now().toString();
                    HashMap<String,String> data = new HashMap<>();
                    data.put("Khata_name",pk_edKhataName.getText().toString());
                    data.put("Phone_number",pk_edCell.getText().toString());
                    data.put("City_name",pk_edCity.getText().toString());
                    data.put("Area_name",pk_edArea.getText().toString());
                    data.put("Street_address",pk_edStreetAddress.getText().toString());
                    data.put("Date_time",date_time);
                    data.put("Pending_payment","0");
                    data.put("Total_Bills","0");
                    data.put("Pending_bills","0");
                    data.put("ID_no",ID_no);
                    data.put("ID_CardNumber",pk_edIDCard.getText().toString());
                    assert ID_no != null;
                    firebaseDatabase.child(ID_no).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PakkaKhata_AddKhata.this,"Khata Added Successfully",Toast.LENGTH_SHORT).show();
                            pk_edKhataName.setText("");
                            pk_edCell.setText("");
                            pk_edCity.setText("");
                            pk_edKhataName.setText("");
                            pk_edStreetAddress.setText("");
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PakkaKhata_AddKhata.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    onBackPressed();
                    finish();
                }

            }
        });
    }

    private void init() {
        pk_edKhataName = findViewById(R.id.loginName);
        pk_edCell = findViewById(R.id.pk_edCell);
        pk_edCity = findViewById(R.id.pk_edCity);
        pk_edArea = findViewById(R.id.pk_edArea);
        pk_edStreetAddress = findViewById(R.id.pk_edStreetAddress);
        pk_edIDCard = findViewById(R.id.pk_edIDCard);
        btn_pk_AddKhata = findViewById(R.id.btn_pk_AddKhata);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("PakkaKhata");
    }
}