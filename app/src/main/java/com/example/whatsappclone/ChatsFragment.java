package com.example.whatsappclone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<datamodel> dataholder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView = view.findViewById(R.id.Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder = new ArrayList<>();

        datamodel obj1 = new datamodel(R.drawable.ic_baseline_camera_alt_24,"Angular","Web Application","10:02");
        dataholder.add(obj1);

        datamodel obj2 = new datamodel(R.drawable.ic_baseline_camera_alt_24,"Angular","Web Application","10:02");
        dataholder.add(obj2);

        datamodel obj3 = new datamodel(R.drawable.ic_baseline_camera_alt_24,"Angular","Web Application","10:02");
        dataholder.add(obj3);

        datamodel obj4 = new datamodel(R.drawable.ic_baseline_camera_alt_24,"Angular","Web Application","10:02");
        dataholder.add(obj4);

        recyclerView.setAdapter(new myadapter(dataholder));
        return view;
    }
}
