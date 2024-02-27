package com.example.safetywomenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class calling extends AppCompatActivity {

    public MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        //backgroundMusic
        player  = MediaPlayer.create(calling.this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();
        //backgroundMusicEnd

    }

    public void acceptCallEvent(View view){
        Intent acceptCall = new Intent(this,dialing.class);
        startActivity(acceptCall);
        player.stop();
        finish();

    }

    public void denyCall(View view){
        Intent rejectCall = new Intent(this,FakeCallActivity.class);
        startActivity(rejectCall);
        player.stop();
        finish();


    }

}