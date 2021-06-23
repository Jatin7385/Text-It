package com.example.whatsappclone;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class ChatsFragment extends Fragment {
    RecyclerView recyclerView;
    List<UsersModel> userList;
    FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView = view.findViewById(R.id.Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();

        String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UsersModel users = snapshot1.getValue(UsersModel.class);

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if(!users.getId().equals(firebaseUser.getUid()))
                    {
                        userList.add(users);
                    }
                }
                for(int i=0;i<userList.size();i++){
                    System.out.println(userList.get(i));
                }
                System.out.println("size : "+userList.size());

                recyclerView.setAdapter(new myadapter(userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
//
//    class Sortbytime implements Comparator<UsersModel>
//    {
//        // Used for sorting in ascending order of
//        // roll number
//        public int compare(Student a, Student b)
//        {
//            return a.rollno - b.rollno;
//        }
//
//        @Override
//        public int compare(UsersModel o1, UsersModel o2) {
//            return o2.get;
//        }
//    }
}
