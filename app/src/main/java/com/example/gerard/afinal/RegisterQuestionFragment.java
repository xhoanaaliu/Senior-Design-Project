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

public class RegisterQuestionFragment extends Fragment implements View.OnClickListener {

    private Button yesOrganization;
    private Button noOrganization;

    public RegisterQuestionFragment() {
        // Required empty public constructor
    }



    public static RegisterQuestionFragment newInstance() {
        RegisterQuestionFragment fragmentQues = new RegisterQuestionFragment();
        return fragmentQues;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ask_organization_user, container, false);

        yesOrganization = (Button) rootView.findViewById(R.id.yes_org);
        noOrganization = (Button) rootView.findViewById(R.id.no_org);

        yesOrganization.setOnClickListener(this);
        noOrganization.setOnClickListener(this);
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

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {}
    public void replaceFragment(Fragment someFragment) {
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.questionForUser, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
