package com.example.gerard.afinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.facebook.FacebookSdk.getApplicationContext;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;

public class EventFragment extends Fragment implements View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    public KenBurnsView poster;
    private TextView title, location, date, description,times, category, eventId;
    private ImageView googleLoc, calendarDate,buttonTime;
    private Button interestedButton, goingToButton;

    private boolean isGoingToButtonClicked = false;
    private boolean isInterestedInButtonClicked = false;

    private String event_id;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    String userID;
    FirebaseUser user;
    private static ArrayList<String> events_retrieved;
    DatabaseReference databaseReference,userRef;
    DatabaseReference myRef;
    DatabaseReference interestRef;
    DatabaseReference eventRef;
    private ArrayList<EventInfo> eventsArrayList;
    private Bitmap bitmap;
    String imageURL;
    Hashtable listCalendar;

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
    public TextView getTime() {
        return times;
    }

    public void setTime(TextView times) {
        this.times = times;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize
        title = view.findViewById(R.id.eventTitle);
        location = view.findViewById(R.id.location);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        times = view.findViewById(R.id.times);
        poster = view.findViewById(R.id.eventPoster);
        goingToButton = view.findViewById(R.id.goingTo);
        interestedButton = view.findViewById(R.id.interestedIn);
        googleLoc = view.findViewById(R.id.googleLocation);
        calendarDate = view.findViewById(R.id.calendarDate);
        buttonTime=view.findViewById(R.id.buttonTime);
        category = view.findViewById(R.id.category);
        eventId = view.findViewById(R.id.categoryID);

        googleLoc.setOnClickListener(this);
        calendarDate.setOnClickListener(this);
        buttonTime.setOnClickListener(this);
        goingToButton.setOnClickListener(this);
        interestedButton.setOnClickListener(this);
        poster.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        eventsArrayList = new ArrayList<>();
        eventRef=FirebaseDatabase.getInstance().getReference("Event");
        myRef = FirebaseDatabase.getInstance().getReference("GoingTo");
        interestRef = FirebaseDatabase.getInstance().getReference("Interested");
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Event/event1");
        events_retrieved = new ArrayList<String>();

        haveCalendarReadWritePermissions(getActivity());
        requestCalendarReadWritePermission(getActivity());
        listCalendar = new Hashtable();
//        listCalendar = listCalendarId(getApplicationContext());

    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.googleLocation:
                //ads
                break;
            case R.id.calendarDate:
                //adas
                break;
            //case R.id.goingTo:
                //sds
            //case R.id.interestedIn:
                //sad
            case R.id.eventPoster:
                /*AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.eventPoster);
                //Glide.with(getApplicationContext()).load(imageURL).into(photoView);
               // photoView.setImageBitmap(bitmapimage);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();*/
                break;

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
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Bundle bundle = this.getArguments();
        if(bundle != null){
            title.setText(bundle.getString("title"));
            location.setText(bundle.getString("location"));
            date.setText(bundle.getString("date"));
            description.setText(bundle.getString("description"));
            times.setText(bundle.getString("time"));
            category.setText(bundle.getString("category"));
            eventId.setText(bundle.getString("ID"));

            String url = bundle.getString("url");
            final StorageReference islandRef = mStorageRef.child(url);

            islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //String imageURL = uri.toString();
                    imageURL = uri.toString();
                    Glide.with(getApplicationContext()).load(imageURL).into(poster);
                    //bitmap = Glide.with(getApplicationContext()).load(imageURL).into(poster);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            userRef = FirebaseDatabase.getInstance().getReference("GoingTo");
            userRef.orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    goingToButton.setBackgroundResource(R.color.colorPrimary);
                                    goingToButton.setTextColor(getResources().getColor(R.color.white));
                                    isGoingToButtonClicked = true;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        //Toast.makeText(getApplicationContext(),"Your user id is on going to table",Toast.LENGTH_SHORT).show();
                        interestRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    interestedButton.setBackgroundResource(R.color.colorPrimary);
                                    interestedButton.setTextColor(getResources().getColor(R.color.white));
                                    isInterestedInButtonClicked = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                /*if (dataSnapshot.getValue() != null) {
                                    //Toast.makeText(getApplicationContext(),"You are already going to this event",Toast.LENGTH_SHORT).show();
                                    goingToButton.setBackgroundResource(R.color.colorPrimary);
                                    goingToButton.setTextColor(getResources().getColor(R.color.white));
                                    isGoingToButtonClicked = true;
                                }else{

                                }*/
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        //Toast.makeText(getApplicationContext(),"WHatatattata",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}});

            goingToButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isGoingToButtonClicked = !isGoingToButtonClicked; // toggle the boolean flag

                    calendarEvent(isGoingToButtonClicked);

                    if (isGoingToButtonClicked == true && isInterestedInButtonClicked == false) {
                        v.setBackgroundResource(R.color.colorPrimary);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));

                        removeInterestedIn();
                        checkIfGoingToNew();

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == false){
                        v.setBackgroundColor(Color.TRANSPARENT);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        removeInterestedIn();
                        removeGoingTo();

                    }else if(isGoingToButtonClicked == true && isInterestedInButtonClicked == true) {
                        checkIfGoingToNew();
                        removeInterestedIn();

                        v.setBackgroundResource(R.color.colorPrimary);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));

                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        interestedButton.setBackgroundColor(Color.TRANSPARENT);
                        isInterestedInButtonClicked = false;

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == true) {
                        v.setBackgroundColor(Color.TRANSPARENT);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeGoingTo();
                    }
                }
            });
            interestedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInterestedInButtonClicked = !isInterestedInButtonClicked; // toggle the boolean flag
                    //setFocus(btn_unfocus, btn[1]);
                    if (isInterestedInButtonClicked == true&& isGoingToButtonClicked == false) {
                        v.setBackgroundResource(R.color.colorPrimary);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfInterestedInNew();
                        removeGoingTo();

                    }else if ((isInterestedInButtonClicked == false && isGoingToButtonClicked == false)|| (isInterestedInButtonClicked == false && isGoingToButtonClicked == true)){
                        //setFocus(btn_unfocus, btn[1]);
                        v.setBackgroundColor(Color.TRANSPARENT);
                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeInterestedIn();
                        removeGoingTo();

                    }else if(isInterestedInButtonClicked == true && isGoingToButtonClicked == true) {
                        v.setBackgroundResource(R.color.colorPrimary);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfInterestedInNew();
                        removeGoingTo();

                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        goingToButton.setBackgroundColor(Color.TRANSPARENT);
                        isGoingToButtonClicked = false;
                    }
                }
            });

        }
        else {
            // EventBus.getDefault().register(this);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String value = dataSnapshot.getValue(String.class);
                    System.out.print(value);
                    events_retrieved.add(value);
                    System.out.print(events_retrieved.size());

                    for (int i = 0; i < events_retrieved.size(); i++) {
                        Log.i("lllll", "again" + i + " " + events_retrieved.get(i).toString());
                        if (events_retrieved.size() == 4) {
                            String title_retrieved = events_retrieved.get(0);
                            String location_retrieved = events_retrieved.get(1);
                            String date_retrieved = events_retrieved.get(2);
                            String time_retrieved = events_retrieved.get(3);
                            title.setText(title_retrieved);
                            location.setText(location_retrieved);
                            date.setText(date_retrieved);
                            times.setText(time_retrieved);
                        }
                    }
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
    }

    @Override
    public void onStop() {
        // EventBus.getDefault().unregister(this);
        super.onStop();
    }

   /* public void setCurrentFragmentName(String currentFragmentName){
        this.currentFragmentName = currentFragmentName;
    }*/
   public void checkIfGoingToNew(){
       userRef = FirebaseDatabase.getInstance().getReference("GoingTo");
       userRef.orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.getValue() != null) {
                   userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.getValue() != null) {
                               Toast.makeText(getApplicationContext(),"You are already going to this event",Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                               EventInfo e1 = new EventInfo();
                               e1.setEvent_id(eventId.getText().toString());
                               String value = e1.getEvent_id();
                               Map<String, String> goingTo = new HashMap<>();
                               goingTo.put("eventID",value);
                               goingTo.put("user_id",userID);
                               DatabaseReference db = myRef.push();
                               db.setValue(goingTo);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {}});
                   //Toast.makeText(getApplicationContext(),"Your user id is on going to table",Toast.LENGTH_SHORT).show();
               } else {
                   userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.getValue() != null) {
                               Toast.makeText(getApplicationContext(),"You are already going to this event",Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                               EventInfo e1 = new EventInfo();
                               e1.setEvent_id(eventId.getText().toString());
                               String value = e1.getEvent_id();
                               Map<String, String> goingTo = new HashMap<>();
                               goingTo.put("eventID",value);
                               goingTo.put("user_id",userID);
                               DatabaseReference db = myRef.push();
                               db.setValue(goingTo);
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {}});
                   //Toast.makeText(getApplicationContext(),"WHatatattata",Toast.LENGTH_SHORT).show();
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {}});
   }

    public void removeGoingTo(){
        eventRef =  FirebaseDatabase.getInstance().getReference();
        eventRef.child("GoingTo").orderByChild("user_id").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                eventRef = FirebaseDatabase.getInstance().getReference();
                eventRef.child("GoingTo").orderByChild("eventID").equalTo(eventId.getText().toString()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (isGoingToButtonClicked == false){
                            dataSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void checkIfInterestedInNew(){
        userRef = FirebaseDatabase.getInstance().getReference("Interested");
        userRef.orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You are already interested in this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                EventInfo e2 = new EventInfo();
                                e2.setEvent_id(eventId.getText().toString());
                                String value = e2.getEvent_id();
                                Map<String, String> interested = new HashMap<>();
                                interested.put("eventID",value);
                                interested.put("user_id",userID);
                                DatabaseReference db2 = interestRef.push();
                                db2.setValue(interested);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}});
                    //Toast.makeText(getApplicationContext(),"You are already in interested in table",Toast.LENGTH_SHORT).show();
                } else {
                    userRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You are already interested in this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                EventInfo e2 = new EventInfo();
                                e2.setEvent_id(eventId.getText().toString());
                                String value = e2.getEvent_id();
                                Map<String, String> interested = new HashMap<>();
                                interested.put("eventID",value);
                                interested.put("user_id",userID);
                                DatabaseReference db2 = interestRef.push();
                                db2.setValue(interested);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}});
                    //Toast.makeText(getApplicationContext(),"WHatatattata",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }
    public void removeInterestedIn(){
        eventRef =  FirebaseDatabase.getInstance().getReference();
        eventRef.child("Interested").orderByChild("user_id").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                eventRef = FirebaseDatabase.getInstance().getReference();
                eventRef.child("Interested").orderByChild("eventID").equalTo(eventId.getText().toString()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (isInterestedInButtonClicked == false){
                            dataSnapshot.getRef().setValue(null);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void calendarEvent(Boolean b){
        if (b){
            float longi = 10;
           /* MakeNewCalendarEntry(getActivity(),title.getText().toString(),description.getText().toString(),
                    location.getText().toString(),Long.parseLong(times.getText().toString()),Long.parseLong(times.getText().toString()),
                    true,true, 1,1);
            MakeNewCalendarEntry(getActivity(),title.getText().toString(),description.getText().toString(),
                    location.getText().toString(),longi,longi+1,
                    true,true, 1,1);*/
            //addReminder(2019, 5, 5, 18, 0,2019,5, 5, 18, 30);
            String dateInputS = date.getText().toString();
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd-MM-yyyy").parse(dateInputS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat year = new SimpleDateFormat("yyyy");
            DateFormat month = new SimpleDateFormat("MM");
            DateFormat day = new SimpleDateFormat("dd");
            String outputYear = year.format(dateInput);
            String outputMonth = month.format(dateInput);
            String outputDay = day.format(dateInput);

            long startTime = getLongAsDate(Integer.parseInt(outputYear), Integer.parseInt(outputMonth), Integer.parseInt(outputDay));
            long endTime = getLongAsDate(Integer.parseInt(outputYear), Integer.parseInt(outputMonth), Integer.parseInt(outputDay)+1);
            //contentEvent.put("dtstart", startTime);
            //contentEvent.put("dtend", endTime);
            ContentResolver cr = getContext().getContentResolver();
        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, "Reminder to " + title.getText().toString());
        calEvent.put(CalendarContract.Events.DTSTART, startTime);
        calEvent.put(CalendarContract.Events.DTEND, endTime);
        calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);
        //calEvent.put(CalendarContract.EventDays.STARTDAY,"2458608.50000");

        //save an event
        final Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);

        int dbId = Integer.parseInt(uri.getLastPathSegment());

        //Now create a reminder and attach to the reminder
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID, dbId);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_DEFAULT);
        reminders.put(CalendarContract.Reminders.MINUTES, 0);

        final Uri reminder = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

        int added = Integer.parseInt(reminder.getLastPathSegment());
        //this means reminder is added
        if(added > 0) {
            Intent view = new Intent(Intent.ACTION_VIEW);
            view.setData(uri); // enter the uri of the event not the reminder

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
                view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        |Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        |Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            }
            else {
                view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            }
            //view the event in calendar
            startActivity(view);
            //Calendar cl = Calendar.getInstance();
           //cl.setTimeInMillis(milliseconds);  //here your time in miliseconds
            //String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.YEAR);
        }
        }
    }
    public long getLongAsDate(int year, int month, int date) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }
    public void addReminder(int statrYear, int startMonth, int startDay, int startHour, int startMinut, int endYear, int endMonth, int endDay, int endHour, int endMinuts){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(statrYear, startMonth, startDay, startHour, startMinut);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinuts);
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put(Events.CALENDAR_ID, 1);
        eventValues.put(Events.TITLE, "OCS");
        eventValues.put(Events.DESCRIPTION, "Clinic App");
        eventValues.put(Events.EVENT_TIMEZONE, "Nasik");
        eventValues.put(Events.DTSTART, startMillis);
        eventValues.put(Events.DTEND, endMillis);

        //eventValues.put(Events.RRULE, "FREQ=DAILY;COUNT=2;UNTIL="+endMillis);
        eventValues.put("eventStatus", 1);
        eventValues.put("visibility", 3);
        eventValues.put("transparency", 0);
        eventValues.put(Events.HAS_ALARM, 1);

        Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        /***************** Event: Reminder(with alert) Adding reminder to event *******************/

        String reminderUriString = "content://com.android.calendar/reminders";

        ContentValues reminderValues = new ContentValues();

        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", 1);
        reminderValues.put("method", 1);

        Uri reminderUri = getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
    }
    //Remember to initialize this activityObj first, by calling initActivityObj(this) from your activity
    private static final String TAG = "CalendarHelper";
    public static final int CALENDARHELPER_PERMISSION_REQUEST_CODE = 99;


    public static void MakeNewCalendarEntry(Activity caller,String title,String description,String location,float startTime,float endTime, boolean allDay,boolean hasAlarm, int calendarId,int selectedReminderValue) {

        ContentResolver cr = caller.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startTime);
        values.put(Events.DTEND, endTime);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.STATUS, Events.STATUS_CONFIRMED);
        //values.put(Events.LAST_DATE,"12.2.2019");
        //values.put(Events.RDATE,"12.08.2109");


        if (allDay)
        {
            values.put(Events.ALL_DAY, true);
        }

        if (hasAlarm)
        {
            values.put(Events.HAS_ALARM, true);
        }

        //Get current timezone
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(TAG, "Timezone retrieved=>"+TimeZone.getDefault().getID());
        Uri uri = cr.insert(Events.CONTENT_URI, values);
        Log.i(TAG, "Uri returned=>"+uri.toString());
        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        if (hasAlarm)
        {
            ContentValues reminders = new ContentValues();
            reminders.put(Reminders.EVENT_ID, eventID);
            reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            reminders.put(Reminders.MINUTES, selectedReminderValue);

            Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);
        }


    }

    public static void requestCalendarReadWritePermission(Activity caller)
    {
        List<String> permissionList = new ArrayList<String>();

        if  (ContextCompat.checkSelfPermission(caller,Manifest.permission.WRITE_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_CALENDAR);

        }

        if  (ContextCompat.checkSelfPermission(caller,Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_CALENDAR);

        }

        if (permissionList.size()>0)
        {
            String [] permissionArray = new String[permissionList.size()];

            for (int i=0;i<permissionList.size();i++)
            {
                permissionArray[i] = permissionList.get(i);
            }

            ActivityCompat.requestPermissions(caller,
                    permissionArray,
                    CALENDARHELPER_PERMISSION_REQUEST_CODE);
        }

    }

    public static Hashtable listCalendarId(Context c) {

        if (haveCalendarReadWritePermissions((Activity)c)) {

            String projection[] = {"_id", "calendar_displayName"};
            Uri calendars;
            calendars = Uri.parse("content://com.android.calendar/calendars");

            ContentResolver contentResolver = c.getContentResolver();
            Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

            if (managedCursor.moveToFirst())
            {
                String calName;
                String calID;
                int cont = 0;
                int nameCol = managedCursor.getColumnIndex(projection[1]);
                int idCol = managedCursor.getColumnIndex(projection[0]);
                Hashtable<String,String> calendarIdTable = new Hashtable<>();

                do
                {
                    calName = managedCursor.getString(nameCol);
                    calID = managedCursor.getString(idCol);
                    Log.v(TAG, "CalendarName:" + calName + " ,id:" + calID);
                    calendarIdTable.put(calName,calID);
                    cont++;
                } while (managedCursor.moveToNext());
                managedCursor.close();

                return calendarIdTable;
            }

        }

        return null;

    }

    public static boolean haveCalendarReadWritePermissions(Activity caller)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(caller,
                Manifest.permission.READ_CALENDAR);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(caller,
                    Manifest.permission.WRITE_CALENDAR);

            if (permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }

        return false;
    }


}