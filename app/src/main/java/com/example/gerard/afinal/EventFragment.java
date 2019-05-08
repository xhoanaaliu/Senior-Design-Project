package com.example.gerard.afinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.FirebaseError;
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

import java.sql.Time;
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

    public KenBurnsView poster;
    private TextView title, location, date, description,times, category;
    private ImageView googleLoc, calendarDate,buttonTime;
    private Button interestedButton, goingToButton;
    private String eventIdString, dateString, timeString;

    private boolean isGoingToButtonClicked = false;
    private boolean isInterestedInButtonClicked = false;

    private String event_id;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    String userID;
    FirebaseUser user;
    private static ArrayList<String> events_retrieved;
    DatabaseReference databaseReference,userRef;
    DatabaseReference myRef, goTo;
    DatabaseReference interestRef;
    DatabaseReference eventRef;
    private ArrayList<EventInfo> eventsArrayList;
    private Bitmap bitmap;
    String imageURL;

    public EventFragment() {

    }

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
        goTo = FirebaseDatabase.getInstance().getReference();
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
            dateString = bundle.getString("date");
            description.setText(bundle.getString("description"));
            timeString =bundle.getString("time");
            category.setText(bundle.getString("category"));
            eventIdString = bundle.getString("ID");
            String url = bundle.getString("url");

            final StorageReference islandRef = mStorageRef.child(url);

            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat formatter = new SimpleDateFormat("d MMMM");
            String s = formatter.format(dateInput);
            date.setText(s);
            times.setText(timeString);
            //GET THE IMAGE
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
            goTo.child("GoingTo").child(userID).child(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));
                        isGoingToButtonClicked = true;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

            goTo.child("InterestedIn").child(userID).child(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));
                        isInterestedInButtonClicked = true;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

            goingToButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isGoingToButtonClicked = !isGoingToButtonClicked; // toggle the boolean flag
                   if (isGoingToButtonClicked == true && isInterestedInButtonClicked == false) {
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));
                        requestCalendarReadWritePermission();
                        boolean b = haveCalendarReadWritePermissions();
                        calendarEvent(b);

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(null);
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(true);

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == false){
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(null);
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(null);

                    }else if(isGoingToButtonClicked == true && isInterestedInButtonClicked == true) {
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(true);

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(null);
                        requestCalendarReadWritePermission();

                        boolean b = haveCalendarReadWritePermissions();
                        calendarEvent(b);

                        goingToButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.white));

                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        isInterestedInButtonClicked = false;

                    }else if(isGoingToButtonClicked == false && isInterestedInButtonClicked == true) {
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(null);
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

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(true);
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(null);

                    }else if ((isInterestedInButtonClicked == false && isGoingToButtonClicked == false)
                            || (isInterestedInButtonClicked == false && isGoingToButtonClicked == true)){

                        interestedButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(null);
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(null);

                    }else if(isInterestedInButtonClicked == true && isGoingToButtonClicked == true) {
                        interestedButton.setBackgroundResource(R.drawable.rounded_button_clicked);
                        interestedButton.setTextColor(getResources().getColor(R.color.white));

                        goTo.child("InterestedIn").child(userID).child(eventIdString).setValue(true);
                        goTo.child("GoingTo").child(userID).child(eventIdString).setValue(null);

                        goingToButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        goingToButton.setBackgroundResource(R.drawable.rounded_button_unclicked);
                        isGoingToButtonClicked = false;
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void calendarEvent(Boolean b){

        if (b){
            String dateInputS = dateString;
            Date dateInput = null;
            try {
                dateInput = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(dateInputS+" "+timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat year = new SimpleDateFormat("yyyy");
            DateFormat month = new SimpleDateFormat("MM");
            DateFormat day = new SimpleDateFormat("dd");
            DateFormat hour = new SimpleDateFormat("hh");
            DateFormat minute =new SimpleDateFormat("mm");
            String outputYear = year.format(dateInput);
            String outputMonth = month.format(dateInput);
            String outputDay = day.format(dateInput);
            String outputHour = hour.format(dateInput);
            String outputMinute = minute.format(dateInput);


            long startTime = getLongAsDate(Integer.parseInt(outputYear), Integer.parseInt(outputMonth),
                    Integer.parseInt(outputDay), Integer.parseInt(outputHour)+3, Integer.parseInt(outputMinute));
            long endTime = getLongAsDate(Integer.parseInt(outputYear), Integer.parseInt(outputMonth),
                    Integer.parseInt(outputDay), Integer.parseInt(outputHour) +4, Integer.parseInt(outputMinute));

            ContentResolver cr = getContext().getContentResolver();
            ContentValues calEvent = new ContentValues();

            calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
            calEvent.put(CalendarContract.Events.TITLE, "Reminder to " + title.getText().toString());
            calEvent.put(CalendarContract.Events.DTSTART, startTime);
            calEvent.put(CalendarContract.Events.DTEND, endTime);
            calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
            calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);

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
    public long getLongAsDate(int year, int month, int date, int hour, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
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

    public static boolean haveCalendarReadWritePermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CALENDAR);

        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_CALENDAR);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }

        return false;
    }

    public void deleteEventFromCalendar(View view) {
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
            final String[] listItems = {"This event does not exist.", "The poster is not the same with the information given.",
                    "This post contains offensive content."};
            final ArrayList<String > mSelectedItems = new ArrayList();  // Where we track the selected items
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Report")// Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(listItems, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(listItems[which]);
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(which));
                                    }
                                }
                            })
                    // Set the action buttons
                    .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Report");
                            dataref.child(userID).child(eventIdString).addListenerForSingleValueEvent(new ValueEventListener() {
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                       Toast.makeText(getContext(),"You already reported this event.", Toast.LENGTH_SHORT);
                                    }else {
                                        dataref.child(userID).child(eventIdString).child("reason").setValue(mSelectedItems.get(0));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

}