package com.example.gerard.afinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.fourmob.datetimepicker.date.CalendarDay;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;


public class NewEventFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{


    private static final String DATEPICKER_TAG = "datepicker";
    private static final String TIMEPICKER_TAG = "timepicker";

    private static Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ImageView dateButton;
    private ImageView timeButton;
    private ImageView submitButton;
    private MaterialSpinner spinner;
    private KenBurnsView new_poster;
    private DatabaseReference dataref;
    private EditText titleField;
    private EditText addressField;
    private EditText descriptionField;
    private TextView date_field;
    private TextView time_field;
    HashMap<String,String> mMap;
    StorageReference storageRef;


    private static final String ARG_PARAM1 = "hashmap";
    private String mParam1;

    HashMap<String, String> n;

    Bitmap bitmapimage;
    String userID;
    FirebaseUser user;
    String category;

    public NewEventFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewEventFragment newInstance() {
        NewEventFragment fragment = new NewEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMap = new HashMap<>();
        Bundle b = this.getArguments();

        bitmapimage = getArguments().getParcelable("BitmapImage");

        if(b.getSerializable("hashmap") != null)
            mMap = (HashMap<String,String>)b.getSerializable("hashmap");

        b.clear();

        }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        addressField =  view.findViewById(R.id.address);

        dateButton = view.findViewById(R.id.dateButton);
        timeButton =  view.findViewById(R.id.timeButton);
        submitButton = view.findViewById(R.id.submit);
        titleField = view.findViewById(R.id.title);

        descriptionField = view.findViewById(R.id.description);
        date_field = view.findViewById(R.id.date_field);
        time_field = view.findViewById(R.id.time_field);
        new_poster = view.findViewById(R.id.new_poster);
        new_poster.setImageBitmap(bitmapimage);

        new_poster.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        storageRef = FirebaseStorage.getInstance().getReference();
        spinner =  view.findViewById(R.id.spinner);
        spinner.setItems("Conference", "Cultural", "Workshop", "Concert", "Festival", "Exhibition");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                category = item;
            }
        });

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        dataref = FirebaseDatabase.getInstance().getReference("Event");

        setFields();


        StorageReference imageRef = storageRef.child( bitmapimage.toString()+ ".jpg");

        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapimage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "Image Upload Failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_LONG).show();
                // ...
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //setCurrentFragmentName("Users");
        //  EventBus.getDefault().post(new GoToHomeScreenEvent(currentFragmentName, "Users", false));
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        // EventBus.getDefault().unregister(this);
        super.onStop();
    }

   /* public void setCurrentFragmentName(String currentFragmentName){
        this.currentFragmentName = currentFragmentName;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v;

           v = inflater.inflate(R.layout.fragment_new_event, container, false);
           return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(getActivity(), "Time Set", Toast.LENGTH_LONG).show();
        String new_time = hourOfDay + ":" + minute;
        time_field.setText(new_time);
    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(getActivity(), "Date Set", Toast.LENGTH_LONG).show();
        int month_up = month + 1;
        String new_date = day + "-" + month_up + "-" + year;
        try {
            DateFormat inputFormat = new SimpleDateFormat("d-M-yyyy");
            Date date = inputFormat.parse(new_date);

            DateFormat outputFormatDate = new SimpleDateFormat("dd-MM-yyyy");
            String outputStringDate = outputFormatDate.format(date);
            date_field.setText(outputStringDate);
        }catch (ParseException p){
            p.printStackTrace();
        }
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.dateButton:

                Calendar minDate = Calendar.getInstance();
                minDate.setTime(new Date());
                minDate.add(Calendar.DAY_OF_YEAR, -20);

                Calendar maxDate = Calendar.getInstance();
                maxDate.setTime(new Date());
                maxDate.add(Calendar.DAY_OF_YEAR, 365);

                datePickerDialog.setDateConstraints(new CalendarDay(minDate), new CalendarDay(maxDate));
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);

                DatePickerDialog dpd = (DatePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
                if (dpd != null) {
                    dpd.setOnDateSetListener(this);
                }

                break;

            case R.id.timeButton:

                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);

                TimePickerDialog tpd = (TimePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
                if (tpd != null) {
                    tpd.setOnTimeSetListener(this);
                }

                break;

            case R.id.submit:

                if( titleField.getText().toString().trim().equals("")||date_field.getText().toString().trim().equals("")
                        ||time_field.getText().toString().trim().equals("")||addressField.getText().toString().trim().equals("")
                        ||descriptionField.getText().toString().trim().equals(""))
                {
                    titleField.setError( "Required!" );
                    date_field.setError( "Required!" );
                    time_field.setError( "Required!" );
                    addressField.setError( "Required!" );
                    descriptionField.setError( "Required!" );

                    titleField.setHint("Please fill in the required Information!");

                } else {
                    Map<String, String> em = new HashMap<>();
                    em.put("title" , titleField.getText().toString());
                    em.put("date" , date_field.getText().toString());
                    em.put("time" , time_field.getText().toString());
                    em.put("location" , addressField.getText().toString() );
                    em.put("category" , category);
                    em.put("description" , descriptionField.getText().toString());
                    em.put("imageName", bitmapimage.toString() + ".jpg");
                    em.put("user_id",userID);

                    DatabaseReference db = dataref.push();
                    db.setValue(em);
                    HomePage hm = new HomePage();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, hm, "new_event")
                            .addToBackStack(null).commit();
                }
                break;

            case R.id.new_poster:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                photoView.setImageBitmap(bitmapimage);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
        }
    }

    public void setFields(){

        for (HashMap.Entry<String, String> entry : mMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());

            switch(entry.getValue()){
                case("Place"):
                    addressField.setHint(entry.getKey());
                    break;
                case("Date"):
                case("Time"):
                    try {
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+'00:00");
                        Date date = inputFormat.parse(entry.getKey());

                        DateFormat outputFormatDate = new SimpleDateFormat("dd-MM-yyyy");
                        String outputStringDate = outputFormatDate.format(date);

                        DateFormat outputFormatTime = new SimpleDateFormat("HH:mm");
                        String outputStringTime = outputFormatTime.format(date);

                        if(outputStringTime.equals("00:00")){
                            //nothing
                        }else {
                            time_field.setText(outputStringTime);
                        }

                        date_field.setText(outputStringDate);

                    }catch (ParseException p){
                        p.printStackTrace();
                    }
                    break;

            }




        }


    }

}