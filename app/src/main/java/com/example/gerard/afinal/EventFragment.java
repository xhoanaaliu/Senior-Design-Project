package com.example.gerard.afinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.shapeofview.ShapeOfView;

public class EventFragment extends Fragment implements View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    private KenBurnsView poster;
    private TextView title, location, date, description;
    private ImageView googleLoc, calendarDate;
    private Button interestedButton, goingToButton;
    private ImageButton  f;

    public EventFragment() {}

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