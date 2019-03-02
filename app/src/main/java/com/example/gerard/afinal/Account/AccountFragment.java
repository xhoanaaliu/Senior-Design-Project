package com.example.gerard.afinal.Account;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gerard.afinal.R;


public class AccountFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private View AccountView;
    RecyclerView rv_account;
    private AccountAdapter accountAdapter;

    public AccountFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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

        AccountView = inflater.inflate(R.layout.fragment_account,container,false);
        rv_account= (RecyclerView)AccountView.findViewById(R.id.rv_account);
        rv_account.setLayoutManager(new LinearLayoutManager(getContext()));
        accountAdapter = new AccountAdapter(getActivity());
        rv_account.setAdapter(accountAdapter);

        return AccountView;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
