package com.example.whatsappclone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.network.ListNetworkRequest;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
//ADDED TIME HERE
//Add an imageUrl part in the firebase database for Chats as well
public class ChatScreenActivity extends AppCompatActivity {
    private Button back;
    private FloatingActionButton send, imageButton;
    private EditText text;
    private String message;
    private String name, url, friendId, myId, time;
    int SELECT_PICTURE = 200;
    private TextView textView_name;
    private CircleImageView profilepic;
    private FirebaseUser user;

    private List<ChatsModel> chatsList;
    private ChatsAdapter adapter;
    private RecyclerView recView;
    private ProgressBar progressBar;
    private int imageCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_chat_screen);

        getSupportActionBar().hide();

        user = FirebaseAuth.getInstance().getCurrentUser();

        time = new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());

        back = findViewById(R.id.back_button);
        send = findViewById(R.id.sendButton);
        text = findViewById(R.id.text);
        textView_name = findViewById(R.id.name);
        profilepic = findViewById(R.id.profilepic);
        imageButton = findViewById(R.id.imageButton);
        progressBar = findViewById(R.id.progress_bar_chat);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        recView = findViewById(R.id.chats_recView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recView.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        name = (String) b.get("name");
        url = (String) b.get("url");
        friendId = (String) b.get("friendId");
        myId = user.getUid();

        textView_name.setText(name);

        if (url.equals("default")) {
            String uri = "@drawable/profilepicc";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            profilepic.setImageDrawable(res);
        } else {
            Picasso.with(ChatScreenActivity.this).load(url).into(profilepic);
        }

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
                sendMessage(myId, friendId, message, "null");
                System.out.println(message);
                text.setText("");
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() > 0) {
                    send.setEnabled(true);
                } else {
                    send.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readMessages(myId, friendId);
    }

    private void readMessages(String myId, String friendId) {
        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatsModel chats = snapshot1.getValue(ChatsModel.class);
                    if (chats.getSender().equals(myId) && chats.getReceiver().equals(friendId) || chats.getSender().equals(friendId) && chats.getReceiver().equals(myId)) {
                        chatsList.add(chats);
                    }

                    adapter = new ChatsAdapter(ChatScreenActivity.this, chatsList);
                    recView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String myId, String friendId, String message, String uri) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myId);
        hashMap.put("receiver", friendId);
        hashMap.put("message", message);
        hashMap.put("imageUrl", uri);
        //hashMap.put("time",time);
        databaseReference.child("Chats").push().setValue(hashMap);
    }

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            final String[] imageUrl = new String[1];
            Button imagePreviewSend;
            ImageView previewImage;
            EditText image_text;
            final AlertDialog.Builder alert = new AlertDialog.Builder(ChatScreenActivity.this);
            View view = getLayoutInflater().inflate(R.layout.image_preview_dialog, null);
            imagePreviewSend = view.findViewById(R.id.preview_button);
            previewImage = view.findViewById(R.id.image_preview);
            image_text = view.findViewById(R.id.image_text);
            alert.setView(view);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    progressBar.setVisibility(View.VISIBLE);
                    System.out.println(selectedImageUri.toString());
                    while (selectedImageUri == null) {
                        continue;
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    //previewImage.setImageURI(selectedImageUri);
                    Picasso.with(ChatScreenActivity.this).load(selectedImageUri.toString()).into(previewImage);

                    String filepath = "Photos/" + "chatImages" + user.getUid() + String.valueOf(imageCount);
                    imageCount++;
                    StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
                    if (selectedImageUri != null) {
                        // update the preview image in the layout
                        reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl[0] = uri.toString();
                                        System.out.println("Photo added in Storage");
                                    }
                                });
                            }
                        });

                        imagePreviewSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String message = image_text.getText().toString().trim();
                                alertDialog.dismiss();
                                sendMessage(myId, friendId, message, imageUrl[0]);
                            }
                        });
                    }
                }
            }
        }
    }
}
