package com.example.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private TextView profile_name;
    private CircleImageView imageView;
    FirebaseUser user;
    String myId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);

        profile_name = view.findViewById(R.id.profile_name);
        imageView = view.findViewById(R.id.profile_pic);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myId = user.getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    UsersModel users = snapshot1.getValue(UsersModel.class);
                    if(users.getId() == myId)
                    {
                        System.out.println("Entered");
                        profile_name.setText(users.getName());
                        Picasso.with(getContext()).load(users.getImageURL()).into(imageView);
                        break;
                    }
                    else
                        {
                            System.out.println("NOT GETTING ID");
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
