package com.xonetsapps.fabtechnologies.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xonetsapps.fabtechnologies.Http.DialogBox;
import com.xonetsapps.fabtechnologies.Http.HttpLink;
import com.xonetsapps.fabtechnologies.MainActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.RegisterActivity;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PasswordChange extends Fragment {
    private EditText etPassword;
    private EditText etConfirmPassword;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private String password;
    private String confirmPassword;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private User user;
    public PasswordChange() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_password_change, container, false);
        final FragmentActivity c = getActivity();
        session = new SessionHandler(getContext());
        user = session.getUserDetails();
        etPassword = rootView.findViewById(R.id.password);
        etConfirmPassword = rootView.findViewById(R.id.confirmPassword);
        Button register = rootView.findViewById(R.id.updateButton);

        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if(isOnline()){
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Retrieve the data entered in the edit texts

                    if (validateInputs()) {
                        registerUser();
                    }

                }
            });
        }else{

            new DialogBox(getActivity(), new PasswordChange());

        }
        return rootView;
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("    Password Change");
    }
    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */

    private void registerUser() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("user_id",user.getId());
            request.put("password",etPassword.getText().toString());
            request.put("confirm_password",etConfirmPassword.getText().toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST,new HttpLink().passChange, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "Login Response: " + response.toString());
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getString(KEY_STATUS).equals("success")) {

                                 Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                //loadDashboard();

                            }else{
                                Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Toast.makeText(getContext(),
                                        obj.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(etPassword.getText().toString())) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Confirm Password cannot be empty");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Password and Confirm Password does not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}



