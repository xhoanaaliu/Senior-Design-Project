package com.example.gerard.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.fourmob.datetimepicker.date.CalendarDay;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{


    private static final String DATEPICKER_TAG = "datepicker";
    private static final String TIMEPICKER_TAG = "timepicker";

    private static Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton;
    private Button timeButton;
    private Button submitButton;
    private MaterialSpinner spinner;
    private KenBurnsView new_poster;
    private DatabaseReference dataref;
    private EditText titleField;
    private EditText addressField;
    private EditText descriptionField;


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


    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        dateButton = view.findViewById(R.id.dateButton);
        timeButton =  view.findViewById(R.id.timeButton);
        submitButton = view.findViewById(R.id.submit);
        titleField = view.findViewById(R.id.title);
        addressField = view.findViewById(R.id.address);
        descriptionField = view.findViewById(R.id.description);

        spinner =  view.findViewById(R.id.spinner);
        spinner.setItems("Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        new_poster = view.findViewById(R.id.new_event_poster);

        dataref = FirebaseDatabase.getInstance().getReference();
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

    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {

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
                maxDate.add(Calendar.DAY_OF_YEAR, 20);

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

                Map<String, String> em = new HashMap<>();
                em.put("title" , titleField.getText().toString());
                em.put("date" , "15 March");
                em.put("time" , "20:00");
                em.put("location" , addressField.getText().toString() );
                em.put("category" , "CS");
                em.put("description" , descriptionField.getText().toString());

                dataref.child("Event").child("event2").setValue(em);

        }


    }

}