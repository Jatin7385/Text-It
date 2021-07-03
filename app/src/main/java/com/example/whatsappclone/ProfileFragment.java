package com.example.whatsappclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private TextView profile_name;
    private CircleImageView imageView;
    FirebaseUser user;
    String myId;
    Uri uri;
    int SELECT_PICTURE = 200;
    int CAPTURE_IMAGE = 100;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        profile_name = view.findViewById(R.id.profile_name);
        imageView = view.findViewById(R.id.profile_pic);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myId = user.getUid();

        profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.update_name_dialog, null);
                Button back = view.findViewById(R.id.update_name_dismiss_button);
                Button update = view.findViewById(R.id.update_name_send_button);
                EditText text = view.findViewById(R.id.update_name_text);
                alert.setView(view);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name;
                        name = text.getText().toString().trim();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("name",name);
                        reference.updateChildren(hashMap);
                        profile_name.setText(name);
                        alertDialog.dismiss();
                    }
                });

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.image_option_dialog, null);
                Button camera = view.findViewById(R.id.camera_button);
                Button gallery = view.findViewById(R.id.gallery_button);
                Button dismiss = view.findViewById(R.id.dismiss_button);
                alert.setView(view);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Request for camera runtime permission
                        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{
                                    Manifest.permission.CAMERA
                            },100);
                        }

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);

                        alertDialog.dismiss();
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imageChooser();
                        alertDialog.dismiss();
                    }
                });

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    UsersModel users = snapshot1.getValue(UsersModel.class);
                    assert users != null;
                    if(users.getId().equals(user.getUid()))
                    {
                        profile_name.setText(users.getName());
                        Picasso.with(getContext()).load(users.getImageURL()).into(imageView);
                        System.out.println(users.getImageURL());
                        break;
                    }
                    else
                        {
                            System.out.print(users.getId() + "-" + user.getUid());
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
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

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();

        if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);

            String filepath = "Photos/" + "userprofile" + user.getUid();
            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            if (imageUri != null ) {
                // update the preview image in the layout
                imageView.setImageURI(imageUri);
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageURL = uri.toString();
                                if(imageURL == "null")
                                {
                                    Uri urii = Uri.parse("app/src/main/res/drawable-v24/profilepicc.png");
                                    imageURL = urii.toString();
                                }
                                System.out.println(imageURL);
                                hashMap.put("imageURL",imageURL);
                                reference1.updateChildren(hashMap);
                            }
                        });
                    }
                });

            }
        }

        else if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                uri = data.getData();
                String filepath = "Photos/" + "userprofile" + user.getUid();
                StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
                if (uri != null ) {
                    // update the preview image in the layout
                    imageView.setImageURI(uri);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();
                                    if(imageURL == "null")
                                    {
                                        Uri urii = Uri.parse("app/src/main/res/drawable-v24/profilepicc.png");
                                        imageURL = urii.toString();
                                    }
                                    System.out.println(imageURL);
                                    hashMap.put("imageURL",imageURL);
                                    reference1.updateChildren(hashMap);
                                }
                            });
                        }
                    });

                }
            }
        }
    }
}
