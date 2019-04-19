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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gerard.afinal.MainActivity;
import com.example.gerard.afinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryListAdapter;
    private TextView categoryName, categoryName1;
    private ImageView categoryPicture, categoryPicture1;
    private CategoryAdapter categoryAdapter;

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
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        categoryName = getView().findViewById(R.id.categoryName);
        categoryName1 = getView().findViewById(R.id.categoryName1);
        categoryPicture = getView().findViewById(R.id.categoryPicture);
        categoryPicture1 = getView().findViewById(R.id.categoryPicture1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        categoryList = new ArrayList<>();
        categoryList.add(new Category("as", "asa",R.drawable.com_facebook_profile_picture_blank_portrait,R.drawable.com_facebook_profile_picture_blank_square));
        categoryList.add(new Category("as", "asa",R.drawable.com_facebook_profile_picture_blank_portrait,R.drawable.com_facebook_profile_picture_blank_square));
        categoryList.add(new Category("as", "asa",R.drawable.com_facebook_profile_picture_blank_portrait,R.drawable.com_facebook_profile_picture_blank_square));
        System.out.print("ghfhjf"+categoryList.size());
        categoryAdapter = new CategoryAdapter(new ArrayList<Category>(categoryList));
        recyclerView.setAdapter(categoryAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
