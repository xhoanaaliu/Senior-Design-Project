package com.example.gerard.afinal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;
import com.example.gerard.afinal.Account.ProfileFragment;
import com.example.gerard.afinal.Login_SignUp.LoginActivity;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageSource;
import com.google.api.services.vision.v1.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private HomePage fragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private ImageView imageView;
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
    private final int REQUEST_LOCATION_CODE = 99;
    private final int REQUEST_LOCATION_CODE2 = 98;
    Location lastLoc;
    boolean gotLocation = false;
    private Toolbar toolbar;
    String API_KEY = "5d9a93f99b0aab73d3f8be94453d6d83f3ea2b193b7780ca43751893";
    HashMap<String, String> u;

    Bitmap bitmap;
    NavigationView navigationView;
    public static final int PICK_IMAGE = 98;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dataref = FirebaseDatabase.getInstance().getReference();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("REQUEST LOCATION", "NOT GRANTED");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE2);

        }
        if(!gotLocation){
                SmartLocation.with(this).location()
                        .oneFix()
                        .start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(Location location) {
                                if (location != null) {
                                    Log.d("LOCATION VAAAR22222", location.toString());
                                    lastLoc = location;
                                    gotLocation = true;
                                }
                            }
                        });
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            //@Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


            }
        };
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        imageView = findViewById(R.id.imageView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragment = new HomePage();
        profileFragment = new ProfileFragment();
        settingsFragment = new SettingsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragment, "Homepage")
                .addToBackStack(null)
                .commit();

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (f instanceof EventFragment) {
            navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else if (f instanceof NewEventFragment) {
            navigation.getMenu().findItem(R.id.navigation_camera).setChecked(true);
        } else if (f instanceof ProfileFragment) {
            navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
        } else if (f instanceof LocationFragment) {
            navigation.getMenu().findItem(R.id.navigation_change_location).setChecked(true);
        } else if (f instanceof EventHistoryFragment) {
            navigation.getMenu().findItem(R.id.navigation_my_events).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (f instanceof EventFragment) {
            navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } else if (f instanceof NewEventFragment) {
            navigation.getMenu().findItem(R.id.navigation_camera).setChecked(true);
        } else if (f instanceof ProfileFragment) {
            navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
        } else if (f instanceof LocationFragment) {
            navigation.getMenu().findItem(R.id.navigation_change_location).setChecked(true);
        } else if (f instanceof EventHistoryFragment) {
            navigation.getMenu().findItem(R.id.navigation_my_events).setChecked(true);
        }

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
            Bundle bundle = new Bundle();
            if(lastLoc != null){
                bundle.putDouble("latitude", lastLoc.getLatitude());
                bundle.putDouble("longtitude", lastLoc.getLongitude());
                locationFragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, locationFragment, "location")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_home) {
            HomePage home = new HomePage();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, home, "HomeFragment")
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, settingsFragment, "Settings")
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();

                }
            }).executeAsync();


            Intent i1 = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i1);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
            //Handle bottom navigation view clicks here.
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomePage home = new HomePage();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, home, "HomeFragment")
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_camera:

                    new TTFancyGifDialog.Builder(MainActivity.this)
                            .setTitle("Choose Method")
                            .setMessage("Please select from where you want to take your poster !")
                            .setPositiveBtnBackground("#66000000")
                            .setPositiveBtnText("Open Camera")
                            .setNegativeBtnBackground("#66000000")
                            .setNegativeBtnText("Open Gallery")
                            .setGifResource(R.drawable.gif13)   //Pass your Gif here
                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    dispatchTakePictureIntent();
                                }
                            })
                            .OnNegativeClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {

                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, PICK_IMAGE);
                                }
                            })
                            .build();
                    return true;
                case R.id.navigation_change_location:
                    LocationFragment locationFragment = new LocationFragment();
                    Bundle bundle = new Bundle();
                    if(lastLoc != null){
                        bundle.putDouble("latitude", lastLoc.getLatitude());
                        bundle.putDouble("longtitude", lastLoc.getLongitude());
                        locationFragment.setArguments(bundle);
                    }
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                Toast.makeText(this,"Your poster is being processed",Toast.LENGTH_SHORT).show();
                uploadImage(photoURI);

        }

        if(requestCode == PICK_IMAGE  && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            Toast.makeText(this,"Your poster is being processed",Toast.LENGTH_SHORT).show();
            uploadImage(selectedImage);

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
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            case REQUEST_LOCATION_CODE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
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
                bitmap = resizeBitmap(getApplicationContext(), uri);

                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if(!isConnected){
                    new TTFancyGifDialog.Builder(MainActivity.this)
                            .setTitle("No Internet Connection")
                            .setMessage("Please check your internet connection and try again")
                            .setPositiveBtnBackground("#32CD32")
                            .setPositiveBtnText("OK")
                            .setGifResource(R.drawable.no_internet)   //Pass your Gif here
                            .OnPositiveClicked(new TTFancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    //Toast.makeText(MainActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                    HomePage hm = new HomePage();
                                     getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.main_fragment, hm, "no internet")
                                            .addToBackStack(null).commit();
                                }
                            })
                            .build();
                }else {

                    imageLabeling(bitmap);
                    callCloudVision(bitmap);
                }
                //selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "Null image was returned.");
        }
    }

    public static Bitmap resizeBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
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
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

                    Feature web_feature = new Feature();
                    web_feature.setType("WEB_DETECTION");
                    web_feature.setMaxResults(10);
                    featureList.add(web_feature);


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

                System.out.println(result);

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

        message.append("Texts:\n");

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

        message.append("Labels:\n");

        List<EntityAnnotation> labels = response.getResponses().get(0)
                .getLabelAnnotations();

        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        label.getScore(), label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        message.append("Landmarks:\n");
        List<EntityAnnotation> landmarks = response.getResponses().get(0)
                .getLandmarkAnnotations();
        if (landmarks != null) {
            for (EntityAnnotation landmark : landmarks) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        landmark.getScore(), landmark.getDescription()));
                message.append("\n");
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
                    //System.out.println(result);
                    TextRazor client = new TextRazor(API_KEY);

                    client.addExtractor("words");
                    client.addExtractor("entities");

                    AnalyzedText response = client.analyze(result);

                    u = new HashMap<>();

                    if(response.getResponse().getEntities() != null) {
                        for (Entity entity : response.getResponse().getEntities()) {
                            if( entity.getDBPediaTypes() != null) {
                                System.out.println(entity.getEntityId() + ": " + entity.getDBPediaTypes().get(0));
                                u.put(entity.getEntityId(), entity.getDBPediaTypes().get(0));
                            }
                        }
                    }

                    return u;
                } catch (NetworkException e) {

                } catch (AnalysisException a) {
                    a.printStackTrace();
                }

                return null;

            }
            protected void onPostExecute(HashMap u) {

                final NewEventFragment new_event = new NewEventFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("hashmap",u);
                bundle.putParcelable("BitmapImage",bitmap);
                new_event.setArguments(bundle);

                new TTFancyGifDialog.Builder(MainActivity.this)
                        .setTitle("Poster Processed")
                        .setMessage("Your Poster has been processed. Press OK to be redirected to the event page.")
                        .setPositiveBtnBackground("#32CD32")
                        .setPositiveBtnText("OK")
                        .setGifResource(R.drawable.gif8)   //Pass your Gif here
                        .OnPositiveClicked(new TTFancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //Toast.makeText(MainActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fragment, new_event, "new_event")
                                        .addToBackStack(null).commit();
                            }
                        })
                        .build();
            }
        }.execute();
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
       /* private static Bitmap rotateImage(Bitmap img, int degree) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
    }
*/
       public void imageLabeling(Bitmap bitmap){
           FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
           //FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

           FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                   .getCloudImageLabeler();

        // Or, to set the minimum confidence required:
        // FirebaseVisionCloudImageLabelerOptions options =
        //     new FirebaseVisionCloudImageLabelerOptions.Builder()
        //         .setConfidenceThreshold(0.7f)
        //         .build();
        // FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
        //     .getCloudImageLabeler(options);



           labeler.processImage(image)
                   .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                       @Override
                       public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                           // Task completed successfully
                           // ...
                           for (FirebaseVisionImageLabel label: labels) {
                               String text = label.getText();
                               String entityId = label.getEntityId();
                               float confidence = label.getConfidence();
                               System.out.println("Text: " + text +  " Confidence: " + confidence);
                           }
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           // Task failed with an exception
                           // ...
                       }
                   });
       }

    /**
     * Detects whether the remote image on Google Cloud Storage has features you would want to
     * moderate.
     *
     * @param gcsPath The path to the remote on Google Cloud Storage file to detect web annotations.
     * @param out A {@link PrintStream} to write the results to.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
   /* public static void detectWebDetectionsGcs(String gcsPath, PrintStream out) throws Exception,
            IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.Builder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Type.WEB_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // Search the web for usages of the image. You could use these signals later
                // for user input moderation or linking external references.
                // For a full list of available annotations, see http://g.co/cloud/vision/docs
                WebDetection annotation = res.getWebDetection();
                out.println("Entity:Id:Score");
                out.println("===============");
                for (WebEntity entity : annotation.getWebEntitiesList()) {
                    out.println(entity.getDescription() + " : " + entity.getEntityId() + " : "
                            + entity.getScore());
                }
                for (WebLabel label : annotation.getBestGuessLabelsList()) {
                    out.format("\nBest guess label: %s", label.getLabel());
                }
                out.println("\nPages with matching images: Score\n==");
                for (WebPage page : annotation.getPagesWithMatchingImagesList()) {
                    out.println(page.getUrl() + " : " + page.getScore());
                }
                out.println("\nPages with partially matching images: Score\n==");
                for (WebImage image : annotation.getPartialMatchingImagesList()) {
                    out.println(image.getUrl() + " : " + image.getScore());
                }
                out.println("\nPages with fully matching images: Score\n==");
                for (WebImage image : annotation.getFullMatchingImagesList()) {
                    out.println(image.getUrl() + " : " + image.getScore());
                }
                out.println("\nPages with visually similar images: Score\n==");
                for (WebImage image : annotation.getVisuallySimilarImagesList()) {
                    out.println(image.getUrl() + " : " + image.getScore());
                }
            }
        }
    }*/

}