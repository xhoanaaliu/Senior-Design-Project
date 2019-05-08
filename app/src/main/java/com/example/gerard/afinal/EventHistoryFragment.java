package com.example.gerard.afinal;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gerard.afinal.Login_SignUp.LoginFragment;
import com.example.gerard.afinal.Login_SignUp.NormalUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static com.facebook.FacebookSdk.getApplicationContext;


public class EventHistoryFragment extends Fragment {
    private TextView textViewEventHistory;
   ListView listView;
    MyCustomListAdapter adapter;
    private DatabaseReference datarefEvents;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
     ArrayList<EventInfo> eventFragmentArrayList  = new ArrayList<>();
    private EventFragment evt;
    private DatabaseReference ref2;
    private String eventTitle;

    FirebaseUser user;
    private String userID;
    ImageView imageViewEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView= inflater.inflate(R.layout.fragment_event_history, container, false);
        ref2 = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        datarefEvents = FirebaseDatabase.getInstance().getReference("Event");
        userID=user.getUid();//userID from firebaseAuth
        listView = rootView.findViewById(R.id.listView);
        imageViewEvent = rootView.findViewById(R.id.imageViewEvent);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Checks if the userID that exists in GoingTo table, also exists in event table.
        //By checking the eventID's, retrieves information from Event table.
      ref2.child("GoingTo").child(userID).addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              final String key = dataSnapshot.getKey();
              Toast.makeText(getContext(),"j", Toast.LENGTH_SHORT).show();
              datarefEvents.orderByChild("user_id").equalTo(userID).addChildEventListener(new ChildEventListener() {
                  @Override
                  public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                      String key2 = dataSnapshot.getKey();
                      if(key2.equals(key)){
                          Toast.makeText(getContext(),key,Toast.LENGTH_SHORT).show();
                          adapter = new MyCustomListAdapter(getContext(),R.layout.display_event,eventFragmentArrayList);
                          listView.setAdapter(adapter);
                          Map<String, String> value = (Map<String,String>) dataSnapshot.getValue();
                          String category = value.get("category");
                          String date = value.get("date");
                          String description = value.get("description");
                          String location = value.get("location");
                          String time = value.get("time");
                          String title = value.get("title");
                          String url = value.get("imageName");

                          EventInfo e1 = new EventInfo(category,date,description,url,location,time,title);
                          eventFragmentArrayList.add(e1);
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


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}