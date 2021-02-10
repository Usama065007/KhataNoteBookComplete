package com.example.khatanotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText loginName, loginPassword;
    Button btn_login;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginName = findViewById(R.id.loginName);
        loginPassword = findViewById(R.id.loginPassword);
        btn_login = findViewById(R.id.btn_login);
        FirebaseDatabase.getInstance().getReference().child("verification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("username").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginName.getText().toString().trim().isEmpty())
                {
                    loginName.setError("Field cannot be Empty");
                }
                else if(loginPassword.getText().toString().trim().isEmpty())
                {
                    loginPassword.setError("Field cannot be Empty");
                }
                else
                {
                    if (loginName.getText().toString().equals(username))
                    {
                        if (loginPassword.getText().toString().equals(password))
                        {
                            startActivity(new Intent(MainActivity.this,Main_Start.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Password Invalid",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Username Invalid",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
}