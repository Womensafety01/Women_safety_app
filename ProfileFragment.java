package com.example.safetywomenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    View v;
    TextView editName, editPhone;
    CircleImageView profileimgview;
    TextView textEmail;
    DatabaseReference databaseReference;
    static String name, phone, imageurls;
    String currentUserEmail;
    Button button;

    FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        editName = v.findViewById(R.id.editname);
        textEmail = v.findViewById(R.id.textemail);
        editPhone = v.findViewById(R.id.editphone);
        profileimgview = v.findViewById(R.id.imgvi);

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        firebaseStorage=FirebaseStorage.getInstance();
        final StorageReference storageReference=firebaseStorage.getReference().child("User_Profile");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    name = ds.child("name").getValue(String.class);
                    phone = ds.child("mno").getValue(String.class);
                    imageurls = ds.child("imageurl").getValue(String.class);
                    setDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button = v.findViewById(R.id.edit_profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                final String set_name = editName.getText().toString();
                final String set_email = textEmail.getText().toString();
                final String set_phone = editPhone.getText().toString();
                if (set_name == "" || set_email == "" || set_phone == "") {
                    Toast.makeText(getContext(), "Enter all Details", Toast.LENGTH_SHORT).show();
                } else {
                    if (set_phone.length() != 11) {
                        Toast.makeText(getContext(), "Invalid Phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (!(set_email.trim().matches(pattern))) {
                            Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                            ;
                        } else {
                            databaseReference.child("Users").orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        ds.getRef().child("name").setValue(set_name);
                                        ds.getRef().child("mno").setValue(set_phone);
                                        ds.getRef().child("email").setValue(set_email);
                                        Toast.makeText(getContext(), "Changed", Toast.LENGTH_LONG).show();
                                        currentUserEmail = set_email;
                                        name = set_name;
                                        phone = set_phone;
                                        setDetails();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                */
               Intent intent = new Intent(getActivity(),UpdatemyAccount.class);
               intent.putExtra("name",name);
               intent.putExtra("email",currentUserEmail);
               intent.putExtra("phoneno",phone);
               intent.putExtra("image",imageurls);
               startActivity(intent);

            }
        });


        return  v;
    }



    public static void setDetails(String n, String p) {
        name = n;
        phone = p;

    }

    private void setDetails() {
        editName.setText(name);
        textEmail.setText(currentUserEmail);
        editPhone.setText(phone);
        Glide.with(getContext())
                .load(imageurls)
                .into(profileimgview);
    }

}