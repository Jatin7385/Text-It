package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    private Button next;
    private EditText phoneNumber;
    private CountryCodePicker ccp;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void init()
    {
        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumber);
        next = findViewById(R.id.next_button);

        final String[] phnum = {null};

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phnum[0] = phoneNumber.getText().toString();
                if(!phnum[0].isEmpty()) {
                    if(phnum[0].length() == 10) {
                        number = ccp.getFullNumber() + phnum[0];
                        Intent otp = new Intent(LoginActivity.this, VerifyotpActivity.class);
                        otp.putExtra("number", number);
                        startActivity(otp);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Please enter correct number",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    {
                        Toast.makeText(LoginActivity.this,"Enter a number",Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }
}