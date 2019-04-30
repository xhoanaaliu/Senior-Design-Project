package com.example.gerard.afinal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

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
    private boolean gotLocation = false;
    private Map<LatLng, Event> markerList = new HashMap<>();

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

                                        HomePage homePage = new HomePage();
                                        homePage.setArguments(bundle);

                                        AppCompatActivity activity = (AppCompatActivity) getContext();
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, homePage)
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String curr = sdf.format(date);


            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home_page, container, false);
            TextView currDate = view.findViewById(R.id.textView9);
            currDate.setText(curr + " Events");

            TextView locationName = view.findViewById(R.id.textView7);
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

            mMapView = view.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView.getMapAsync(this);


            //mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://resinventa.appspot.com/");
            mStorageRef = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("Event");
            events_retrieved = new ArrayList<>();
            imageList = new ArrayList<>();

            recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle);
            adapter = new MyAdapter(events_retrieved, getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

                    Event temp = new Event(title, location, date, URL, description);

                    Geocoder geocoder1 = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder1.getFromLocationName(location, 10);
                    }
                    catch (IOException io){
                        Log.d("Service", "Unavailable");
                    }

                    if (!addresses.isEmpty()){
                        addMarker(googleMap, addresses.get(0).getLatitude(), addresses.get(0).getLongitude(), temp);
                        Log.d("THIS","REACHEDDDDDDDDDD");
                    }

                    events_retrieved.add(temp);
                    adapter.notifyDataSetChanged();
                    Log.d("DATASET", "CHANGED");
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
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        if(lastLoc != null) {
            curr = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            Log.d("KONUM VAAAAAR", "" + lastLoc.getLatitude() + "" + lastLoc.getLongitude());
        }
        else {
            curr = new LatLng(lat, lng);
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(curr).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //addMarker(googleMap, curr.latitude, curr.longitude);

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
    public boolean onMarkerClick(Marker marker) {
        LatLng position = (LatLng) marker.getTag();

        if(markerList.containsKey(position)){
            Event temp = markerList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("title", temp.getTitle());
            bundle.putString("location", temp.getLocation());
            bundle.putString("date", temp.getDate());
            bundle.putString("description", temp.getDescription());
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
        private String title;
        private String location;
        private String date;
        private String URL;
        private String description;

        public Event(String title, String location, String date, String URL, String description) {
            this.title = title;
            this.location = location;
            this.date = date;
            this.URL = URL;
            this.description = description;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
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
