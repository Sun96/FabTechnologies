package com.xonetsapps.fabtechnologies.Http;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.activity.UpdateActivity;

public class DialogBox {
    Activity activity;
    Dialog MyDialog;
    TextView close;
    Fragment fragment;
    public DialogBox(Activity activity,Fragment fragment){
        this.activity=activity;
        this.fragment=fragment;
        MyCustomAlertDialog();
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(activity);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.customdialog);
        MyDialog.setTitle("My Custom Dialog");

        close = (TextView)MyDialog.findViewById(R.id.close);

        close.setEnabled(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        MyDialog.show();
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
