package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button next;
    private EditText phoneNumber;
    private CountryCodePicker ccp;
    private String full_number;
    private String number;
    private static final String TAG = "PhoneAuthActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ccp = findViewById(R.id.ccp);
        phoneNumber = findViewById(R.id.phoneNumber);
        next = findViewById(R.id.next_button);

        mAuth = FirebaseAuth.getInstance();

        final String[] phnum = {null};

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phnum[0] = phoneNumber.getText().toString().trim();
                if(!phnum[0].isEmpty()) {
                    if(phnum[0].length() == 10) {
                        full_number = "+" + ccp.getFullNumber()+"-" + phnum[0];
                        number = phnum[0];
                        Intent otp = new Intent(LoginActivity.this, VerifyotpActivity.class);
                        otp.putExtra("full number", full_number);
                        otp.putExtra("number",number);
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