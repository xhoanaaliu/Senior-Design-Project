package com.example.gerard.afinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private EventFragment fragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private ImageView imageView;
    private LoginFragment loginFragment;
    private SignUpFragment signupfragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        imageView = findViewById(R.id.imageView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new EventFragment();
        loginFragment = new LoginFragment();
        profileFragment = new ProfileFragment();
        settingsFragment = new SettingsFragment();
        signupfragment = new SignUpFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment, EventFragment.newInstance(), null)
                .commit();


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            NewEventFragment nextFrag = new NewEventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_my_events) {
            EventHistoryFragment eventHistoryFragment = new EventHistoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, eventHistoryFragment, "eventhistory")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_my_profile) {
            ProfileFragment pfragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, pfragment, "profile")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_change_location) {
            LocationFragment locationFragment = new LocationFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, locationFragment, "location")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_home) {
            EventFragment home = new EventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment,home,"EventFragment")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, settingsFragment, "Settings")
                    .addToBackStack(null)
                    .commit();

        }else if(id==R.id.nav_login){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, loginFragment, "Login")
                    .addToBackStack(null)
                    .commit();
        }else if(id==R.id.nav_signup){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, signupfragment, "Sign Up")
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    EventFragment home = new EventFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment,home,"EventFragment")
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_camera:
                    NewEventFragment nextFrag = new NewEventFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_change_location:
                    LocationFragment locationFragment = new LocationFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, locationFragment, "location")
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_my_events:
                    EventHistoryFragment eventHistoryFragment = new EventHistoryFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, eventHistoryFragment, "profile")
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_profile:
                    ProfileFragment pfragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, pfragment, "profile")
                            .addToBackStack(null).commit();
                    return true;
            }
            return false;
        }
    };
}