package com.example.gerard.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.gerard.afinal.Account.ProfileFragment;
import com.example.gerard.afinal.Login_SignUp.LoginFragment;
import com.example.gerard.afinal.Login_SignUp.SignUpFragment;
import com.example.gerard.afinal.OCR.PackageManagerUtils;
import com.example.gerard.afinal.OCR.PermissionUtils;
import com.example.gerard.afinal.Settings.SettingsFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private HomePage fragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private ImageView imageView;
    private LoginFragment loginFragment;
    private SignUpFragment signupfragment;
    private GoogleApiClient mGoogleApiClient;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String mCurrentPhotoPath;
    BottomNavigationView navigation;
    private DatabaseReference dataref;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    Uri photoURI;
    private final String LOG_TAG = "MainActivity";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCvkaYZjqjAm9jgQyiS0pMr-CE6f3ZVExU";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";

    String API_KEY = "5d9a93f99b0aab73d3f8be94453d6d83f3ea2b193b7780ca43751893";
    HashMap<String, String> u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
       database = FirebaseDatabase.getInstance();
        dataref = FirebaseDatabase.getInstance().getReference();


        /*
        Map<String, String> em = new HashMap<>();
        em.put("category","EE");
        em.put("title" , "IEEE meeting");
        em.put("location" , "Bilkent");
        em.put("date" , "15 March");
        em.put("description","Robotics");
        em.put("time","19:30");
        dataref.child("Event").child("1").setValue(em);
        */

        //**************************************************************************************************//
        dataref = FirebaseDatabase.getInstance().getReference();

        Map<String, String> user = new HashMap<>();
        user.put("email" , "test email");
        user.put("password" , "test password");
        user.put("name" , "test name");
        user.put("surname" , "test surname");
        user.put("birthday" , "test birthday");
        user.put("picture" , "test picture");
        dataref.child("User").child("user 1").setValue(user);

        Map<String, String> be_friend = new HashMap<>();
        be_friend.put("email_user" , "test email user");
        be_friend.put("email_friend" , "test email friend");
        dataref.child("BeFriend").child("be friend 1").setValue(be_friend);

        Map<String, String> follow = new HashMap<>();
        follow.put("email_user" , "test email user");
        follow.put("email_organization" , "test email organization");
        dataref.child("Follow").child("follow 1").setValue(follow);

        Map<String, String> interest = new HashMap<>();
        interest.put("interest_type" , "test interest type");
        interest.put("ineterest_name" , "test interest name");
        dataref.child("Interest").child("interest 1").setValue(interest);

        Map<String, String> interest_event = new HashMap<>();
        interest_event.put("interest_event_id" , "test interest event");
        interest_event.put("ineterest_id" , "test interest");
        dataref.child("InterestEvent").child("interest event 1").setValue(interest_event);

        Map<String, String> interest_user = new HashMap<>();
        interest_user.put("interest_user_id" , "test interest user");
        interest_user.put("ineterest_id" , "test interest");
        dataref.child("InterestUser").child("interest user 1").setValue(interest_user);

        Map<String, String> organization = new HashMap<>();
        organization.put("organization_email" , "test organization_email");
        organization.put("organization_name" , "test organization name");
        dataref.child("Organization").child("organization 1").setValue(organization);

        Map<String, String> interested_in = new HashMap<>();
        interested_in.put("user_id" , "test user");
        interested_in.put("event_id" , "test event");
        dataref.child("InterestedIn").child("ineterested in 1").setValue(interested_in);

        Map<String, String> participated_in = new HashMap<>();
        participated_in.put("user_id" , "test user");
        dataref.child("ParticipatedIn").child("participated in 1").setValue(participated_in);

        Map<String, String> going_to = new HashMap<>();
        going_to.put("user_id" , "test user");
        going_to.put("event_id" , "test event");
        dataref.child("GoingTo").child("going to 1").setValue(going_to);

        //**************************************************************************************************//


        mAuthListener= new FirebaseAuth.AuthStateListener() {
           //@Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            /*    if(firebaseAuth.getCurrentUser()==null){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, loginFragment, "LoginFragment")
                            .addToBackStack(null)
                            .commit();
                }*/
           }
       };
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        imageView = findViewById(R.id.imageView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new HomePage();
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

        super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if(f instanceof EventFragment) {
            navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else if (f instanceof NewEventFragment){
            navigation.getMenu().findItem(R.id.navigation_camera).setChecked(true);
        } else if (f instanceof ProfileFragment){
            navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
        } else if (f instanceof LocationFragment){
            navigation.getMenu().findItem(R.id.navigation_change_location).setChecked(true);
        } else if (f instanceof EventHistoryFragment){
            navigation.getMenu().findItem(R.id.navigation_my_events).setChecked(true);
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
            HomePage home = new HomePage();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment,home,"HomeFragment")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, settingsFragment, "Settings")
                    .addToBackStack(null)
                    .commit();

        }
       else if (id==R.id.nav_logout){
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();

                }
            }).executeAsync();

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
                    HomePage home = new HomePage();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment,home,"HomeFragment")
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
                 photoURI = FileProvider.getUriForFile(this,
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
            if(requestCode == REQUEST_IMAGE_CAPTURE ) {
                uploadImage(photoURI);
            }
           /* NewEventFragment newEventFragment = new NewEventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, newEventFragment, "location")
                    .addToBackStack(null).commit();*/

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                //PermissionUtils Class to be Added
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    dispatchTakePictureIntent();
                }
                break;
          /*  case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    //startGalleryChooser();
                }
                break;*/
        }

    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                Bitmap bitmap = resizeBitmap(
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                callCloudVision(bitmap);
                //selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "Null image was returned.");
        }
    }
    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {

        //resultTextView.setText("Retrieving results from cloud");

        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                   /* Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);*/

                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);


                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(LOG_TAG, "sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.e(LOG_TAG, "Request failed: " + e.getContent());
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Request failed: " + e.getMessage());
                }
                return "Cloud Vision API request failed.";
            }

            protected void onPostExecute(String result) {

                getAuth(result);
            }
        }.execute();
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        StringBuilder message = new StringBuilder();

        //message.append("Texts:\n");

        List<EntityAnnotation> texts = response.getResponses().get(0)
                .getTextAnnotations();

        if (texts != null) {
            for (EntityAnnotation text : texts) {
                if(text.getLocale() != null) {
                    message.append(String.format(Locale.getDefault(), "%s: %s",
                            text.getLocale(), text.getDescription()));
                    message.append("\n");
                }
            }
        } else {
            message.append("nothing\n");
        }
        return message.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void getAuth(final String result) {

        new AsyncTask<Void, Void, HashMap>() {
            @Override
            protected HashMap doInBackground(Void... params) {
                try {

                    AccountManager manager = new AccountManager(API_KEY);
                    Account account = manager.getAccount();
                    System.out.println("Your current account plan is " + account.getPlan() + ", which includes " + account.getPlanDailyRequestsIncluded() + " daily requests, " + account.getRequestsUsedToday() + " used today");
                    System.out.println(result);
                    TextRazor client = new TextRazor(API_KEY);

                    client.addExtractor("words");
                    client.addExtractor("entities");

                    AnalyzedText response = client.analyze(result);

                    u = new HashMap<>();

                    for (Entity entity : response.getResponse().getEntities()) {
                        //System.out.println(entity.getEntityId() + ": " + entity.getDBPediaTypes().get(0));
                        if(entity.getEntityId() == null || entity.getDBPediaTypes().get(0) == null){
                            return null;
                        }else {
                            u.put(entity.getEntityId(), entity.getDBPediaTypes().get(0));
                        }
                    }
                    return u;


                } catch (NetworkException e) {
                    e.printStackTrace();
                } catch (AnalysisException a) {
                    a.printStackTrace();
                }

                return null;

            }
            protected void onPostExecute(HashMap u) {

                NewEventFragment new_event = new NewEventFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("hashmap",u);
                new_event.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, new_event, "new_event")
                        .addToBackStack(null).commit();
            }
        }.execute();
    }




}