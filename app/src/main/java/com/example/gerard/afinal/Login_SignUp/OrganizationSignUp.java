package com.example.gerard.afinal.Login_SignUp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerard.afinal.Account.OrganizationInformationFragment;
import com.example.gerard.afinal.Account.UserInformationFragment;
import com.example.gerard.afinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class OrganizationSignUp extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    private LoginFragment loginFragment;
    private FirebaseAuth mAuth;
    @BindView(R.id.input_orgName)
    EditText _nameText;
    @BindView(R.id.input_orgEmail) EditText _emailText;
    @BindView(R.id.input_orgPassword) EditText _passwordText;
    @BindView(R.id.input_orgConfirmPassword) EditText _confirmpasswordText;
    @BindView(R.id.btn_orgSignUp) Button _signupButton;
    @BindView(R.id.org_link_login)
    TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_organization_information, container, false);

        ButterKnife.bind(this,view);
        mAuth = FirebaseAuth.getInstance();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
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
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _confirmpasswordText.getText().toString();



        // TODO: Implement your own signup logic here.

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
        Toast.makeText(getContext(), "Sign Up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _confirmpasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
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

    }

    @Override
    public void onClick(View v) {

    }
}
