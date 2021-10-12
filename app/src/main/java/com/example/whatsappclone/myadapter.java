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
import androidx.cardview.widget.CardView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private String maxTime,maxdate;
    private String time;
    private String date;
    private int listsize;

    public myadapter(List<UsersModel> userList) {

        this.userList = userList;
        sortUserList(userList);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_row_design,parent,false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();
        timeModelList = new ArrayList<>();
        time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        user = userList.get(position);
        friendId = user.getId();
        url = user.getImageURL();
        name = user.getName();

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.card.getContext(), ChatScreenActivity.class);
                intent.putExtra("url",userList.get(position).getImageURL());
                intent.putExtra("name",userList.get(position).getName());
                intent.putExtra("friendId",userList.get(position).getId());
                holder.card.getContext().startActivity(intent);
            }
        });

        System.out.println("FRIEND ID : : " + friendId);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    TimeModel timeModel = snapshot1.getValue(TimeModel.class);
//                    System.out.println("USER ID'S : " +timeModel.getFriendId() + " , " +  userList.get(position).getId());
                    try {
                        if (timeModel.getMyId().equals(myId) && timeModel.getFriendId().equals(userList.get(position).getId())) {
                            if (!timeModel.getDate().equals(date)) {
                                date = timeModel.getDate();
                                holder.time_text.setText(date);
                            } else {
                                maxTime = timeModel.getTime();
                                holder.time_text.setText(maxTime.substring(0, 5));
                            }
                        }
                        timeModelList.add(timeModel);
                    }catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage().trim());
                    }
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
                    Picasso.with(context).load(userList.get(position).getImageURL()).into(imageView);
                    name.setText(userList.get(position).getName());
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
        TextView header;
        TextView desc;
        TextView time_text;
        CardView card;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            header = itemView.findViewById(R.id.header);
            context = itemView.getContext();
            time_text = itemView.findViewById(R.id.row_time);
            card = itemView.findViewById(R.id.card);
        }
    }

    private void sortUserList(List<UsersModel> userList) {
        List<TimeModel> timeModelList = new ArrayList<>();
        List<UsersModel> userList1 = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    TimeModel timeModel = snapshot1.getValue(TimeModel.class);
                    timeModelList.add(timeModel);
                }

                Collections.sort(timeModelList,TimeModel.TimeModelComparator);
                Collections.reverse(timeModelList);

                for(TimeModel timeModel : timeModelList)
                {
                    System.out.println("Time : " + timeModel.getTime());
                }

                for(TimeModel timeModel : timeModelList)
                {
                    for(UsersModel user : userList)
                    {
                        if(user.getId().equals(timeModel.getFriendId()))
                        {
                            userList1.add(user);

                        }
                    }
                }
                updateReceiptsList(userList1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });;
    }

    public void updateReceiptsList(List<UsersModel> newlist) {
        userList.clear();
        userList = newlist;
        this.notifyDataSetChanged();
    }
}
