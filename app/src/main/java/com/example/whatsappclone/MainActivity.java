package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button chats,status,calls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Fragment[] selectedFragment = {new ChatsFragment()};

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment[0]).commit();

        chats = (Button)findViewById(R.id.chats_button);
        status = (Button)findViewById(R.id.status_button);
        calls = (Button)findViewById(R.id.calls_button);

        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFragment[0] = new ChatsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment[0]).commit();
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFragment[0] = new StatusFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment[0]).commit();
            }
        });

        calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFragment[0] = new CallsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment[0]).commit();
            }
        });
    }
}