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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetUpActivity extends AppCompatActivity {
    private String TAG = "ProfileSetUpActivity";
    private Button next_button;
    private EditText profile_name;
    private CircleImageView profile_pic;
    FirebaseUser user;
    int SELECT_PICTURE = 200;
    private Uri uri;
    private String imageURL = "default";
    private String name;
    private DatabaseReference reference;
    private String number,country_code;
    private String id;
    private HashMap<String, Object> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_set_up);

        getSupportActionBar().hide();

        user = FirebaseAuth.getInstance().getCurrentUser();

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
                updateUserProfile(name);
                Intent intent = new Intent(ProfileSetUpActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void updateUserProfile(String name) {

        String s_name = name;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();
        hashMap = new HashMap<>();
            hashMap.put("id",id);
            hashMap.put("name", s_name);
            hashMap.put("imageURL",imageURL);
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
                String filepath = "Photos/" + "userprofile" + user.getUid();
                StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
                if (uri != null ) {
                    // update the preview image in the layout
                    profile_pic.setImageURI(uri);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageURL = uri.toString();
                                    if(imageURL == "null")
                                    {
                                        Uri urii = Uri.parse("app/src/main/res/drawable-v24/profilepicc.png");
                                        imageURL = urii.toString();
                                    }
                                    System.out.println(imageURL);
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
//                                    HashMap<String,Object> hashMap1 = new HashMap<>();
//                                    hashMap1.put("imageURL",imageUrl);
//                                    databaseReference.updateChildren(hashMap1);
                                }
                            });
                        }
                    });

                }
            }
        }
    }
}