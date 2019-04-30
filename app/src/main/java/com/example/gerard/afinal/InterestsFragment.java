package com.example.gerard.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class InterestsFragment extends Fragment {

    private TextView textViewInterests;
    private DatabaseReference reff;
    private ListView listViewInterests;
    private ArrayList<String> interestList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference refInterests;
    FirebaseUser user;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_interests, container, false);
        textViewInterests = (TextView) rootView.findViewById(R.id.textViewFollowing);
        listViewInterests = rootView.findViewById(R.id.listViewInterests);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,interestList);
        listViewInterests.setAdapter(adapter);
        refInterests = FirebaseDatabase.getInstance().getReference("Interested");
        Query q1 = refInterests.orderByChild("user_id").equalTo(userID);
        q1.addChildEventListener(new ChildEventListener() {
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
                interestList.add(item);
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