package com.xonetsapps.fabtechnologies.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.xonetsapps.fabtechnologies.LoginActivity;
import com.xonetsapps.fabtechnologies.MainActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.RequestModel.Countries;
import com.xonetsapps.fabtechnologies.RequestModel.HttpHandler;
import com.xonetsapps.fabtechnologies.RequestModel.Listadapter;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;
import com.xonetsapps.fabtechnologies.invoice.InvoiceAdapter;
import com.xonetsapps.fabtechnologies.invoice.InvoiceMode;


/**
 * Created by Arafat on 19/06/2017.
 */

public class RequestLog extends Fragment {

    private ProgressDialog pDialog;
    private SessionHandler session;
    public ListView list;
    private User user;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    public static String po;
    private String date,order,price;
    public static ArrayList<Countries> countries = new ArrayList<Countries>();
    public RequestLog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dashboard, container, false);
        session = new SessionHandler(getContext());
        user = session.getUserDetails();
        //  welcomeText.setText("Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate());
        countries.clear();
        list = (ListView) rootView.findViewById(R.id.reqstlog);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                po=countries.get(position).getID();
                Fragment fragment = new Invoice();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Bundle args = new Bundle();
                args.putString("InVo",po);
                fragment.setArguments(args);
               /* Toast.makeText(getContext(),countries.get(position).getID(),Toast.LENGTH_SHORT).show();*/
            }
        });
        if(isOnline()){
            login();
        }else{

            new DialogBox(getActivity(), new RequestLog());

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
        getActivity().setTitle("      Request Log");
    }
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
            request.put("user_id",user.getId() );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, new HttpLink().reqLod, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.dismiss();
                        try {
                            Log.d("SUN_req", "Login Response: " + response.toString());
                            //Check if user got logged in successfully
                            if (response.getString(KEY_STATUS).equals("success")) {
                                JSONArray adata = response.getJSONArray("data");

                                if (adata.length()!=0) {
                                    for (int i = 0; i < adata.length(); i++) {
                                        JSONObject data = adata.getJSONObject(i);
                                        date = data.getString("record_date");
                                        order = data.getString("status");
                                        price = data.getString("net_amount");

                                        Countries co = new Countries(date = data.getString("id"), data.getString("record_date"), data.getString("net_amount"), data.getString("status"), data.getString("transaction_id"));
                                        countries.add(co);
                                    }
                                    Listadapter listAdapter = new Listadapter(getActivity(),countries) ;
                                    list.setAdapter(listAdapter);
                                }else
                                    Toast.makeText(getContext(),
                                            "No data found !!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.d("SUN_req", "Login Response: " + response.getString(KEY_MESSAGE));
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

                        Log.d("SUN", "Login Response: " + error.getMessage());

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
}