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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InterestsFragment extends Fragment {

    private TextView textViewInterests;
    private DatabaseReference reff;
    private ListView listViewInterests;
    private ArrayList<String> followListInterest = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_interests, container, false);
        textViewInterests = (TextView) rootView.findViewById(R.id.textViewFollowing);
        listViewInterests = rootView.findViewById(R.id.listViewInterests);
        reff = FirebaseDatabase.getInstance().getReference().child("Interest").child("interest 1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String interest= dataSnapshot.child("ineterest_name").getValue().toString();
                followListInterest.add(interest);
                adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,followListInterest);
                listViewInterests.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}