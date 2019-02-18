package com.xonetsapps.fabtechnologies.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xonetsapps.fabtechnologies.Http.DialogBox;
import com.xonetsapps.fabtechnologies.Http.HttpLink;
import  com.xonetsapps.fabtechnologies.Http.HttpParse;
import com.xonetsapps.fabtechnologies.LoginActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import  com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import static com.xonetsapps.fabtechnologies.activity.Profile.user;


public class UpdateActivity extends Fragment {

    EditText name, number, addres,email,comName;
    Button UpdateStudent;
    String a1,a2,a3,a4,a5,a6;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private ProgressDialog pDialog;
    private SessionHandler session;


    public UpdateActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_update, container, false);
        name = (EditText)rootView.findViewById(R.id.editName);
        number = (EditText)rootView.findViewById(R.id.editPhoneNumber);
        addres = (EditText)rootView.findViewById(R.id.editAdd);
        email = (EditText)rootView.findViewById(R.id.editemail);
        comName = (EditText)rootView.findViewById(R.id.editComName);
        UpdateStudent = (Button)rootView.findViewById(R.id.UpdateButton);
        session = new SessionHandler(getContext());
        // Receive Student ID, Name , Phone Number , Class Send by previous ShowSingleRecordActivity.


        // Setting Received Student Name, Phone Number, Class into EditText.
        name.setText(user.getName());
        email.setText(user.getEmail());
        number.setText(user.getMobile());
        addres.setText(user.getAddress());
        comName.setText(user.getCompany());
       /* session.loginUser(user.getId(),name.getText().toString(), email.getText().toString(),
                number.getText().toString(),addres.getText().toString(),user.getToken());*/

        a1=user.getId();
        a2=name.getText().toString();
        a3=email.getText().toString();
        a4=number.getText().toString();
        a5=addres.getText().toString();
        a6=user.getToken();
        // Adding click listener to update button .

        if(isOnline()){
            UpdateStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });

        }else{

            new DialogBox(getActivity(), new UpdateActivity());

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    // Method to get existing data from EditText.
    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("user_id", user.getId());
            request.put("name", name.getText().toString());
            request.put("email", email.getText().toString());
            request.put("mobile", number.getText().toString());
            request.put("address", addres.getText().toString());
            request.put("company_name",comName.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, new HttpLink().update, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getString(KEY_STATUS).equals("success")) {
                                session.loginUser(user.getId(),name.getText().toString(), email.getText().toString(),
                                        number.getText().toString(),addres.getText().toString(),user.getToken(),comName.getText().toString());
                                //session.loginUser(a1,a2,a3,a4,a5,a6);
                                Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.d("SUN", "Login Response: " + response.getString(KEY_MESSAGE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

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


}
