package com.example.whatsappclone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.sql.Time;
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
    private FirebaseUser firebaseUser;
    private String myId;
    private String friendId;
    private List<TimeModel> timeModelList;
    private String maxTime,date;

    public myadapter(List<UsersModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_design,parent,false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();
        timeModelList = new ArrayList<>();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChatScreenActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("name",name);
                intent.putExtra("friendId",friendId);
                view.getContext().startActivity(intent);
            }
        });
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        user = userList.get(position);
        friendId = user.getId();
        url = user.getImageURL();
        name = user.getName();

        

        System.out.println("FRIEND ID : : " + position);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    TimeModel timeModel = snapshot1.getValue(TimeModel.class);
                    if(timeModel.getMyId() == myId && timeModel.getFriendId() == userList.get(position).getId())
                    {
                        String op = timeModel.getOption();
                        if(op.equals("0"))
                        {
                            date = timeModel.getDate();
                            holder.time.setText(date);
                        }
                        else if(op.equals("1")){
                            maxTime = timeModel.getTime();
                            holder.time.setText(maxTime.substring(0,5));
                        }
                    }
                    timeModelList.add(timeModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!url.equals("default")) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.view_image_dialog, null);
                    PhotoView imageView = view.findViewById(R.id.image_view);
                    Button back = view.findViewById(R.id.image_dismiss_button);
                    TextView name = view.findViewById(R.id.image_name);
                    alert.setView(view);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    Picasso.with(context).load(url).into(imageView);
                    name.setText(user.getName());
                    alertDialog.show();
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
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
            context = itemView.getContext();
            time = itemView.findViewById(R.id.row_time);
        }
    }
}
