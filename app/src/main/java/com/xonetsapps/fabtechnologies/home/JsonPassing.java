package com.xonetsapps.fabtechnologies.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xonetsapps.fabtechnologies.Http.InvoiceDatabase;
import com.xonetsapps.fabtechnologies.LoginActivity;
import com.xonetsapps.fabtechnologies.MainActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import com.xonetsapps.fabtechnologies.PersonDatabase;
import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;
import com.xonetsapps.fabtechnologies.activity.Invoice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonPassing {
    Context context;
    String totalp;

    public JsonPassing(Context context,String totalp) {
        this.context=context;
        this.totalp=totalp;
        //login();
    }
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    public  String KEY_id;
    private String KEY_token;
    private static final String KEY_USERNAME = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private String login_url = "http://sonicrh.com/sales_api/public/api/v1/save_product_info";
    private SessionHandler session;
    PersonDatabase mydb;
    ArrayList<String> proList,vendorList,qtyList,priceList,idList;
    InvoiceDatabase indb;
    private void displayLoader() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void loadDashboard() {
        idList = new ArrayList<>();
        mydb = new PersonDatabase(context);
        Cursor data = mydb.getAllData();
        while(data.moveToNext()){
            idList.add(data.getString(0));
        }
        for (int l=0;l<data.getCount();l++){
        mydb.deleteData(idList.get(l));}

    }
    public void login() {
        displayLoader();
        proList = new ArrayList<>();
        qtyList = new ArrayList<>();
        vendorList = new ArrayList<>();
        priceList = new ArrayList<>();
        mydb = new PersonDatabase(context);
        indb = new InvoiceDatabase(context);
        Cursor data = mydb.getAllData();
        while(data.moveToNext()){
            proList.add(data.getString(1));
            vendorList.add(data.getString(2));
            qtyList.add(data.getString(3));
            priceList.add(data.getString(4));
        }

        session = new SessionHandler(context);
        User user = session.getUserDetails();

        final JSONObject mainrequest = new JSONObject();
        try {
            mainrequest.put("status", "success");
            mainrequest.put("message", "Successfully send data");


            JSONObject request = new JSONObject();
            try {
                //Populate the request parameters
                request.put("user_id", user.getId());
                request.put("invoice_amount", totalp);
                request.put("discount", 0);
                request.put("net_amount", 0);
                JSONArray array = new JSONArray();
                for (int i = 0; i < proList.size(); i++) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("prouct_id", proList.get(i));
                        obj.put("vendor_id", vendorList.get(i));
                        obj.put("qty", qtyList.get(i));
                        obj.put("unit_price", priceList.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);
                }
                request.put("product_info", array);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mainrequest.put("data", request);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, mainrequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Json_SUN", "Login Response: " + mainrequest.toString());
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getString(KEY_STATUS).equals("success")) {
                                JSONObject data = response.getJSONObject("data");
                               // KEY_id=data.getString("invoice_id").toString();
                                indb.insertdata(data.getString("invoice_id"));
                                Log.d("invoice_id", "Login Response: " +data.getString("invoice_id"));
                                loadDashboard();

                                Toast.makeText(context,
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(context,
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                loadDashboard();
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
                        loadDashboard();
                        //Display error message whenever an error occurs
                        Toast.makeText(context,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("SUN", "Login Response: " + error.getMessage());
                        Log.d("Json_SUN", "Login Response: " + mainrequest.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }

}
