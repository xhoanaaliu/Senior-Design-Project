package com.example.gerard.afinal.Login_SignUp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerard.afinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.support.test.InstrumentationRegistry.getContext;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private EditText _nameText;
    private EditText _surnameText;
    private EditText _emailText;
    private EditText _passwordText;
    private EditText _confirmpasswordText;
    private ImageView _signupButton;
    private TextView _loginLink;

    private ProgressBar progressBar;
    private DatabaseReference mDatabase;

    String name;
    String surname;
    String email;
    String password;
    String confirmpassword;
    String location;
    String preferences;
    String profile_picture ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        Toast.makeText(this, "ALOO3", Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);
        _nameText = findViewById(R.id.input_name);
        _surnameText = findViewById(R.id.input_surname);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _confirmpasswordText = findViewById(R.id.input_confirmpassword);
        progressBar = findViewById(R.id.progressBar);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the
               Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
               startActivity(i);

            }
        });

    }

    public void signup() {

        if (!validate()) {
            //onSignupFailed();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

         name = _nameText.getText().toString();
         surname = _surnameText.getText().toString();
         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();
         confirmpassword = _confirmpasswordText.getText().toString();
         location = "";
         preferences = "";
         profile_picture = "";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);
                            onAuthSuccess(task.getResult().getUser());
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();

                        } else {

                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(SignUpActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }
    private void onAuthSuccess(FirebaseUser user) {

        // Write new user
        writeNewUser(user.getUid(), user.getEmail());

    }

    private void writeNewUser(String userId, String email) {

        Map<String, String> user = new HashMap<>();
        user.put("username", name);
        user.put("surname", surname);
        user.put("email", email);
        user.put("password", password);
        user.put("location", location);

        mDatabase.child("Users").child(userId).setValue(user);
    }

}
