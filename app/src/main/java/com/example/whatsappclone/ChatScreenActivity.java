package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.network.ListNetworkRequest;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreenActivity extends AppCompatActivity {
    private Button back;
    private FloatingActionButton send;
    private EditText text;
    private String message;
    private String name,url,friendId,myId;
    private TextView textView_name;
    private CircleImageView profilepic;
    private FirebaseUser user;

    private List<ChatsModel> chatsList;
    private ChatsAdapter adapter;
    private RecyclerView recView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_chat_screen);

        getSupportActionBar().hide();

        user = FirebaseAuth.getInstance().getCurrentUser();

        back = findViewById(R.id.back_button);
        send = findViewById(R.id.sendButton);
        text = findViewById(R.id.text);
        textView_name = findViewById(R.id.name);
        profilepic = findViewById(R.id.profilepic);

        recView = findViewById(R.id.chats_recView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recView.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        name = (String) b.get("name");
        url = (String) b.get("url");
        friendId = (String) b.get("friendId");
        myId = user.getUid();

        textView_name.setText(name);
        Picasso.with(ChatScreenActivity.this).load(url).into(profilepic);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        message = text.getText().toString();
        System.out.println(message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send text
                message = text.getText().toString();
                sendMessage(myId,friendId,message);
                System.out.println(message);
                text.setText("");
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length() > 0)
                {
                    send.setEnabled(true);
                }
                else
                    {
                        send.setEnabled(false);
                    }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readMessages(myId,friendId);
    }

    private void readMessages(String myId,String friendId) {
        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    ChatsModel chats = snapshot1.getValue(ChatsModel.class);
                    if(chats.getSender().equals(myId) && chats.getReceiver().equals(friendId) || chats.getSender().equals(friendId) && chats.getReceiver().equals(myId))
                    {
                        chatsList.add(chats);
                    }

                    adapter = new ChatsAdapter(ChatScreenActivity.this,chatsList);
                    recView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String myId, String friendId, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",myId);
        hashMap.put("receiver",friendId);
        hashMap.put("message",message);
        databaseReference.child("Chats").push().setValue(hashMap);
    }
}
