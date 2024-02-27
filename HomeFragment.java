package com.example.safetywomenapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static ArrayList<Integer> userImg = new ArrayList<Integer>() {{
        add(R.drawable.user1);
        add(R.drawable.user10);
        add(R.drawable.user11);
        add(R.drawable.user12);
        add(R.drawable.user13);
        add(R.drawable.user14);
        add(R.drawable.user15);
        add(R.drawable.user16);
        add(R.drawable.user17);
        add(R.drawable.user18);
        add(R.drawable.user19);

    }};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Add this sentence to the menu
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Main Menu");

        return v;
    }


/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.termscondition :
                startActivity(new Intent(getContext(),TermsConditions.class));
                return true;

            case R.id.AboutApp :
                startActivity(new Intent(getContext(),About_App.class));
                return true;

            case R.id.rateApp :
                startActivity(new Intent(getContext(),Rating_App.class));
                return true;

            case R.id.shareApp :

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plan");
                String shareBody="Click this Link and Download Trainning & Placement App : https://www.tour2tech.com ";
                String shareSubject = " Download Trainning & Placement Android App ";

                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

                startActivity(Intent.createChooser(sharingIntent,"Share Via"));
                return true;

            case R.id.aboutDeveloper :
                startActivity(new Intent(getContext(),About_Us.class));
                return true;

            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getContext(), "User Successfully LogOut..", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            default: return super.onOptionsItemSelected(item);
        }

    }

 */


}