package com.example.safetywomenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Initialization

    Button location, sms;
    TextView t1, t2, t3, t4, t5;
    String latitude,longitude,country,locality,address;
    String sPhone = "9172422245",sMsg;

    private SensorManager sm;
    private float acelVal;
    private float acelLast;
    private float shake;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        location = (Button) findViewById(R.id.bt_location);
        sms = (Button)findViewById(R.id.open_sms);

        t1 = (TextView) findViewById(R.id.txt_view1);
        t2 = (TextView) findViewById(R.id.txt_view2);
        t3 = (TextView) findViewById(R.id.txt_view3);
        t4 = (TextView) findViewById(R.id.txt_view4);
        t5 = (TextView) findViewById(R.id.txt_view5);

        // Initialize fusedLocationProviderClient

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getLocations();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }


            }

            @SuppressLint("MissingPermission")
            private void getLocations() {

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        // Initialize Location
                        Location location = task.getResult();
                        if (location != null) {

                            // Initialize Address List
                            try {
                                //Initialize Geocoder
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                // Set  latitude on Textview
                                t1.setText(Html.fromHtml("<font color='#6200EE'><b></b><br></font>" + addresses.get(0).getLatitude()));

                                // Set  longitude on Textview
                                t2.setText(Html.fromHtml("<font color='#6200EE'><b></b><br></font>" + addresses.get(0).getLongitude()));

                                // Set  Country name on Textview
                                t3.setText(Html.fromHtml("<font color='#6200EE'><b>Country:</b><br></font>" + addresses.get(0).getCountryName()));

                                // Set  locality on Textview
                                t4.setText(Html.fromHtml("<font color='#6200EE'><b>Locality:</b><br></font>" + addresses.get(0).getLocality()));

                                // Set  latitude on Textview
                                t5.setText(Html.fromHtml("<font color='#6200EE'><b>Address:</b><br></font>" + addresses.get(0).getAddressLine(0)));

                                //Toast.makeText(MainActivity.this, "Address:"+t5.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });

        // open sms
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // latitude = t1.getText().toString().trim();
               // longitude = t2.getText().toString().trim();
              //  country = t3.getText().toString().trim();
              //  locality = t4.getText().toString().trim();
               // address = t5.getText().toString().trim();
                //Intent i = new  Intent(MainActivity.this,SMS.class);
               // i.putExtra("latitude",latitude);
               // i.putExtra("longitude",longitude);
               // i.putExtra("country",country);
              //  i.putExtra("locality",locality);
               // i.putExtra("address",address);
               // startActivity(i);
               // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                // Check condition
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    // when permission is granted create method
                    sendMessage();
                }else {
                    // When permission not granted request for permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS},100);
                }

            }
        });

    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if (shake > 12){
                sendMessage();
               // Toast.makeText(MainActivity.this, "Dont Shake Me", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void sendMessage() {

        latitude = t1.getText().toString().trim();
        longitude = t2.getText().toString().trim();
        country = t3.getText().toString().trim();
        locality = t4.getText().toString().trim();
        address = t5.getText().toString().trim();

        //sPhone = phno.getText().toString().trim();
        sMsg = "Help Me, I am in trouble :"+ "\n" + address + "\n" + latitude;

        if (!sPhone.equals("") && !sMsg.equals("")){
            // when both edit text value is not equal to blank
            // Initialize sms  manager
            SmsManager smsManager = SmsManager.getDefault();
            // Send text message
            StringBuffer smsBody = new StringBuffer();
            smsBody.append("Your location from SafeTravels!\n\n");
            smsBody.append("https://www.google.com/maps/search/?api=1&query=");
            smsBody.append(latitude);
            smsBody.append(",");
            smsBody.append(longitude);
            smsBody.append("\n");
            smsBody.append(locality);
            smsManager.sendTextMessage(sPhone,null, smsBody.toString(),null,null);
            //Display Toast
            Toast.makeText(this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();
        }else {
            // When edit text value is blank display this msg

            Toast.makeText(this, "Invalid data or Empty !", Toast.LENGTH_SHORT).show();
        }

    }
}