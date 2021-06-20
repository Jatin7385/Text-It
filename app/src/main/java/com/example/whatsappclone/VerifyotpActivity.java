package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyotpActivity extends AppCompatActivity {
    private EditText num1,num2,num3,num4,num5,num6;
    private Button next;
    TextView ph_num;
    String full_number,number;
    String otp,country_code;
    private static final String TAG = "VerifyotpActivity";
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp);

        getSupportActionBar().hide();

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        full_number = (String) b.get("full number");
        country_code = full_number.substring(0,3);
        number = (String) b.get("number");

        sendVerificationCode(country_code+number);

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


//        try {
//            Toast.makeText(VerifyotpActivity.this, "OTP has been sent", Toast.LENGTH_SHORT);
//
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberOtpMove();
                    if (!num1.getText().toString().isEmpty() && !num2.getText().toString().isEmpty() && !num3.getText().toString().isEmpty() && !num4.getText().toString().isEmpty() && !num5.getText().toString().isEmpty() && !num6.getText().toString().isEmpty()) {
                        otp = num1.getText().toString() + num2.getText().toString() + num3.getText().toString() + num4.getText().toString() + num5.getText().toString() + num6.getText().toString();
                        verifyCode(otp);
                    } else {
                        Toast.makeText(VerifyotpActivity.this, "Please fill all the boxes", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
//
//            if (!otp.isEmpty()) {
//                signInWithPhoneAuthCredential(credential);
//            } else {
//                return;
//            }
//        }catch (Exception e)
//        {
//            Toast.makeText(VerifyotpActivity.this,"Verification Code is wrong : "+e.toString(),Toast.LENGTH_SHORT).show();
//            System.out.println("Error : "+e.getLocalizedMessage().toString());
//        }
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                VerifyotpActivity.this, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(VerifyotpActivity.this, ProfileSetUpActivity.class);
                            i.putExtra("code",country_code);
                            i.putExtra("number",number);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(VerifyotpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(VerifyotpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void updateUI(FirebaseUser user) {
        Intent intent1 = new Intent(VerifyotpActivity.this, MainActivity.class);
        startActivity(intent1);
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