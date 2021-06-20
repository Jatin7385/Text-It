package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreenActivity extends AppCompatActivity {
    private Button back;
    private FloatingActionButton send;
    private EditText text;
    private String message;
    private String name,url;
    private TextView textView_name;
    private CircleImageView profilepic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_chat_screen);
        back = findViewById(R.id.back_button);
        send = findViewById(R.id.sendButton);
        text = findViewById(R.id.text);
        textView_name = findViewById(R.id.name);
        profilepic = findViewById(R.id.profilepic);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        name = (String) b.get("name");
        url = (String) b.get("url");

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
                System.out.println(message);
                text.setText("");
            }
        });
    }
}
