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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UsersModel> userList;
    private FirebaseUser firebaseUser;
    private List<String> time;
    private List<ChatsModel> chatsList;
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                myId = firebaseUser.getUid();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UsersModel users = snapshot1.getValue(UsersModel.class);
                    String friendId = users.getId();

                    if(!users.getId().equals(firebaseUser.getUid()))
                    {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats");
                        chatsList.clear();
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    ChatsModel chats = snapshot1.getValue(ChatsModel.class);
                                    if (chats.getSender().equals(myId) && chats.getReceiver().equals(friendId) || chats.getSender().equals(friendId) && chats.getReceiver().equals(myId)) {
                                        chatsList.add(chats);
                                    }
                                }

                                try {
                                    String arr[] = getMaxTime(chatsList);
                                    date = arr[0];
                                    maxTime = arr[1];
                                    String op = arr[2];

                                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("myId", myId);
                                    hashMap.put("friendId", friendId);
                                    hashMap.put("time", maxTime);
                                    hashMap.put("date", date);
                                    hashMap.put("option", op);
                                    reference2.child("Time").push().setValue(hashMap);
                                }catch (Exception e)
                                {
                                    System.out.println("Error : " + e.getLocalizedMessage().toString());
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        userList.add(users);

                    }
                }
//                userList = sortUserList(userList);
                recyclerView.setAdapter(new myadapter(userList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

//    private List<UsersModel> sortUserList(List<UsersModel> userList) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time");
//        int n = userList.size();
//        List<TimeModel> timeModelList = new ArrayList<>();
//        List<TimeModel> timeModelList_order = new ArrayList<>();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                        TimeModel timeModel = snapshot1.getValue(TimeModel.class);
//                        timeModelList.add(timeModel);
//                    }
//                }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        //Putting the timeModelList in the same order as the userlist, to make it easier to sort it
//        for(int i=0;i<n;i++)
//        {
//            for(int j = 0;j<timeModelList.size();j++)
//            {
//                if(userList.get(i).getId().equals(timeModelList.get(j).getFriendId()))
//                {
//                    timeModelList_order.add(timeModelList.get(j));
//                    break;
//                }
//            }
//        }
//
//        UsersModel temp = null;
//        for(int i=0; i < n; i++){
//            for(int j=1; j < (n-i); j++){
//                if(timeModelList_order.get(j-1).getDate().compareTo(timeModelList.get(j).getDate()) >= 0 && timeModelList_order.get(j-1).getTime().compareTo(timeModelList.get(j).getTime()) >=0)
//                    //swap elements
//                    temp = userList.get(j-1);
//                    userList.add(j-1, userList.get(j));
//                    userList.add(j,temp);
//                }
//            }
//
//        return userList;
//        }

    private String[] getMaxTime(List<ChatsModel> chatsList) {
        String maxTime = chatsList.get(0).getTime();
        String maxdate = chatsList.get(0).getDate();
        String date = chatsList.get(0).getDate();
        String time = chatsList.get(0).getTime();
        for(int i=1;i<chatsList.size();i++)
        {
            if(chatsList.get(i).getTime().compareTo(maxTime) > 0)
            {
                maxTime = chatsList.get(i).getTime();
                date = chatsList.get(i).getDate();
            }

            if(chatsList.get(i).getDate().compareTo(maxdate) > 0)
            {
                maxdate = chatsList.get(i).getDate();
                time = chatsList.get(i).getTime();
            }
            System.out.println("OUT : " + maxTime + " , " + date + " , " + time + " , " + maxdate);
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

        String arr[] = new String[3];

        //0 denotes that the date of the latest message is not the date of that day, so display the date instead of time.
        //1 denotes that the date of the latest message is the date of that day, so display the time.

        if(date.compareTo(currentDate) != 0)
        {
            arr[0] = maxdate;
            arr[1] = time;
            arr[2] = "0";
        }
        else
        {
            arr[0] = date;
            arr[1] = maxTime;
            arr[2] = "1";
        }
        return arr;
    }
}
