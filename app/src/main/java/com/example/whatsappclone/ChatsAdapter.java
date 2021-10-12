package com.example.whatsappclone;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {

    Context context;
    List<ChatsModel> chatsList;
    ProgressBar progressBar;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public static final int MESSAGE_RIGHT_NOIMAGE = 0;//For the current user
    public static final int MESSAGE_LEFT_NOIMAGE = 1;//For the friend user
    public static final int MESSAGE_RIGHT_IMAGE = 2;//For the current user
    public static final int MESSAGE_LEFT_IMAGE = 3;//For the friend user
    private int viewtype;

    public ChatsAdapter(Context context, List<ChatsModel> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_RIGHT_NOIMAGE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sending_message,parent,false);
            viewtype = 0;
            return new MyViewHolder(view);
        }
        else if(viewType == MESSAGE_LEFT_NOIMAGE)
            {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_receiving_message,parent,false);
                viewtype = 1;
                return new MyViewHolder(view);
            }
        else if(viewType == MESSAGE_RIGHT_IMAGE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sending_image,parent,false);
            viewtype = 2;
            return new MyViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_recieving_image,parent,false);
            viewtype = 3;
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChatsModel chats = chatsList.get(position);
        System.out.println(viewtype);
        if(viewtype == 2 || viewtype == 3)
        {
            try {
                Picasso.with(context).load(chats.getImageUrl()).into(holder.image);
                holder.showMessage.setText(chats.getMessage());
                System.out.println(chats.getMessage());
                System.out.println(chats.getImageUrl());
            }
            catch (Exception e) {
                System.out.println("ERROR : " + e.getLocalizedMessage());
                System.out.println(chats.getImageUrl());
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.view_chat_image_dialog, null);
                    PhotoView imageView = view.findViewById(R.id.image_view);
                    Button back = view.findViewById(R.id.image_dismiss_button);
                    TextView message = view.findViewById(R.id.image_name);
                    alert.setView(view);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    Picasso.with(context).load(chats.getImageUrl()).into(imageView);
                    message.setText(chats.getMessage());
                    alertDialog.show();
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
        }

        else{
            try {
                holder.message.setText(chats.getMessage());
            }
            catch (Exception e)
            {
                System.out.println("ERROR : "+e.getLocalizedMessage());
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView message,showMessage;//Show message is for the text below the image
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_chat);
            message = itemView.findViewById(R.id.message);
            showMessage = itemView.findViewById(R.id.show_message);
            image = itemView.findViewById(R.id.image_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chatsList.get(position).getSender().equals(user.getUid()) && chatsList.get(position).getImageUrl().equals("null"))
        {
            return MESSAGE_RIGHT_NOIMAGE;
        }
        else if(chatsList.get(position).getReceiver().equals(user.getUid()) && chatsList.get(position).getImageUrl().equals("null")){return MESSAGE_LEFT_NOIMAGE;}
        else if(chatsList.get(position).getSender().equals(user.getUid()) && !chatsList.get(position).getImageUrl().equals("null")){return MESSAGE_RIGHT_IMAGE;}
        else if(chatsList.get(position).getReceiver().equals(user.getUid()) && !chatsList.get(position).getImageUrl().equals("null")){return MESSAGE_LEFT_IMAGE;}

        return MESSAGE_RIGHT_NOIMAGE;
    }
}
