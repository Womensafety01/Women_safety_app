package com.example.safetywomenapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdatemyAccount extends AppCompatActivity {

    EditText aname,amobile;
    TextView aemail;
    CircleImageView profileimgview;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri uri;

    String imageUrl;

    Model ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemy_account);

        Toolbar toolbar =findViewById(R.id.updateacc_toolbar);
        toolbar.setTitle("Update Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ad = new Model();

        aname = findViewById(R.id.edname);
        amobile = findViewById(R.id.edmobile);
        aemail = findViewById(R.id.edemail);
        profileimgview = findViewById(R.id.updatephoto);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile ...");

        firebaseAuth= FirebaseAuth.getInstance();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

        firebaseStorage=FirebaseStorage.getInstance();
        final StorageReference storageReference =firebaseStorage.getReference().child("User_Profile");

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phoneno");
        String images = intent.getStringExtra("image");

        aname.setText(name);
        amobile.setText(phone);
        aemail.setText(email);
        Glide.with(UpdatemyAccount.this)
                .load(images)
                .into(profileimgview);


        profileimgview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent1=new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1,"Select Image"),PICK_IMAGE);
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profileimgview.setImageBitmap(bitmap);

            }catch (IOException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    

    private void uploadImage() {
        progressDialog.show();
        StorageReference storageRefrence = FirebaseStorage.getInstance().getReference().child("User_Profile").child(uri.getLastPathSegment());
        storageRefrence.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uriImage = uriTask.getResult();
                imageUrl= uriImage.toString();
                uploadupdateprofileData();


            }
        });
    }

    private void uploadupdateprofileData() {
        ad.setName(aname.getText().toString());
        ad.setMno(amobile.getText().toString());
        ad.setEmail(aemail.getText().toString());
        ad.setImageurl(imageUrl);
        databaseReference.setValue(ad);
        progressDialog.dismiss();
        Toast.makeText(this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),MainScreen.class));
        finish();
    }

    public void Updatemyacc(View view) {
        progressDialog.show();
        uploadImage();
    }
}