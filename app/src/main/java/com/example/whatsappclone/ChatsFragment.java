package com.example.whatsappclone;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
1. When the user uses the app for the first time, the userList should be empty.
2. Then, the user should click on the search button, which should bring an edit text out(or a dialog). The user must type the name of the friend user.
   If the name typed matches the names of the users in the Users node of firebase, then show the users card with the same on click listeners.
3. If the friend user has a chat with the current user, then add the user to the userList and sort the user in the userList on the basis of the max time
*/


public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UsersModel> userList;
    private FirebaseUser firebaseUser;
    private List<String> time;
    private List<ChatsModel> chatsList;
    private List<TimeModel> timeModelList;
    private String maxTime,date;
    private String myId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView = view.findViewById(R.id.Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        time = new ArrayList<>();
        chatsList = new ArrayList<>();
        timeModelList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        
        timeModelList = getTimeList();
        System.out.println(timeModelList.size());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UsersModel users = snapshot1.getValue(UsersModel.class);
                    String friendId = users.getId();

                    if(!users.getId().equals(firebaseUser.getUid()))
                    {
                        for(TimeModel timeModel : timeModelList)
                        {
                            if(users.getId().equals(timeModel.getFriendId()) && timeModel.getMyId().equals(myId) || users.getId().equals(timeModel.getFriendId()) && timeModel.getMyId().equals(friendId))
                            {
                                userList.add(users);
                            }
                        }
                    }
                }

                myadapter adapter = new myadapter(userList);

                System.out.println(userList.size());
                reduceUserList(userList);
                System.out.println(userList.size());

                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    private List<TimeModel> getTimeList() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Time");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                for(DataSnapshot snapshot3 : snapshot2.getChildren())
                {
                    TimeModel timeModel = snapshot3.getValue(TimeModel.class);
                    timeModelList.add(timeModel);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return timeModelList;
    }

    private void reduceUserList(List<UsersModel> userList) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time");
        List<UsersModel> userList1 = new ArrayList<>();
        int op = 0;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    TimeModel timeModel = new TimeModel();
                    for(UsersModel user : userList)
                    {
                        if(timeModel.getFriendId().equals(user.getId()))
                        {
                            System.out.println("ReduceUserList : " +  timeModel.getFriendId());
                            flag = 1;
                            userList1.add(user);
                        }
                    }
                }

                if(flag == 1) {
                    updateReceiptsList(userList1,0);
                }
                else
                {
                    updateReceiptsList(userList1,1);
                }
                System.out.println(userList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateReceiptsList(List<UsersModel> newlist,int op) {
        userList.clear();
        if(op == 0) {
            userList = newlist;
        }

    }


}
