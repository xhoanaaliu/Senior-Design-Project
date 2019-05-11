package com.example.gerard.afinal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerard.afinal.Account.MyAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
/*import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.Place;*/
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LocationFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener{
    private MapView mMapView;
    private GoogleMap googleMap;
    public static double lat = 39.875275;
    public static double lng = 32.748524;
    private static List<Event> events_retrieved;
    DatabaseReference databaseReference;
    final String apiKey ="AIzaSyAT0Qg5FjgR_2WNOXKBb_SwmuBP6Jw72Zg";
    TextView recLoc;
    TextView searchBtn;
    private final int REQUEST_LOCATION_CODE = 99;
    private final int REQUEST_LOCATION_CODE2 = 98;
    private Location lastLoc;
    private boolean gotLocation = false;
    private Map<LatLng, Event> markerList = new HashMap<>();
    private LatLng selected;
    private boolean reached = false;

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
        Bundle arguments = this.getArguments();
        if(arguments != null){
            lat = arguments.getDouble("latitude");
            lng = arguments.getDouble("longtitude");
            Log.d("LATLONG", ""+lat+" "+lng);
        }
        else{
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("REQUEST LOCATION", "NOT GRANTED");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE2);
                //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            else{
                if(!gotLocation) {
                    SmartLocation.with(getContext()).location()
                            .oneFix()
                            .start(new OnLocationUpdatedListener() {
                                @Override
                                public void onLocationUpdated(Location location) {
                                    if (location != null) {
                                        Log.d("LOCATION VAAAR22222", location.toString());
                                        lastLoc = location;
                                        gotLocation = true;
                                        Bundle bundle = new Bundle();
                                        bundle.putDouble("latitude", location.getLatitude());
                                        bundle.putDouble("longtitude", location.getLongitude());

                                        LocationFragment locationFragment = new LocationFragment();
                                        locationFragment.setArguments(bundle);

                                        AppCompatActivity activity = (AppCompatActivity) getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, locationFragment)
                                                .addToBackStack(null).commit();
                                    }
                                }
                            });
                }
            }
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        // Inflate the layout for this fragment
        TextView locationName = view.findViewById(R.id.textView7);
        searchBtn = view.findViewById(R.id.search_btn);
        final Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocation(lat, lng, 10);
            Log.d("Address", addressList.get(0).toString());
        }
        catch (IOException io){
            Log.d("IO", "Service is not available");
        }



        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Event");
        events_retrieved = new ArrayList<>();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                String category = value.get("category");
                String date = value.get("date");
                String description = value.get("description");
                String URL = value.get("imageName");
                String location = value.get("location");
                String time = value.get("time");
                String title = value.get("title");

                Event temp = new Event(title, location, date,time,URL, description);

                Date currentDate = new Date();
                Date currDate = new Date();
                String dateInString = date;
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                long days = 0;
                try {
                    currDate = formatter.parse(dateInString);
                    if (currDate.getTime() >= currentDate.getTime()){
                        long diff = currDate.getTime() - currentDate.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        days = hours / 24;
                    }
                }catch (ParseException e) {
                    //handle exception if date is not in "dd-MM-yyyy" format
                }


                if(days < 20 && days != 0) {

                    Geocoder geocoder1 = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder1.getFromLocationName(location, 10);
                    } catch (IOException io) {
                        Log.d("Service", "Unavailable");
                    }

                    if (!addresses.isEmpty()) {
                        addMarker(googleMap, addresses.get(0).getLatitude(), addresses.get(0).getLongitude(), temp);
                        Log.d("THIS", "REACHEDDDDDDDDDD");
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("lllll", databaseError.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                        //recLoc.setText(place.getName() + "," + place.getId());
                        if(place != null) {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> list = new ArrayList<>();
                            try {
                                list = geocoder.getFromLocationName(place.getName(), 10);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            LatLng temp;
                            if(!list.isEmpty()){
                                temp = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(temp).zoom(11).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.


                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addMarker(GoogleMap mMap, double latitude, double longtitude, Event event){
        LatLng temp = new LatLng(latitude, longtitude);

        markerList.put(temp, event);

        mMap.addMarker(new MarkerOptions().position(temp)).setTag(temp);

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        googleMap = mMap;
        LatLng curr;
        if (lastLoc != null) {
            curr = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            Log.d("KONUM VAAAAAR", "" + lastLoc.getLatitude() + "" + lastLoc.getLongitude());
        } else {
            curr = new LatLng(lat, lng);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(curr).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //addMarker(googleMap, curr.latitude, curr.longitude);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng position = (LatLng) marker.getTag();

        if(markerList.containsKey(position)){
            Event temp = markerList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("title", temp.getTitle());
            bundle.putString("location", temp.getLocation());
            bundle.putString("date", temp.getDate());
            bundle.putString("description", temp.getDescription());
            bundle.putString("time", temp.getTime());
            bundle.putString("url", temp.getURL());

            Fragment fragment = new EventFragment();
            fragment.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment)
                    .addToBackStack(null).commit();
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("LOCATION PERMISSION", "GRANTEEEED");
                    SmartLocation.with(getContext()).location()
                            .oneFix()
                            .start(new OnLocationUpdatedListener() {
                                @Override
                                public void onLocationUpdated(Location location) {
                                    if(location != null){
                                        Log.d("LOCATION VAAAR", location.toString());
                                        lastLoc = location;
                                    }
                                }});
                }
            case REQUEST_LOCATION_CODE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("LOCATION PERMISSION", "GRANTEEEED");
                    SmartLocation.with(getContext()).location()
                            .oneFix()
                            .start(new OnLocationUpdatedListener() {
                                @Override
                                public void onLocationUpdated(Location location) {
                                    if(location != null){
                                        Log.d("LOCATION VAAAR", location.toString());
                                        lastLoc = location;
                                    }
                                }});
                }
        }

    }

    public class Event {
        private String title;
        private String location;
        private String date;
        private String time;
        private String URL;
        private String description;

        public Event() {
        }

        public Event(String title, String location, String date, String time, String URL, String description) {
            this.title = title;
            this.location = location;
            this.date = date;
            this.time = time;
            this.URL = URL;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}