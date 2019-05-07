package com.example.gerard.afinal.Settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gerard.afinal.HomePage;
import com.example.gerard.afinal.MainActivity;
import com.example.gerard.afinal.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Category> categoryList;
    private TextView categoryName, categoryName1, categoryName2, categoryName3, categoryName4, categoryName5;
    private ImageView categoryPicture, categoryPicture1, categoryPicture2, categoryPicture3, categoryPicture4, categoryPicture5;
    private Button applyButton;
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;
    private HomePage home;
    FirebaseUser user;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_categories, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Users");
        applyButton = view.findViewById(R.id.button3);

        categoryName = view.findViewById(R.id.academic_event);
        categoryName1 = view.findViewById(R.id.cultural_event);
        categoryName2 = view.findViewById(R.id.concerts_event);
        categoryName3 = view.findViewById(R.id.festivals_event);
        categoryName4 = view.findViewById(R.id.workshop_event);
        //categoryName5 = getView().findViewById(R.id.exhibition_event);

        categoryPicture = view.findViewById(R.id.academic_picture);
        categoryPicture1 = view.findViewById(R.id.cultural_picture);
        categoryPicture2 = view.findViewById(R.id.concerts_picture);
        categoryPicture3 = view.findViewById(R.id.festivals_picture);
        categoryPicture4 = view.findViewById(R.id.workshop_picture);
        categoryPicture5 = view.findViewById(R.id.exhibition_picture);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                home = new HomePage();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, home, "HomePageOpen")
                        .addToBackStack(null)
                        .commit();
            }

         });
        categoryPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category0 = "Conferences";
                myRefUsers.child("Interests").setValue(category0);
            }
        });
        categoryPicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category1 = "Cultural";
                myRefUsers.child("Interests").setValue(category1);
            }
        });

        categoryPicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category2 = "Concerts";
                myRefUsers.child("Interests").setValue(category2);
            }
        });
        categoryPicture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category3 = "Festivals";
                myRefUsers.child("Interests").setValue(category3);

            }
        });
        categoryPicture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category4 = "Workshops";
                myRefUsers.child("Interests").setValue(category4);

            }
        });
        categoryPicture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category5 = "Exhibitions";
                myRefUsers.child("Interests").setValue(category5);

            }
        });

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Conferences", "Cultural",R.drawable.academic,R.drawable.cultural));
        categoryList.add(new Category("Concerts", "Festival",R.drawable.entertainment,R.drawable.festivals));
        categoryList.add(new Category("Workshops", "Exhibition",R.drawable.workshop,R.drawable.exhibition));


        System.out.print("ghfhjf"+categoryList.size());



        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

}



