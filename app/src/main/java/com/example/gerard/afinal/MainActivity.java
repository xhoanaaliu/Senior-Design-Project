package com.example.gerard.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerard.afinal.Account.ProfileFragment;
import com.example.gerard.afinal.Login_SignUp.LoginFragment;
import com.example.gerard.afinal.Login_SignUp.SignUpFragment;
import com.example.gerard.afinal.Settings.SettingsFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private EventFragment fragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private ImageView imageView;
    private LoginFragment loginFragment;
    private SignUpFragment signupfragment;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        loginFragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, loginFragment, "LoginFragment")
                .addToBackStack(null)
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

        }
        else if (id==R.id.nav_logout){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, loginFragment, "LoginFragment")
                    .addToBackStack(null)
                    .commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


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

                   dispatchTakePictureIntent();

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.gerard.afinal.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            galleryAddPic();
            NewEventFragment newEventFragment = new NewEventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, newEventFragment, "location")
                    .addToBackStack(null).commit();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


}