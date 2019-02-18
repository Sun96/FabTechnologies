package com.xonetsapps.fabtechnologies.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xonetsapps.fabtechnologies.Http.HttpLink;
import com.xonetsapps.fabtechnologies.Http.InvoiceDatabase;
import com.xonetsapps.fabtechnologies.MainActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import  com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.RequestModel.Countries;
import  com.xonetsapps.fabtechnologies.RequestModel.HttpHandler;
import com.xonetsapps.fabtechnologies.RequestModel.Listadapter;
import  com.xonetsapps.fabtechnologies.SessionHandler;
import  com.xonetsapps.fabtechnologies.User;
import  com.xonetsapps.fabtechnologies.invoice.InvoiceAdapter;
import  com.xonetsapps.fabtechnologies.invoice.InvoiceMode;

import static com.android.volley.VolleyLog.TAG;
import static com.xonetsapps.fabtechnologies.activity.RequestLog.po;


/**
 * Created by Arafat on 19/06/2017.
 */

public class Invoice extends Fragment {
    private ProgressDialog pDialog;
    private SessionHandler session;
    public ListView list;
    private User user;
    private static final String KEY_STATUS = "success";
    private static final String KEY_MESSAGE = "message";
    String value;
    private String date,order,price,orderId,discount;
    public ArrayList<InvoiceMode> invo = new ArrayList<InvoiceMode>();
    TextView tx_date,tx_status,tx_amount,stotal,dtotal,total;
    public Invoice() {
        // Required empty public constructor
    }

int k;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_invoice, container, false);

        session = new SessionHandler(getContext());
        user = session.getUserDetails();
        //  welcomeText.setText("Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate());
        list = (ListView) rootView.findViewById(R.id.invoicelist);
        value = getArguments().getString("InVo");
        tx_date = (TextView) rootView.findViewById(R.id.date);
        tx_status = (TextView) rootView.findViewById(R.id.status);
        tx_amount = (TextView) rootView.findViewById(R.id.amount);
        stotal = (TextView) rootView.findViewById(R.id.sTotal);
        dtotal = (TextView) rootView.findViewById(R.id.dtotal);
        total = (TextView) rootView.findViewById(R.id.Ttotal);
        login();


        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("      Invoice");
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
        final JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("invoice_id",value );
            //request.put("User_id",user.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, new HttpLink().inVoice, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.dismiss();
                        try {
                            Log.d("SUN_invoR", "Login Response: " + response.toString());
                            //Check if user got logged in successfully
                            if (response.getString("status").equals("success")) {
                                JSONArray adata = response.getJSONArray("data");
                                for (int i=0;i<adata.length(); i++) {
                                    JSONObject data = adata.getJSONObject(i);
                                    date = data.getString("record_date");
                                    order = data.getString("record_type");
                                    price = data.getString("net_amount");
                                    discount=data.getString("discount");
                                    orderId = data.getString("transaction_id");

                                    InvoiceMode co=  new InvoiceMode(data.getString("product_title"),data.getString("vendor_name"),data.getString("qty"),data.getString("unit_price"));
                                    invo.add(co);

                                }
                                if(order.equals("1")){
                                    tx_status.setText(" Pending");
                                }
                                else if(order.equals("2")){
                                    tx_status.setText(" Complete");
                                }else
                                    tx_status.setText(" Cancelled");

                                tx_date.setText("Date: "+date);
                                tx_amount.setText(orderId);
                                stotal.setText(price);
                                dtotal.setText(discount);
                                total.setText(price);
                                InvoiceAdapter listAdapter = new InvoiceAdapter(getActivity(),invo) ;
                                list.setAdapter(listAdapter);
                            }else{
                                Toast.makeText(getContext(),
                                        /*response.getString("message")*/"not", Toast.LENGTH_SHORT).show();
                                Log.d("SUN_reqI", "Login Response: " + response.getString(KEY_MESSAGE));
                                Log.d("SUN_invo", "Login Response: " + request.toString());

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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}