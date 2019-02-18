package com.xonetsapps.fabtechnologies.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.xonetsapps.fabtechnologies.Http.InvoiceDatabase;
import com.xonetsapps.fabtechnologies.MainActivity;
import com.xonetsapps.fabtechnologies.MySingleton;
import com.xonetsapps.fabtechnologies.PersonDatabase;
import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.RequestModel.HttpHandler;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;
import com.xonetsapps.fabtechnologies.home.HomeAdapter;
import com.xonetsapps.fabtechnologies.home.HomeMode;
import com.xonetsapps.fabtechnologies.home.JsonPassing;
import com.xonetsapps.fabtechnologies.home.Oreder;


/**
 * Created by Arafat on 19/06/2017.
 */

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public TextView textView;
    private String TAG = HomeFragment.class.getSimpleName();
    public RecyclerView lvDetail;
    public ArrayList<HomeMode> myList = new ArrayList<HomeMode>();
    private ProgressDialog pDialog;
    private int kp;
    String name, pId;
    public TextView tresult;
    Button button,button2;
    Double sum;
    public RelativeLayout relativeLayout;
    InvoiceDatabase indb;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private SessionHandler session;
    PersonDatabase mydb;
    ArrayList<String> proList, vendorList, qtyList, priceList, idList2;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        final FragmentActivity c = getActivity();
        button = (Button) rootView.findViewById(R.id.buy);
        button2 = (Button) rootView.findViewById(R.id.buy2);
        sum = 0.0;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sum > 0.0) {
                    open();
                } else
                    Toast.makeText(getContext(), " You didn't order anything! Please Order something ", Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Are you want to Create Customize Order");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                new Oreder(getActivity());
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                     }
        });

        /////listView---------------------------------------------

        lvDetail = (RecyclerView) rootView.findViewById(R.id.lvCustomList);
        tresult = (TextView) rootView.findViewById(R.id.tresult);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.res);
        lvDetail.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        lvDetail.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //relativeLayout.setVisibility(View.GONE);
        if (isOnline()) {
            new GetContacts().execute();
        } else {

            new DialogBox(getActivity(), new HomeFragment());

        }
        return rootView;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        getActivity().setTitle("      New Request");

    }

    public interface OnEditText_Changed {
        void onText_Changed(int position, Double charSeq);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(new HttpLink().product);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    kp = contacts.length();
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        name = c.getString("product_name");
                        pId = c.getString("product_id");

                        //Phone node is JSON Object
                        JSONArray phone = c.getJSONArray("vendor_unit_info");
                        String[] mobile = new String[phone.length()];
                        String[] price = new String[phone.length()];
                        String[] vendorId = new String[phone.length()];
                        for (int j = 0; j < phone.length(); j++) {
                            JSONObject b = phone.getJSONObject(j);
                            mobile[j] = b.getString("vendor_name");
                            price[j] = b.getString("unit_price");
                            vendorId[j] = b.getString("vendor_id");
                        }
                        HomeMode ld = new HomeMode(name, mobile, price, vendorId, pId);
                        myList.add(ld);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final Double[] entered_Number = new Double[kp];
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            for (int k = 0; k < kp; k++) {
                entered_Number[k] = 0.0;
            }

            HomeAdapter homeAdapter = new HomeAdapter(getContext(), myList, new OnEditText_Changed() {
                @Override
                public void onText_Changed(int position, Double charSeq) {
                    entered_Number[position] = charSeq;
                    sum = 0.0;
                    for (int i = 0; i < kp; i++) {
                        sum += entered_Number[i];
                        // Toast.makeText(context,strings[i],Toast.LENGTH_SHORT).show();
                    }
                    if (sum > 0.0)
                        tresult.setText(String.valueOf(sum));
                    else tresult.setText("00.00");
                }
            });

            lvDetail.setAdapter(homeAdapter);


        }

    }

    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you want to confirm the order");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        login(String.valueOf(sum));
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void loadDashboard() {
        idList2 = new ArrayList<>();
        mydb = new PersonDatabase(getActivity());
        Cursor data = mydb.getAllData();
        while (data.moveToNext()) {
            idList2.add(data.getString(0));
        }
        for (int l = 0; l < data.getCount(); l++) {
            mydb.deleteData(idList2.get(l));
        }

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void login(String totalp) {
        displayLoader();
        proList = new ArrayList<>();
        qtyList = new ArrayList<>();
        vendorList = new ArrayList<>();
        priceList = new ArrayList<>();
        mydb = new PersonDatabase(getActivity());
        indb = new InvoiceDatabase(getActivity());
        Cursor data = mydb.getAllData();
        while (data.moveToNext()) {
            proList.add(data.getString(1));
            vendorList.add(data.getString(2));
            qtyList.add(data.getString(3));
            priceList.add(data.getString(4));
        }

        session = new SessionHandler(getActivity());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
         final String TAG = MainActivity.class.getSimpleName();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, new HttpLink().productSave, mainrequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Login Response: " + mainrequest.toString());
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getString(KEY_STATUS).equals("success")) {
                                JSONObject data = response.getJSONObject("data");
                                // KEY_id=data.getString("invoice_id").toString();
                                Log.d("invoice_id", "Login Response: " + data.getString("invoice_id"));
                                loadDashboard();
                                Fragment fragment = new Invoice();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_body, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                Bundle args = new Bundle();
                                args.putString("InVo", String.valueOf(Integer.valueOf(data.getString("invoice_id"))));
                                fragment.setArguments(args);
                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(),
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
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Toast.makeText(getActivity(),
                                        obj.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                        Log.d(TAG, "Login Response2: " + mainrequest.toString());
                        Log.d(TAG, "Login Response1: " + error.toString());

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);
    }
}

