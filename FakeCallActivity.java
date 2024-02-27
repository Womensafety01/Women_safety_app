package com.example.safetywomenapp;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;

public class FakeCallActivity extends AppCompatActivity {

    public RadioGroup rGroup;
    public int afterTime;
    public String waitingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);

        //RadioBtn
        rGroup = (RadioGroup) findViewById(R.id.rBtnGroup);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.rBtnNow){
                    afterTime = 0;
                }
                else if (checkedId == R.id.rBtnOne){
                    afterTime = 15000;
                    waitingTime = "Wait 15 Second";
                }
                else if (checkedId == R.id.rBtnFive){
                    afterTime = 60000;
                    waitingTime = "Wait 1 minute";
                }

                else if (checkedId == R.id.rBtnThirty){
                    afterTime = 1800000;
                    waitingTime = "Wait 30 minutes";
                }
                else if (checkedId == R.id.rBtnHour){
                    afterTime = 3600000;
                    waitingTime = "Wait 1 hour";
                }
            }
        });


    }

    public void callBtnEvent(View view){

        final Intent callEvent = new Intent(this,calling.class);
        final Handler handler = new Handler();
        //interstitialAdLoader

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(callEvent);
            }
        }, afterTime);

    }

}