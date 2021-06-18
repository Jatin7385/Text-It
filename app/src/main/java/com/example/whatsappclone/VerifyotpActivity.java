package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyotpActivity extends AppCompatActivity {
    private EditText num1,num2,num3,num4,num5,num6;
    private Button next;
    TextView ph_num;
    String full_number,number;
    String otp,country_code;
    private static final String TAG = "VerifyotpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        Intent intent = getIntent();

        //number = intent.getExtras().toString();
        Bundle b = intent.getExtras();
        full_number = (String) b.get("full number");
        country_code = full_number.substring(0,3);
        number = (String) b.get("number");

        num1 = findViewById(R.id.inputotp1);
        num2 = findViewById(R.id.inputotp2);
        num3 = findViewById(R.id.inputotp3);
        num4 = findViewById(R.id.inputotp4);
        num5 = findViewById(R.id.inputotp5);
        num6 = findViewById(R.id.inputotp6);
        ph_num = findViewById(R.id.ph_num);

        ph_num.setText(full_number);

        next = findViewById(R.id.next_button_otp);

        //numberOtpMove is not working properly
        numberOtpMove();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(country_code + number,
                60,
                TimeUnit.SECONDS,
                VerifyotpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInUser(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d(TAG, "onVerificationFailed:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        try {
                            Toast.makeText(VerifyotpActivity.this, "OTP has been sent", Toast.LENGTH_SHORT);

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    numberOtpMove();
                                    if (!num1.getText().toString().isEmpty() && !num2.getText().toString().isEmpty() && !num3.getText().toString().isEmpty() && !num4.getText().toString().isEmpty() && !num5.getText().toString().isEmpty() && !num6.getText().toString().isEmpty()) {
                                        otp = num1.getText().toString() + num2.getText().toString() + num3.getText().toString() + num4.getText().toString() + num5.getText().toString() + num6.getText().toString();
                                    } else {
                                        Toast.makeText(VerifyotpActivity.this, "Please fill all the boxes", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

                            if (!otp.isEmpty()) {
                                signInUser(credential);
                            } else {
                                return;
                            }
                        }catch (Exception e)
                        {
                            Toast.makeText(VerifyotpActivity.this,"Verification Code is wrong : "+e.toString(),Toast.LENGTH_SHORT).show();
                            System.out.println("Error : "+e.getLocalizedMessage().toString());
                        }

                    }

                });
    }


    private void signInUser(PhoneAuthCredential credential){
        FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent1 = new Intent(VerifyotpActivity.this, MainActivity.class);
                    startActivity(intent1);
                }else
                    {
                        Log.d(TAG,"onComplete:"+task.getException().getLocalizedMessage());
                    }
            }
        });
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