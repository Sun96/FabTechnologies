package com.xonetsapps.fabtechnologies.Http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.xonetsapps.fabtechnologies.RequestModel.HttpHandler;
import com.xonetsapps.fabtechnologies.activity.Help;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class CompanyInfo {
    Activity context;
    String appslink;
    public  CompanyInfo(Activity context){
        this.context=context;
        new GetContacts().execute();
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(new HttpLink().comInfo);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONObject c = jsonObj.getJSONObject("data");

                    appslink =c.getString("apps_link");
                    // looping through All Contacts

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
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
            // Dismiss the progress dialog
            Intent localIntent = new Intent("android.intent.action.SEND");
            localIntent.setType("text/plain");
            localIntent.putExtra("android.intent.extra.TEXT",appslink);
            context.startActivity(Intent.createChooser(localIntent, "Share This App Using"));
            /**
             * Updating parsed JSON data into ListView
             * */
        }

    }
}
