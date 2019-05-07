package com.example.gerard.afinal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static android.support.v7.widget.RecyclerView.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private MapView mMapView;
    private GoogleMap googleMap;
    public static double lat = 39.875275;
    public static double lng = 32.748524;
    private static List<Event> events_retrieved;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private StorageReference mStorageRef;
    private List<Bitmap> imageList;
    private final int REQUEST_LOCATION_CODE = 99;
    private final int REQUEST_LOCATION_CODE2 = 98;
    private Location lastLoc;
    private Map<LatLng, Event> markerList = new HashMap<>();
    private Date currentDate = new Date();

    public HomePage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance() {
        HomePage fragment = new HomePage();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "NOT GRANTED");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE2);
            //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
        }
        else{
            SmartLocation.with(getContext()).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            if (location != null) {
                                lastLoc = location;
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            }
                        }
                    });
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home_page, container, false);
            //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            //Date date = new Date();
            //String curr = sdf.format(date);

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("REQUEST LOCATION", "NOT GRANTED");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE2);
                //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            else{
                SmartLocation.with(getContext()).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                if (location != null) {
                                    lastLoc = location;
                                    lat = location.getLatitude();
                                    lng = location.getLongitude();
                                }
                            }
                        });
            }

            TextView locationName = view.findViewById(R.id.textView10);
            final Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addressList = new ArrayList<>();
            try {
                addressList = geocoder.getFromLocation(lat, lng, 10);
                Log.d("Address", addressList.get(0).toString());
            }
            catch (IOException io){
                Log.d("IO", "Service is not available");
            }

            if(!addressList.isEmpty()) {
                locationName.setText(addressList.get(0).getAdminArea());
            }

            TextView currDate = view.findViewById(R.id.textView9);
            currDate.setText("Recent Events");

            mStorageRef = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("Event");
            events_retrieved = new ArrayList<>();
            imageList = new ArrayList<>();

            recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle);
            adapter = new MyAdapter(events_retrieved, getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
            recyclerView.addItemDecoration(decoration);
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
                    String ID = dataSnapshot.getKey();
                    String creatorID = value.get("user_id");

                    Event temp = new Event(title, location, date, time, URL, description, category, ID, creatorID);

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

                    SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL");
                    String a = dateFormat.format(currDate);
                    Log.d("DATE", a);

                    if((days < 15) && (days != 0)){
                        events_retrieved.add(temp);
                        adapter.notifyDataSetChanged();
                        Log.d("DATASET", "CHANGED");
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class Event {
        private String ID;
        private String title;
        private String location;
        private String date;
        private String time;
        private String URL;
        private String description;
        private String category;
        private String creatorID;

        public Event() {
        }

        public Event(String title, String location, String date, String time, String URL, String description, String category, String ID, String creatorID) {

            this.title = title;
            this.location = location;
            this.date = date;
            this.time = time;
            this.URL = URL;
            this.description = description;
            this.category = category;
            this.ID = ID;
            this.creatorID = creatorID;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCreatorID() {
            return creatorID;
        }

        public void setCreatorID(String creatorID) {
            this.creatorID = creatorID;
        }
    }
}
