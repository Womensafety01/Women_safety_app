package com.example.safetywomenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS extends AppCompatActivity {

    EditText phno,msg;
    Button btn_msg;
    String latitude, longitude, country, locality, address;
    String sPhone,sMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
            country = extras.getString("country");
            locality = extras.getString("locality");
            address = extras.getString("address");
        }

        phno=(EditText)findViewById(R.id.et_mobile);
        msg=(EditText)findViewById(R.id.et_msg);

        // Get values from edit text
       // sPhone = phno.getText().toString().trim();
        //sMsg =  "Latitude is :"+ latitude;
        msg.setText(latitude+"\n"+longitude+"\n"+country+"\n"+locality+"\n"+address);
       // sMsg=msg.getText().toString().trim();
       // Toast.makeText(this, ""+sMsg, Toast.LENGTH_SHORT).show();
        btn_msg=(Button)findViewById(R.id.send_sms);

        //Toast.makeText(this, ""+latitude, Toast.LENGTH_SHORT).show();

        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check condition
                if (ContextCompat.checkSelfPermission(SMS.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    // when permission is granted create method
                    sendMessage();
                }else {
                    // When permission not granted request for permission
                    ActivityCompat.requestPermissions(SMS.this, new String[]{Manifest.permission.SEND_SMS},100);
                }
            }

        });
    }

    private void sendMessage() {

        // check condition
        //msg.setText(latitude+"\n"+longitude+"\n"+country+"\n"+locality+"\n"+address);
        sPhone = phno.getText().toString().trim();
        sMsg = "Help Me, I am in trouble :"+ latitude + longitude ;

        if (!sPhone.equals("") && !sMsg.equals("")){
            // when both edit text value is not equal to blank
            // Initialize sms  manager
            SmsManager smsManager = SmsManager.getDefault();
            // Send text message
            smsManager.sendTextMessage(sPhone,null, sMsg,null,null);
            //Display Toast
            Toast.makeText(this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();
        }else {
            // When edit text value is blank display this msg

            Toast.makeText(this, "Invalid data or Empty !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // check condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // when permission is granted call method
            sendMessage();

        }else {
            // permission is denied then display toast msg
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }





}