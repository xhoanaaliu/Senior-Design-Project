package com.example.gerard.afinal.Account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gerard.afinal.EventHistoryFragment;
import com.example.gerard.afinal.InterestsFragment;
import com.example.gerard.afinal.MainActivity;
import com.example.gerard.afinal.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabs;
    private ViewPager viewPager;
    private MyAdapter adapter;
    public ProfileFragment() {
        // Required empty public constructor
    }



    public static ProfileFragment newInstance() {
        ProfileFragment pfragment  = new ProfileFragment();
        return pfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
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
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs=(TabLayout) view.findViewById(R.id.tabs);
        viewPager=(ViewPager) view.findViewById(R.id.viewPager);
        adapter= new MyAdapter(getChildFragmentManager());
        adapter.addFragment(new InterestsFragment(),"Interests");
        adapter.addFragment(new EventHistoryFragment(),"EventHıstory");
        adapter.addFragment(new FollowingFragment(),"Followıng");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }




    @Override
    public void onClick(View v) {
        Intent i1 =  new Intent(getActivity(), MainActivity.class);
        startActivity(i1);


    }
}