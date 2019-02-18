package com.xonetsapps.fabtechnologies;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 *
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }


    public void loginUser(String id,String name, String email,String mobile,String addres,String token,String companny) {
        mEditor.putString("id", id);
        mEditor.putString("name", name);
        mEditor.putString("email", email);
        mEditor.putString("mobile", mobile);
        mEditor.putString("addres", addres);
        mEditor.putString("token", token);
        mEditor.putString("company",companny);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setId(mPreferences.getString("id", KEY_EMPTY));
        user.setName(mPreferences.getString("name", KEY_EMPTY));
        user.setEmail(mPreferences.getString("email", KEY_EMPTY));
        user.setMobile(mPreferences.getString("mobile", KEY_EMPTY));
        user.setAddress(mPreferences.getString("addres", KEY_EMPTY));
        user.setToken(mPreferences.getString("token", KEY_EMPTY));
        user.setCompany(mPreferences.getString("company", KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));
        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}
