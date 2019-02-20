package com.example.gerard.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabs;
    private ViewPager viewPager;
    private BottomNavigationView bottom_nav;
    MyAdapter adapter;
    Intent intent;
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
        bottom_nav=rootView.findViewById(R.id.bottom_nav);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs=view.findViewById(R.id.tabs);
        viewPager=view.findViewById(R.id.viewPager);
        adapter= new MyAdapter(getFragmentManager());
        adapter.addFragment(new InterestsFragment(),"Interests");
        adapter.addFragment(new EventHistoryFragment(),"EventHistory");
        adapter.addFragment(new FollowingFragment(),"Following");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        bottom_nav.setOnClickListener(this);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });


    }




    @Override
    public void onClick(View v) {
       Intent i1 =  new Intent(getActivity(),MainActivity.class);
       startActivity(i1);


 }
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

