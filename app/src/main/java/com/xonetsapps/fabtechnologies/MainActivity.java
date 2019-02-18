package com.xonetsapps.fabtechnologies;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xonetsapps.fabtechnologies.Http.CompanyInfo;
import com.xonetsapps.fabtechnologies.activity.Help;
import  com.xonetsapps.fabtechnologies.activity.HomeFragment;
import com.xonetsapps.fabtechnologies.activity.RequestLog;
import  com.xonetsapps.fabtechnologies.activity.PasswordChange;
import  com.xonetsapps.fabtechnologies.activity.Profile;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    int a;
    public TextView textView,textView2,textView3;
    private SessionHandler session;
    public  static Toolbar toolbar;
    User user;
    private long lastPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this .getWindow().setSoftInputMode(WindowManager.LayoutParams. SOFT_INPUT_STATE_ALWAYS_HIDDEN );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.log2);
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        textView2=(TextView) headerview.findViewById(R.id.hdname);
        textView3=(TextView) headerview.findViewById(R.id.hdmail);
        textView2.setText(user.getName());
        textView3.setText(user.getEmail());

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
        //-------------------------------------
        if (a==0) {
            displayView(0);
            a=1;
        }

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "texfont1.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (item.getItemId()) {
            case R.id.nav_camera:
                fragment = new HomeFragment();
                title = "New Request";
                drawer.closeDrawers();
                break;
            case R.id.nav_gallery:
               fragment = new RequestLog();
                title = "Requst Log";
                drawer.closeDrawers();
                break;
            case R.id.nav_slideshow:
                fragment = new Profile();
                title = "Setting";
                drawer.closeDrawers();
                break;
            case R.id.nav_manage:
                fragment = new ViewListContents();
                title = "Policy";
                drawer.closeDrawers();
                break;
            case R.id.nav_pass:
                fragment = new PasswordChange();
                title = "Change password";
                drawer.closeDrawers();
                break;
            case R.id.nav_help:
                fragment = new Help();
                title = "Change password";
                drawer.closeDrawers();
                break;
            case R.id.nav_share:
               new CompanyInfo(this);
                break;
            case R.id.nav_send:
                session.logoutUser();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            //getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "New Request";
                drawer.closeDrawers();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            //getSupportActionBar().setTitle(title);
        }
    }
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
        int i = 0;
        boolean bool = true;
        if (paramKeyEvent.getKeyCode() == 4) {
            switch (paramKeyEvent.getAction())
            {
                case 0:
                    if (paramKeyEvent.getDownTime() - this.lastPressedTime >= 3000L)
                    {
                        Toast.makeText(getApplicationContext(), "Press Back To Exit", 0).show();
                        this.lastPressedTime = paramKeyEvent.getEventTime();
                    }
                    else
                    {
                        finish();
                    }
                    bool = true;
            }
        }
        return bool;
    }
}
