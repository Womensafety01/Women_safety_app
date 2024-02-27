package com.example.safetywomenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

public class dialing extends AppCompatActivity {

    public Chronometer myChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialing);

        myChrono = (Chronometer) findViewById(R.id.chronometer2);
        myChrono.start();

    }

    public void quitCallEvent(View view) {
        startActivity(new Intent(dialing.this,MainScreen.class));
        finish();

    }

}