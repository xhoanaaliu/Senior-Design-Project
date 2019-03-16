package com.example.gerard.afinal.Login_SignUp;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.app.ProgressDialog;

import com.example.gerard.afinal.Account.ProfileFragment;
import com.example.gerard.afinal.EventFragment;
import com.example.gerard.afinal.R;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.v7.app.AppCompatActivity;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private EventFragment event = new EventFragment();
    private SignUpFragment signUpFragment;
    private OrganizationSignUp organizationSignUp;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;

    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_signuporganization) TextView _orgsignupLink;
     public LoginFragment(){

      }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrl = new ScrollView(getContext());
        scrl.setId(R.id.sign_in);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.sign_in_screen, container, false);
        ButterKnife.bind(this,view);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Start the Signup activity
                signUpFragment = new SignUpFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, signUpFragment, "SignUp")
                        .addToBackStack(null)
                        .commit();
            }
        });
        _orgsignupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Start the Signup activity
                organizationSignUp = new OrganizationSignUp();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment, organizationSignUp, "SignUp")
                        .addToBackStack(null)
                        .commit();
            }
        });
      return view;

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();





        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();

                        // onLoginFailed();
                        progressDialog.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, event, "Home")
                                .addToBackStack(null)
                                .commit();
                    }
                }, 3000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                getActivity().finish();
            }
        }
    }


    public void onBackPressed() {
        // disable going back to the MainActivity
        getActivity().moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //getActivity().finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("You should write between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
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