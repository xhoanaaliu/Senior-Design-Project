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

import com.example.gerard.afinal.Account.OrganizationInformationFragment;
import com.example.gerard.afinal.Account.UserInformationFragment;
import com.example.gerard.afinal.R;

public class RegisterQuestionFragment extends Fragment implements View.OnClickListener {

    private Button org;
    private Button user;
    private UserInformationFragment userfragment;
    private OrganizationInformationFragment orgfragment;

    public RegisterQuestionFragment(){}

    public static RegisterQuestionFragment newInstance() {
        RegisterQuestionFragment fragment = new RegisterQuestionFragment();
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
        View rootView = inflater.inflate(R.layout.ask_organization_user, container, false);
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

        org = (Button) view.findViewById(R.id.yes_org);
        user = (Button) view.findViewById(R.id.no_org);
        org.setOnClickListener(this);
        user.setOnClickListener(this);
        user.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){

                userfragment = new UserInformationFragment();
                getFragmentManager().beginTransaction().replace(R.id.questionForUser,userfragment," user information").addToBackStack(null).commit();
            }
        });
        org.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){

                orgfragment = new OrganizationInformationFragment();
                getFragmentManager().beginTransaction().replace(R.id.questionForUser,orgfragment," user information").addToBackStack(null).commit();
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
            case R.id.yes_org:
                fragment = new OrganizationInformationFragment();
                replaceFragment(fragment);
                break;

            case R.id.no_org:
                fragment = new UserInformationFragment();
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
        transaction.replace(R.id.questionForUser, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
