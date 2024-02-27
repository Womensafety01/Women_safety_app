package com.example.safetywomenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    EditText name,email,pass,repass,phone;      //Declaration
    Button register;
    TextView login;
    TextInputLayout nameError,emailError,phoneError,passError;
    Model userData;

    //-------------- Firebase ---------------
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    // ------------- Progress Dialog ----------

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        pass = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.repassword);
        login = (TextView) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        nameError = (TextInputLayout) findViewById(R.id.nameError);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        phoneError = (TextInputLayout) findViewById(R.id.phoneError);
        passError = (TextInputLayout) findViewById(R.id.passError);

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to LoginActivity
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

    }

    public void SetValidation() {

        userData = new Model();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("REGISTERING ...");

        progressDialog.show();

        //Local Variable to store Password

        String password = pass.getText().toString().trim();
        String repassword = repass.getText().toString().trim();

        // Profile Data Store

        userData.setName(name.getText().toString());
        userData.setEmail(email.getText().toString());
        userData.setMno(phone.getText().toString());
        userData.setImageurl("https://firebasestorage.googleapis.com/v0/b/womensafety-fa171.appspot.com/o/profile_nav_logo.png?alt=media&token=8dc14aa7-7075-499f-bf47-cb5821271c98");

        // Firebase Database Reference

        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();


        // Validate Data

        // Full name

        if (TextUtils.isEmpty(userData.name)) {

            Toast.makeText(Register.this, "Please Enter Full Name ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }


        // Email
        if (TextUtils.isEmpty(userData.email)) {

            Toast.makeText(Register.this, "Please Enter Email ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        // password

        if (TextUtils.isEmpty(password)) {

            Toast.makeText(Register.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        // repassword

        if (TextUtils.isEmpty(repassword)) {

            Toast.makeText(Register.this, "Please Enter RePassword", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        if (password.length() < 6) {

            Toast.makeText(Register.this, "Password Must be more than 6 digit & less than 1 digit", Toast.LENGTH_SHORT).show();
        }


        // Mobile Number

        if (TextUtils.isEmpty(userData.mno)) {

            Toast.makeText(Register.this, "Please Enter Mobile Number ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        if (userData.mno.length() < 10) {

            Toast.makeText(Register.this, "Mobile no. must be more 11 digit number! Enter Valid number. ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }



        // Validation Done !!


        if (password.equals(repassword) && userData.mno.length() == 10 ) {

            firebaseAuth.createUserWithEmailAndPassword(userData.email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                myRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userData);
                                progressDialog.dismiss();
                                Toast.makeText(Register.this, "Registeration Done", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(), Login.class));
                                startActivity(new Intent(Register.this, Login.class));
                                finish();


                            } else {
                                String msg = task.getException().toString();
                                Toast.makeText(Register.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            // ...
                        }
                    });

        }

    }


}