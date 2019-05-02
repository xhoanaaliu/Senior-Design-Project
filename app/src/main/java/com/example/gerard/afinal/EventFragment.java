package com.example.gerard.afinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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

import java.util.ArrayList;
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
                    }/*interestRef.orderByChild("eventID").equalTo(eventId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    });*/
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}});

            goingToButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isGoingToButtonClicked = !isGoingToButtonClicked; // toggle the boolean flag

                    if (isGoingToButtonClicked == true && isInterestedInButtonClicked == false) {
                        v.setBackgroundResource(R.color.colorPrimary);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfGoingToNew();

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == false){
                        v.setBackgroundColor(Color.TRANSPARENT);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeGoingTo();

                    }else if(isGoingToButtonClicked == true && isInterestedInButtonClicked == true) {
                        checkIfGoingToNew();

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

                    }else if ((isInterestedInButtonClicked == false && isGoingToButtonClicked == false)|| (isInterestedInButtonClicked == false && isGoingToButtonClicked == true)){
                        //setFocus(btn_unfocus, btn[1]);
                        v.setBackgroundColor(Color.TRANSPARENT);
                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        removeInterestedIn();

                    }else if(isInterestedInButtonClicked == true && isGoingToButtonClicked == true) {
                        v.setBackgroundResource(R.color.colorPrimary);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        checkIfInterestedInNew();

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

    //Remember to initialize this activityObj first, by calling initActivityObj(this) from your activity
    private static final String TAG = "CalendarHelper";
    public static final int CALENDARHELPER_PERMISSION_REQUEST_CODE = 99;


    public static void MakeNewCalendarEntry(Activity caller,String title,String description,String location,long startTime,long endTime, boolean allDay,boolean hasAlarm, int calendarId,int selectedReminderValue) {

        ContentResolver cr = caller.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startTime);
        values.put(Events.DTEND, endTime);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.STATUS, Events.STATUS_CONFIRMED);


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