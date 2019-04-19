package com.example.gerard.afinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.shapeofview.ShapeOfView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventFragment extends Fragment implements View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    public KenBurnsView poster;
    private TextView title, location, date, description;
    private ImageView googleLoc, calendarDate;
    private Button interestedButton, goingToButton;
    private ImageButton  f;
    private String event_id;

    private static ArrayList<String> events_retrieved;
    DatabaseReference databaseReference;

    public EventFragment() {

    }
    /*public EventFragment(String event_id){
        this.event_id=event_id;
    }
    */
    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }
    public void setEvent_id(String event_id){
        this.event_id=event_id;
    }
    public String getEvent_id(){
        return event_id;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getLocation() {
        return location;
    }

    public void setLocation(TextView location) {
        this.location = location;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize
        title = view.findViewById(R.id.eventTitle);
        location = view.findViewById(R.id.location);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        poster = view.findViewById(R.id.eventPoster);
        interestedButton = view.findViewById(R.id.button);
        goingToButton = view.findViewById(R.id.button2);
        googleLoc = view.findViewById(R.id.googleLocation);
        calendarDate = view.findViewById(R.id.calendarDate);
        googleLoc.setOnClickListener(this);
        calendarDate.setOnClickListener(this);
        goingToButton.setOnClickListener(this);
        interestedButton.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Event/event1");
        events_retrieved = new ArrayList<String>();

    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.googleLocation:
                //ads
            case R.id.calendarDate:
                //adas
            case R.id.button:
                //sds
            case R.id.button2:
                //sad
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        //setCurrentFragmentName("Users");
        //  EventBus.getDefault().post(new GoToHomeScreenEvent(currentFragmentName, "Users", false));
    }

    @Override
    public void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                System.out.print(value);
                //Log.i("lllll", value);
                events_retrieved.add(value);
                System.out.print(events_retrieved.size());
                //String title_retrieved =  events_retrieved.get(0);
                /*for (int i = 0; i < events_retrieved.size(); i++)
                {
                    Log.i("lllll", i+" "+events_retrieved.get(i).toString());
                }
                */
                for (int i = 0; i < events_retrieved.size(); i++)
                {
                    Log.i("lllll", "again"+i+" "+events_retrieved.get(i).toString());
                    if(events_retrieved.size()==3){
                        String title_retrieved = events_retrieved.get(0);
                        String location_retrieved = events_retrieved.get(1);
                        String date_retrieved = events_retrieved.get(2);
                        title.setText(title_retrieved);
                        location.setText(location_retrieved);
                        date.setText(date_retrieved);
                    }
                }
                //Map<String, String> map = dataSnapshot.getValue(Map.class);
                //String title_retrieved =  map.get("title");
                //String location_retrieved = map.get("location");
                //String date_retrieved = map.get("date");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //setEvent();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("lllll", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        // EventBus.getDefault().unregister(this);
        super.onStop();
    }

   /* public void setCurrentFragmentName(String currentFragmentName){
        this.currentFragmentName = currentFragmentName;
    }*/

}