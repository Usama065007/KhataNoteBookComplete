package com.example.khatanotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main_Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__start);
    }
    public void btn_kacha_khata(View view)
    {
        startActivity(new Intent(Main_Start.this,KachaKhata_Main.class));
    }
    public void btn_pakka_khata(View view)
    {
        startActivity(new Intent(Main_Start.this,PakkaKhata_Main.class));
    }
}