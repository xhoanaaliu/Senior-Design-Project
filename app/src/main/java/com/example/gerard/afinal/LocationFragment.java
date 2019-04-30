package com.example.gerard.afinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerard.afinal.Account.MyAdapter;

import com.google.android.gms.common.api.Status;
/*import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.Place;*/
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LocationFragment extends Fragment {
    final String apiKey ="AIzaSyAT0Qg5FjgR_2WNOXKBb_SwmuBP6Jw72Zg";
    TextView recLoc;
    ImageView searchBtn;

    public LocationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
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
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recLoc = view.findViewById(R.id.rec_loc);
        searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize Places.
                Places.initialize(getApplicationContext(), apiKey);
                // Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(getActivity());

                // Initialize the AutocompleteSupportFragment.
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                // Specify the types of place data to return.
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        recLoc.setText(place.getName()+","+place.getId());
                        Log.i("f", "Place: " + place.getName() + ", " + place.getId());
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("f", "An error occurred: " + status);
                    }
                });

               /* // Initialize the AutocompleteSupportFragment.
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                // Specify the types of place data to return.
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i("k", "Place: " + place.getName() + ", " + place.getId());
                        recLoc.setText(place.getAddress());
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("k", "An error occurred: " + status);
                    }
                });*/
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}