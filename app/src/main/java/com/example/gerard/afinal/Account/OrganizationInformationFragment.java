package com.example.gerard.afinal.Account;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gerard.afinal.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrganizationInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrganizationInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizationInformationFragment extends Fragment implements View.OnClickListener {

    private Button sign_up_org;

    private OnFragmentInteractionListener mListener;

    public OrganizationInformationFragment() {
        // Required empty public constructor
    }


    public static OrganizationInformationFragment newInstance(String param1, String param2) {
        OrganizationInformationFragment fragment = new OrganizationInformationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_information, container, false);

        sign_up_org = (Button) rootView.findViewById(R.id.signupnoworg);
        sign_up_org.setOnClickListener(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //(if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        //}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {}

    public void onClick(View view){

        //THIS ACTION LISTENER WILL BE EDITED LATER
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
