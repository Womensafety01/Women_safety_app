package com.example.safetywomenapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeScreenFragment extends Fragment implements SensorEventListener {

    static boolean SOSMode = false;
    private static boolean open = false;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserEmail;
    public static String userID;
    SwitchCompat shakeswitch;

    MediaPlayer player;

    Button location, sms;
    TextView t1, t2, t3, t4, t5, t6, t7;
    String latitude,longitude,country,locality,address;
    String sPhone = "9172422245",sMsg;

    private SensorManager sm;
    private float acelVal;
    private float acelLast;
    private float shake;

    FusedLocationProviderClient fusedLocationProviderClient;

    //private SensorEventListener sensorListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        currentUserEmail = currentUser.getEmail();

        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        location = v.findViewById(R.id.bt_location);
        sms = v.findViewById(R.id.open_sms);

        t1 = v.findViewById(R.id.txt_view1);
        t2 = v.findViewById(R.id.txt_view2);
        t3 = v.findViewById(R.id.txt_view3);
        t4 = v.findViewById(R.id.txt_view4);
        t5 = v.findViewById(R.id.txt_view5);
        t6 = v.findViewById(R.id.txt_view6);
        t7 = v.findViewById(R.id.txt_view7);

        // Initialize fusedLocationProviderClient

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Sensor

        shakeswitch = v.findViewById(R.id.shakeSwitch);
        shakeswitch.setChecked(open);
        shakeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // Intent shake = new Intent(getContext(), ShakeService.class);
                if (b) {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Shake to send alerts is ENABLED", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setDuration(2000);
                    snackbar.show();
                    open = true;
                    //shakeMessage();

                } else {
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Shake to send alerts is DISABLED", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setDuration(2000);
                    snackbar.show();
                    open = false;
                    //getActivity().stopService(shake);
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getLocations();
                } else {
                    //when permission denied
                   requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                // Set  latitude on Textview
                                t1.setText(Html.fromHtml("<font color='#2EB62C'><b>Latitude</b><br></font>" + addresses.get(0).getLatitude()));

                                // Set  longitude on Textview
                                t2.setText(Html.fromHtml("<font color='#2EB62C'><b>Longitude</b><br></font>" + addresses.get(0).getLongitude()));

                                // Set  Country name on Textview
                                t3.setText(Html.fromHtml("<font color='#2EB62C'><b>Country:</b><br></font>" + addresses.get(0).getCountryName()));

                                // Set  locality on Textview
                                t4.setText(Html.fromHtml("<font color='#2EB62C'><b>Locality:</b><br></font>" + addresses.get(0).getLocality()));

                                // Set  latitude on Textview
                                t5.setText(Html.fromHtml("<font color='#2EB62C'><b>Address:</b><br></font>" + addresses.get(0).getAddressLine(0)));

                                // Set  latitude on Textview
                                t6.setText(Html.fromHtml("<font color='#2EB62C'><b></b><br></font>" + addresses.get(0).getLatitude()));

                                // Set  longitude on Textview
                                t7.setText(Html.fromHtml("<font color='#2EB62C'><b></b><br></font>" + addresses.get(0).getLongitude()));


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

                // Check condition
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    // when permission is granted create method
                    sendMessage();
                }else {
                    // When permission not granted request for permission
                    requestPermissions( new String[]{Manifest.permission.SEND_SMS},100);
                }

            }
        });


        return v;
    }


    private SensorEventListener sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                acelLast = acelVal;
                acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = acelVal - acelLast;
                shake = shake * 0.9f + delta;

                if (shake > 12) {
                    sendMessage();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


    private void stopPlayer(){
        if (player != null){
            player.release();
            player = null;
            Toast.makeText(getContext(), "Media Player Released", Toast.LENGTH_SHORT).show();
        }
    }



    private void sendMessage() {

        latitude = t6.getText().toString().trim();
        longitude = t7.getText().toString().trim();
        country = t3.getText().toString().trim();
        locality = t4.getText().toString().trim();
        address = t5.getText().toString().trim();

        //sPhone = phno.getText().toString().trim();
        sMsg = "Help Me, I am in trouble :"+ "\n" + address + "\n" + latitude;

        if (!sPhone.equals("") && !sMsg.equals("")){
            // when both edit text value is not equal to blank
            // Initialize sms  manager
            ArrayList<String> contactno = ContactFragment.contactno;
            SmsManager smsManager = SmsManager.getDefault();
            // Send text message
            StringBuffer smsBody = new StringBuffer();
            smsBody.append("Hey! I am in danger. Help Me.. MY Location:\n");
            smsBody.append("https://www.google.com/maps/search/?api=1&query=");
            smsBody.append(latitude);
            smsBody.append(",");
            smsBody.append(longitude);
            smsBody.append("\n");
            smsBody.append(locality);

            for (String no : contactno) {
                smsManager.sendTextMessage(no, null, smsBody.toString(), null, null);
                //playSiren();
            }

        }else {

            // When edit text value is blank display this msg

            Toast.makeText(getContext(), "Invalid data or Empty !", Toast.LENGTH_SHORT).show();
        }


    }

    private void playSiren() {
        if (player == null){
            player = MediaPlayer.create(getContext(), R.raw.siren);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });

        }
        player.start();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
