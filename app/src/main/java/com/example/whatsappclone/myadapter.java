package com.example.whatsappclone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>{
    ArrayList<datamodel> dataholder;
    Context context;
    List<UsersModel> userList;
    private UsersModel user;
    private String url;
    private String name;

    public myadapter(List<UsersModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_design,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChatScreenActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("name",name);
                intent.putExtra("friendId",user.getId());
                view.getContext().startActivity(intent);
            }
        });
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        user = userList.get(position);
        url = user.getImageURL();
        name = user.getName();

        //System.out.println("URL : "+url);
        if(url.equals("default"))
        {
            String uri = "@drawable/profilepicc";
            int imageResource = context.getResources().getIdentifier(uri,null,context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.img.setImageDrawable(res);
        }
        else {
            Picasso.with(context).load(url).into(holder.img);
        }
        holder.header.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
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
