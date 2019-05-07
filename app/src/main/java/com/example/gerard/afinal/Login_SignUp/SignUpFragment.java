package com.example.gerard.afinal.Login_SignUp;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerard.afinal.R;
import com.example.gerard.afinal.Settings.CategoriesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    private LoginFragment loginFragment;
    private CategoriesFragment categoryfragment;
    private FirebaseDatabase database;
    private DatabaseReference myRefUser;
    private CircleImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private LoginFragment lfr;
    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_surname) EditText _surnameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_confirmpassword) EditText _confirmpasswordText;
    @BindView(R.id.btn_signup)
    ImageView _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.signup, container, false);

        ButterKnife.bind(this,view);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRefUser=database.getReference("Users");


        // Read from the database
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "onDataChange: Added information to database: \n" +
                        dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                categoryfragment = new CategoriesFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, categoryfragment, "CategoryOpen")
                        .addToBackStack(null)
                        .commit();

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the
                loginFragment = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, loginFragment, "LogIn")
                        .addToBackStack(null)
                        .commit();


            }
        });
        return view;
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _confirmpasswordText.getText().toString();
        String location="";
        String preferences ="";
        String profile_picture = "";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String name = _nameText.getText().toString();
                        String surname = _surnameText.getText().toString();
                        String email = _emailText.getText().toString();
                        String password = _passwordText.getText().toString();
                        String confirmpassword = _confirmpasswordText.getText().toString();
                        String location="";
                        String preferences ="";
                        String profile_picture = "";
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            NormalUser u1 = new NormalUser();
                            u1.setUsername(name);
                            u1.setUsersurname(surname);
                            u1.setEmail(email);
                            u1.setPassword(password);
                            u1.setLocation("");
                            u1.setPreferences("");
                            u1.setProfile_picture("");
                            u1.setUser_id(userID);
                            myRefUser.child(userID).setValue(u1);

                            //myRefUser.push().setValue(u1);
                        } else {
                            // If sign in fails, display a message to the user.
                           /* Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(getContext(), "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });






        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();

                        loginFragment = new LoginFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, loginFragment, "LogIn")
                                .addToBackStack(null)
                                .commit();

                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        getActivity().setResult(RESULT_OK, null);

    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Sign Up Failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _confirmpasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (surname.isEmpty() || surname.length() < 3) {
            _surnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _surnameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (confirmpassword.isEmpty() || confirmpassword.length() < 4 || confirmpassword.length() > 10) {
            _confirmpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {

            if(!(_passwordText.equals(_confirmpasswordText))){
                _confirmpasswordText.setError("Passwords do not match!");
            }
            _confirmpasswordText.setError(null);
        }


        return valid;
    }



    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRefUser = database.getReference("Users");
        profilePic = view.findViewById(R.id.profile_image);
        lfr=new LoginFragment();
    }
    @Override
    public void onClick(View v) {

    }
}