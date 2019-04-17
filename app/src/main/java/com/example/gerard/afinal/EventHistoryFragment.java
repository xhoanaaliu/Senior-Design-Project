package com.example.gerard.afinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class EventHistoryFragment extends Fragment {
    private TextView textViewEventHistory;
    private ListView listView;
    private ArrayList<String> eventHistoryList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference datarefEvents;
    private ArrayList<String> eventFragmentArrayList = new ArrayList<>();
    private EventFragment evt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_event_history, container, false);
        evt = new EventFragment();
        textViewEventHistory = (TextView) rootView.findViewById(R.id.textViewEventHistory);
        listView = rootView.findViewById(R.id.listView);
        datarefEvents = FirebaseDatabase.getInstance().getReference("Event");
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,eventFragmentArrayList);
        listView.setAdapter(adapter);
        datarefEvents.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
             Map<String, String> value = (Map<String,String>) dataSnapshot.getValue();
             String item="";
             String category = value.get("category");
             String date = value.get("date");
             String description = value.get("description");
             String location = value.get("location");
             String time = value.get("time");
             String title = value.get("title");
             item = "Category : " + category + " " + "\nDate : " +  date + " " + "\nDescription : " +
                     description + " " + "\nLocation : " +  location + " " + "\nTime : " +  time + " " + "\nTitle : " + title;
             eventFragmentArrayList.add(item);
             adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootView;
    }



}