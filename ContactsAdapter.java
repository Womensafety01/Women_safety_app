package com.example.safetywomenapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<com.example.safetywomenapp.ContactsAdapter.MyViewHolder> {
    ArrayList<String> names, no;
    ArrayList<Integer> img;
    Context context;

    public ContactsAdapter(Context ct, ArrayList<String> contactNames, ArrayList<String> contactNo, ArrayList<Integer> imgid) {
        names = contactNames;
        no = contactNo;
        img = imgid;
        context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv1.setText(names.get(position));
        holder.tv2.setText(no.get(position));
        holder.img.setImageResource(HomeFragment.userImg.get(position % 35));

        holder.btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = holder.tv2.getText().toString();
                String call = "tel:"+mobile.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                holder.rootview.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return no.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView img;
        View rootview;
        int position;
        Model model;
        Button btncall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rootview = itemView;
            tv1 = itemView.findViewById(R.id.ContactName);
            tv2 = itemView.findViewById(R.id.ContactNo);
            img = itemView.findViewById(R.id.Dp);
            btncall = itemView.findViewById(R.id.btncalls);


        }
    }
}
