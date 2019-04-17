package com.example.gerard.afinal.Settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gerard.afinal.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    private Spinner spinner;
    private ArrayList<String> contact = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private OnFragmentInteractionListener mListener;
    private Button buttonComment;
    private EditText editEmail,editSuggestion;

    public CommentFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner=view.findViewById(R.id.spinner);
        buttonComment = view.findViewById(R.id.buttonComment);
        editEmail = view.findViewById(R.id.editEmail);
        editSuggestion=view.findViewById(R.id.editSuggestion);
        contact.add("New Feature Idea");
        contact.add("Compliment");
        contact.add("Bug or issue");
        contact.add("General Feedback");
        adapter = new ArrayAdapter<String >(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,contact);
        spinner.setAdapter(adapter);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Thank you for your comment!",Toast.LENGTH_SHORT).show();
                editEmail.setText("");
                editSuggestion.setText("");

            }
        });

    }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}