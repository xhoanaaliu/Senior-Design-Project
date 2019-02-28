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
import android.app.FragmentTransaction;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class SignUpFragment extends Fragment implements View.OnClickListener{


    //private OnFragmentInteractionListener mListener;
    private Button signin_b;
    private Button signup_b;

    public SignUpFragment() { }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
        View rootView = inflater.inflate(R.layout.signup, container, false);

        signin_b = (Button) rootView.findViewById(R.id.sign_in_button);
        signup_b = (Button) rootView.findViewById(R.id.sign_up_button);

        signin_b.setOnClickListener(this);
        signup_b.setOnClickListener(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
        //mListener.onFragmentInteraction(uri);
        //}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void onClick(View view){

        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.sign_in_button:
                fragment = new LoginFragment();
                replaceFragment(fragment);
                break;

            case R.id.sign_up_button:
                fragment = new RegisterQuestionFragment();
                replaceFragment(fragment);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.register, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}