package com.example.gerard.afinal.Login_SignUp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gerard.afinal.Account.UserInformationFragment;
import com.example.gerard.afinal.R;



public class SignUpFragment extends Fragment implements View.OnClickListener{


    //private OnFragmentInteractionListener mListener;
    private Button signin_b;
    private Button signup_b;
    private RegisterQuestionFragment question;
    private LoginFragment log;

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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        signin_b = view.findViewById(R.id.sign_in_button);
        signup_b = view.findViewById(R.id.sign_up_button);
        signin_b.setOnClickListener(this);
        signup_b.setOnClickListener(this);
        signup_b.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){

                question = new RegisterQuestionFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,question,"information").addToBackStack(null).commit();
            }
        });
        signin_b.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){

                log = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,log,"log").addToBackStack(null).commit();
            }
        });
    }

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