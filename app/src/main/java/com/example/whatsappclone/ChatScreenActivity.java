package com.example.whatsappclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatScreenActivity extends AppCompatActivity {
    Button back;
    FloatingActionButton send;
    EditText text;
    String message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_chat_screen);
        back = findViewById(R.id.back_button);
        send = findViewById(R.id.sendButton);
        text = findViewById(R.id.text);
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
