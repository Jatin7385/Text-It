package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetUpActivity extends AppCompatActivity {
    private String TAG = "ProfileSetUpActivity";
    private Button next_button;
    private EditText profile_name;
    private CircleImageView profile_pic;
    int SELECT_PICTURE = 200;
    private Uri uri;
    private String s_uri;
    private String name;
    private DatabaseReference reference;
    private String number,country_code;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);

        next_button = findViewById(R.id.next_button_profile);
        profile_name = findViewById(R.id.profilename);
        profile_pic = findViewById(R.id.profilepic);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        country_code = (String) b.get("code");
        number = (String) b.get("number");

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = profile_name.getText().toString().trim();
                if(uri==null) {
                    uri = Uri.parse("app/src/main/res/drawable-v24/profilepicc.png");
                }
                updateUserProfile(name, uri);
                Intent intent = new Intent(ProfileSetUpActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void updateUserProfile(String name, Uri uri) {

        String s_name = name;
        s_uri = uri.toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",id);
            hashMap.put("name", s_name);
            hashMap.put("uri", s_uri);
            hashMap.put("country_code", country_code);
            hashMap.put("number", number);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileSetUpActivity.this,"Profile Created",Toast.LENGTH_SHORT);
            }
        });


    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                uri = data.getData();
                if (null != uri) {
                    // update the preview image in the layout
                    profile_pic.setImageURI(uri);
                }
            }
        }
    }
}