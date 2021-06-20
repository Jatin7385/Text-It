package com.example.whatsappclone;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {

    Context context;
    List<ChatsModel> chatsList;

    public static final int MESSAGE_RIGHT = 0;//For the current user
    public static final int MESSAGE_LEFT = 1;//For the friend user

    public ChatsAdapter(Context context, List<ChatsModel> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_RIGHT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_sending_message,parent,false);
            return new MyViewHolder(view);
        }
        else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_receiving_message,parent,false);
                return new MyViewHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChatsModel chats = chatsList.get(position);

        holder.message.setText(chats.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(chatsList.get(position).getSender().equals(user.getUid()))
        {
            return MESSAGE_RIGHT;
        }
        else{return MESSAGE_LEFT;}
    }
}
