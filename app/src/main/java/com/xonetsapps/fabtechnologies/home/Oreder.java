package com.xonetsapps.fabtechnologies.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xonetsapps.fabtechnologies.Http.HttpLink;
import com.xonetsapps.fabtechnologies.MySingleton;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Oreder {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private String password;
    private String confirmPassword;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private User user;
    private Activity activity;



    public Oreder(Activity activity){
        this.activity=activity;
        registerUser();
    }
    private void displayLoader() {
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void registerUser() {
        displayLoader();
        session = new SessionHandler(activity);
        user = session.getUserDetails();
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("user_id",user.getId());





        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST,new HttpLink().cRequest, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "Login Response: " + response.toString());
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getString(KEY_STATUS).equals("success")) {

                                Toast.makeText(activity,
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                //loadDashboard();

                            }else{
                                Toast.makeText(activity,
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
                                Toast.makeText(activity,
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
        MySingleton.getInstance(activity).addToRequestQueue(jsArrayRequest);
    }
}
