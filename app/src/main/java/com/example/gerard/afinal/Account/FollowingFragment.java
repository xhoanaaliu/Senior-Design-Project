package com.example.gerard.afinal.Account;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gerard.afinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FollowingFragment extends Fragment {
    private TextView textViewFollowing;
    private DatabaseReference reff;
    private ListView listViewFollow;
    private ArrayList<String> followList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_following, container, false);
        textViewFollowing = (TextView) rootView.findViewById(R.id.textViewFollowing);
        listViewFollow = rootView.findViewById(R.id.listViewFollow);
        reff = FirebaseDatabase.getInstance().getReference().child("Follow").child("follow 1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String follow= dataSnapshot.child("email_organization").getValue().toString();
                followList.add(follow);
                adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,followList);
                listViewFollow.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }


}