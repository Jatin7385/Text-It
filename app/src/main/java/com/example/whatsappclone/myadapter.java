package com.example.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>{
    ArrayList<datamodel> dataholder;
    Context context;
    public myadapter(ArrayList<datamodel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_design,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChatScreenActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Picasso.with(context).load(user.getPhotoUrl()).into(holder.img);
        //holder.img.setImageResource(dataholder.get(position).getImage());
        holder.header.setText(user.getDisplayName());
        //holder.desc.setText(dataholder.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class  myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView header,desc,time;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            header = itemView.findViewById(R.id.header);
            desc = itemView.findViewById(R.id.desc);
            context = itemView.getContext();
        }
    }
}
