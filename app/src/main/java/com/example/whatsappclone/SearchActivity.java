package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<UsersModel> userList;
    private FirebaseUser firebaseUser;
    private Button search,back;
    private EditText edt;
    private String text;
    private int op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.search_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        userList = new ArrayList<>();
        search = findViewById(R.id.search_button);
        edt = findViewById(R.id.search_text);
        back = findViewById(R.id.back_button_search);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = edt.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                if(isNumeric(text))
                {
                    op = 0; // Denotes searching by number
                }
                else
                {
                    op = 1; // Denotes, searching by name
                }
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UsersModel users = snapshot1.getValue(UsersModel.class);
                            if(op == 0)
                            {
                                if(users.getNumber().equals(text))
                                {
                                    System.out.println(users.getNumber());
                                    System.out.println(text);
                                    userList.add(users);
                                }
                            }
                            else if(op == 1)
                            {
                                if(users.getName().toLowerCase().equals(text.toLowerCase()))
                                {
                                    userList.add(users);
                                }
                            }
                        }
                        recyclerView.setAdapter(new SearchAdapter(userList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public static boolean isNumeric(String string) {
        long intValue;

        System.out.println(String.format("Parsing string: \"%s\"", string));

        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    private boolean searchTimeModelList(TimeModel timeModel, List<TimeModel> timeModelList) {
        for(TimeModel time : timeModelList)
        {
            if(time == timeModel){return true;}
        }
        return false;
    }

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