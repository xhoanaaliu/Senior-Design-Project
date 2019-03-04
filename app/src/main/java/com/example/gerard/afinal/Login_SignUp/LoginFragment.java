package com.example.gerard.afinal.Login_SignUp;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;

import com.example.gerard.afinal.R;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private Button loginnow;
    private Button logFacebook;
    private Button logGoogle;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sign_in_screen, container, false);

       // loginnow = (Button) rootView.findViewById(R.id.loginViaMail);
        //logFacebook = (Button) rootView.findViewById(R.id.login_button);
        //logGoogle = (Button) rootView.findViewById(R.id.login_google);

        loginnow.setOnClickListener(this);
        logFacebook.setOnClickListener(this);
        logGoogle.setOnClickListener(this);
        return rootView;
    }
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
        //mListener.onFragmentInteraction(uri);
        //}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onClick(View view){

        //THIS ACTION LISTENER WILL BE EDITED LATER
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

//OLD CODE IS BELOW... IT HAS BUGS--------FACEBOOK API

/*public class LoginFragment extends Fragment {
    private OnClickListener mListener;
    private static final String EMAIL = "email";
    private View loginView;
    private TextView textView;
    private CallbackManager callbackManager;
    public LoginFragment(){
    }
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginView = inflater.inflate(R.layout.sign_in_screen,container,false);
        final LoginButton button =  loginView.findViewById(R.id.login_button);
        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                callbackManager = CallbackManager.Factory.create();
                button.setReadPermissions(Arrays.asList(EMAIL));
                // Callback registration
                button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                FacebookResult(object);
                            }
                        });
                        Bundle bundle1=new Bundle();
                        bundle1.putString("fields","email");
                        graphRequest.setParameters(bundle1);
                        graphRequest.executeAsync();
                    }
                    @Override
                    public void onCancel() {
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }
        });
        return loginView;
    }
    //The final FacebookResult method contains all the values returned from Facebook.
    private void FacebookResult(JSONObject object) {
        textView.setText(object.toString());
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        //}
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}*/