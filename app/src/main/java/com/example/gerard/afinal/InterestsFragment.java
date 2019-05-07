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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class InterestsFragment extends Fragment {

    private TextView textViewInterests;
    private DatabaseReference reff;
    private ListView listViewInterests;
    MyCustomListAdapter adapter;
    ArrayList<EventInfo> eventInterestArrayList  = new ArrayList<>();

    private DatabaseReference refInterests;
    private DatabaseReference datarefEvents;
    private StorageReference mStorageRef;
    FirebaseUser user;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView= inflater.inflate(R.layout.fragment_interests, container, false);
        textViewInterests = (TextView) rootView.findViewById(R.id.textViewFollowing);
        listViewInterests = rootView.findViewById(R.id.listViewInterests);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        datarefEvents = FirebaseDatabase.getInstance().getReference("Event");
        refInterests = FirebaseDatabase.getInstance().getReference();
        listViewInterests = rootView.findViewById(R.id.listViewInterests);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        refInterests.child("InterestedIn").child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                datarefEvents.orderByChild("user_id").equalTo(userID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String key2 = dataSnapshot.getKey();
                        if(key2.equals(key)){
                            adapter = new MyCustomListAdapter(getContext(),R.layout.display_event,eventInterestArrayList);
                            listViewInterests.setAdapter(adapter);
                            Map<String, String> value = (Map<String,String>) dataSnapshot.getValue();
                            String category = value.get("category");
                            String date = value.get("date");
                            String description = value.get("description");
                            String location = value.get("location");
                            String time = value.get("time");
                            String title = value.get("title");
                            String url = value.get("imageName");

                            EventInfo e1 = new EventInfo(category,date,description,url,location,time,title);
                            eventInterestArrayList.add(e1);
                            adapter.notifyDataSetChanged();
                        }
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