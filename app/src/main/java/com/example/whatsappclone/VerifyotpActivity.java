package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyotpActivity extends AppCompatActivity {
    private EditText num1,num2,num3,num4,num5,num6;
    private Button next;
    TextView ph_num;
    String number;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        Intent intent = getIntent();

        number = intent.getExtras().toString();

        num1 = findViewById(R.id.inputotp1);
        num2 = findViewById(R.id.inputotp2);
        num3 = findViewById(R.id.inputotp3);
        num4 = findViewById(R.id.inputotp4);
        num5 = findViewById(R.id.inputotp5);
        num6 = findViewById(R.id.inputotp6);
        ph_num = findViewById(R.id.ph_num);

        ph_num.setText(number);

        next = findViewById(R.id.next_button_otp);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!num1.getText().toString().isEmpty() && !num2.getText().toString().isEmpty() && !num3.getText().toString().isEmpty() && !num4.getText().toString().isEmpty() && !num5.getText().toString().isEmpty() && !num6.getText().toString().isEmpty()) {
                    otp = num1.getText().toString() + num2.getText().toString() + num3.getText().toString() + num4.getText().toString() + num5.getText().toString() + num6.getText().toString();
                }
                else
                    {
                        Toast.makeText(VerifyotpActivity.this,"Please fill all the boxes",Toast.LENGTH_SHORT).show();
                    }
            }
        });

        numberOtpMove();
    }

    private void numberOtpMove() {
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    num1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    num2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    num3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    num4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        num5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    num5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}