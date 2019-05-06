package com.example.gerard.afinal;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import android.provider.CalendarContract;

public class EventFragment extends Fragment implements View.OnClickListener{

    //private OnFragmentInteractionListener mListener;
    public static int MY_PERMISSION = 1;
    public KenBurnsView poster;
    private TextView title, location, date, description,times, category;
    private ImageView googleLoc, calendarDate,buttonTime;
    private Button interestedButton, goingToButton;
    private String eventIdString, dateString;

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
        setHasOptionsMenu(true);
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

        //haveCalendarReadWritePermissions(getActivity());
        //requestCalendarReadWritePermission(getActivity());
  }

        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.googleLocation:
                //ads
                    break;
                case R.id.calendarDate:
                    requestCalendarReadWritePermission();
                    break;
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
    }

    @Override
    public void onStart() {
        super.onStart();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Bundle bundle = this.getArguments();
        if(bundle != null){
            title.setText(bundle.getString("title"));
            location.setText(bundle.getString("location"));
            //date.setText(bundle.getString("date"));
            dateString = bundle.getString("date");
            description.setText(bundle.getString("description"));
            times.setText(bundle.getString("time"));
            category.setText(bundle.getString("category"));
            //eventId.setText(bundle.getString("ID"));
            eventIdString = bundle.getString("ID");
            String url = bundle.getString("url");
            final StorageReference islandRef = mStorageRef.child(url);
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
           // DateFormat day = new SimpleDateFormat("dd");
           // String outputYear = year.format(dateInput);
            DateFormat formatter = new SimpleDateFormat("d MMMM");
            String s = formatter.format(dateInput);
            date.setText(s);

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
                        userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                                    goingToButton.setTextColor(getResources().getColor(R.color.white));
                                    isGoingToButtonClicked = true;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        //Toast.makeText(getApplicationContext(),"Your user id is on going to table",Toast.LENGTH_SHORT).show();
                        interestRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    interestedButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                                    interestedButton.setTextColor(getResources().getColor(R.color.white));
                                    isInterestedInButtonClicked = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                /*if (dataSnapshot.getValue() != null) {
                                    //Toast.makeText(getApplicationContext(),"You are already going to this event",Toast.LENGTH_SHORT).show();
                                    goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
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

                    if (isGoingToButtonClicked == true && isInterestedInButtonClicked == false) {
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));
                        requestCalendarReadWritePermission();

                        removeInterestedIn();
                        checkIfGoingToNew();

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == false){
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        removeInterestedIn();
                        removeGoingTo();

                    }else if(isGoingToButtonClicked == true && isInterestedInButtonClicked == true) {
                        checkIfGoingToNew();
                        removeInterestedIn();
                        requestCalendarReadWritePermission();

                        goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));

                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        isInterestedInButtonClicked = false;

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == true) {
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeGoingTo();
                    }
                }
            });
            interestedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInterestedInButtonClicked = !isInterestedInButtonClicked; // toggle the boolean flag

                    if (isInterestedInButtonClicked == true&& isGoingToButtonClicked == false) {
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfInterestedInNew();
                        removeGoingTo();

                    }else if ((isInterestedInButtonClicked == false && isGoingToButtonClicked == false)
                            || (isInterestedInButtonClicked == false && isGoingToButtonClicked == true)){
                        //setFocus(btn_unfocus, btn[1]);
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeInterestedIn();
                        removeGoingTo();

                    }else if(isInterestedInButtonClicked == true && isGoingToButtonClicked == true) {
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfInterestedInNew();
                        removeGoingTo();

                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
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
        super.onStop();
    }
   public void checkIfGoingToNew(){
       userRef = FirebaseDatabase.getInstance().getReference("GoingTo");
       userRef.orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if (dataSnapshot.getValue() != null) {
                   String s =(dataSnapshot.getKey());
                   DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                       DatabaseReference ref = database.child("GoingTo").child(s);
                        //userRef= FirebaseDatabase.getInstance().getReference("GoingTo/"+goingToEvents.get(0));
                        ref.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.getValue() != null) {
                               Toast.makeText(getApplicationContext(),"You are already going to this event",Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                               EventInfo e1 = new EventInfo();
                               e1.setEvent_id(eventIdString);
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
               }
                   //Toast.makeText(getApplicationContext(),"Your user id is on going to table",Toast.LENGTH_SHORT).show();
               else {
                   EventInfo e1 = new EventInfo();
                   e1.setEvent_id(eventIdString);
                   String value = e1.getEvent_id();
                   Map<String, String> goingTo = new HashMap<>();
                   goingTo.put("eventID", value);
                   goingTo.put("user_id", userID);
                   DatabaseReference db = myRef.push();
                   db.setValue(goingTo);
                   /*goingToEvents = new ArrayList<>();
                   DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                   while (goingToEvents.size() != 0) {
                       DatabaseReference ref = database.child("GoingTo").child(goingToEvents.get(0));
                       ref.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.getValue() != null) {
                                   Toast.makeText(getApplicationContext(), "You are already going to this event", Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                                   EventInfo e1 = new EventInfo();
                                   e1.setEvent_id(eventIdString);
                                   String value = e1.getEvent_id();
                                   Map<String, String> goingTo = new HashMap<>();
                                   goingTo.put("eventID", value);
                                   goingTo.put("user_id", userID);
                                   DatabaseReference db = myRef.push();
                                   db.setValue(goingTo);
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });
                       goingToEvents.remove(0);
                       //Toast.makeText(getApplicationContext(),"WHatatattata",Toast.LENGTH_SHORT).show();
                   }*/
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
                eventRef.child("GoingTo").orderByChild("eventID").equalTo(eventIdString).addChildEventListener(new ChildEventListener() {
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
                    userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You are already interested in this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                EventInfo e2 = new EventInfo();
                                e2.setEvent_id(eventIdString);
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
                    userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You are already interested in this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                EventInfo e2 = new EventInfo();
                                e2.setEvent_id(eventIdString);
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
                eventRef.child("Interested").orderByChild("eventID").equalTo(eventIdString).addChildEventListener(new ChildEventListener() {
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
            String dateInputS = dateString;
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
                }else {
                    view.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                }
                //view the event in calendar
                startActivity(view);
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
    //Remember to initialize this activityObj first, by calling initActivityObj(this) from your activity
    private static final String TAG = "CalendarHelper";
    public static final int CALENDARHELPER_PERMISSION_REQUEST_CODE = 99;

    public void requestCalendarReadWritePermission() {
        List<String> permissionList = new ArrayList<String>();

        if  (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_CALENDAR);

        }

        if  (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_CALENDAR);

        }

        if (permissionList.size()>0) {
            String[] permissionArray = new String[permissionList.size()];

            for (int i = 0; i < permissionList.size(); i++) {
                permissionArray[i] = permissionList.get(i);
            }

            requestPermissions(permissionArray,
                    CALENDARHELPER_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CALENDARHELPER_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    calendarEvent(true);
                }else {
                    Toast.makeText(getContext(),"Denied",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public static boolean haveCalendarReadWritePermissions(Activity caller)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(caller,
                Manifest.permission.READ_CALENDAR);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(caller,
                    Manifest.permission.WRITE_CALENDAR);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }

        return false;
    }

    public void deleteEvent(View view) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, CALENDARHELPER_PERMISSION_REQUEST_CODE);
        }

        Uri uri = CalendarContract.Events.CONTENT_URI;

        String mSelectionClause = CalendarContract.Events.TITLE+ " = ?";
        String[] mSelectionArgs = {"Zoftino.com Tech Event"};

        int updCount = getApplicationContext().getContentResolver().delete(uri,mSelectionClause,mSelectionArgs);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.reportEvent){
            new TTFancyGifDialog.Builder(getActivity())
                    .setTitle("Report")
                    .setMessage("Are you sure you want to report this Event?")
                    .setPositiveBtnBackground("#32CD32")
                    .setPositiveBtnText("OK")
                    .setGifResource(R.drawable.gif8)   //Pass your Gif here
                    .OnPositiveClicked(new TTFancyGifDialogListener() {
                        @Override
                        public void OnClick() {
                            checkIfAlreadyReported();
                            Spinner popupSpinner = new Spinner(getContext(), Spinner.MODE_DIALOG);
                            Toast.makeText(getActivity(),"Thank you! We will look at your report request!",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .build();
        }
        return super.onOptionsItemSelected(item);
    }
    public void checkIfAlreadyReported(){
        userRef = FirebaseDatabase.getInstance().getReference("Report");
        userRef.orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You have already reported this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Report");
                                Map<String, String> report = new HashMap<>();
                                report.put("eventId", eventIdString);
                                report.put("userId", userID);
                                report.put("reason", "test name");
                                DatabaseReference db = dataref.push();
                                db.setValue(report);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}});
                    //Toast.makeText(getApplicationContext(),"Your user id is on going to table",Toast.LENGTH_SHORT).show();
                } else {
                    userRef.orderByChild("eventID").equalTo(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(getApplicationContext(),"You have already reported this event",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Report");
                                Map<String, String> report = new HashMap<>();
                                report.put("eventId", eventIdString);
                                report.put("userId", userID);
                                report.put("reason", "test name");
                                DatabaseReference db = dataref.push();
                                db.setValue(report);
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

}