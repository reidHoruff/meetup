package com.meetup.seii.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by reid on 29/03/15.
 */
public class MeetupSingleton {
    private static MeetupSingleton instance = null;

    private MeetupUser user;

    private boolean isUserVerified, loginFailed;

    private MeetupSingleton() {
        this.user = null;
        this.isUserVerified = false;
        this.loginFailed = false;
    }

    public MeetupSingleton setLoginFailed(boolean f) {
        this.loginFailed = f;
        return this;
    }

    public boolean getLoginFailed() {
        return this.loginFailed;
    }

    public MeetupSingleton setUserIsVerified(boolean v) {
        this.isUserVerified = v;
        return this;
    }

    public boolean isUserVerified() {
        return this.isUserVerified;
    }

    public static MeetupSingleton get() {
        if (MeetupSingleton.instance == null) {
            MeetupSingleton.instance = new MeetupSingleton();
        }
        return MeetupSingleton.instance;
    }

    public MeetupUser getUser() {
        return this.user;
    }

    public MeetupSingleton setUser(MeetupUser user) {
        this.user = user;
        return this;
    }

    public MeetupSingleton saveUser(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("com.meetup.app.username", user.username)
                .putString("com.meetup.app.firstname", user.firstName)
                .putString("com.meetup.app.lastname", user.lastName)
                .putString("com.meetup.app.password", user.password)
                .putString("com.meetup.app.sex", user.sex)
                .putString("com.meetup.app.age", user.age)
                .apply();
        return this;
    }

    public MeetupSingleton readUser(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        MeetupUser user = new MeetupUser();
        user.username = prefs.getString("com.meetup.app.username", null);
        user.firstName = prefs.getString("com.meetup.app.firstname", null);
        user.lastName = prefs.getString("com.meetup.app.lastname", null);
        user.password = prefs.getString("com.meetup.app.password", null);
        user.sex = prefs.getString("com.meetup.app.sex", null);
        user.age = prefs.getString("com.meetup.app.age", null);

        this.user = user;
        return this;
    }

    public boolean hasLoggedInBefore(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        boolean has = prefs.getBoolean("com.meetup.app.has_logged_in", false);
        Log.i("SPL", "hasLoggedInBefore(): " + has);
        return has;
    }

    public MeetupSingleton setHasLoggedInBefore(Activity activity, boolean hasLoggedIn) {
        Log.i("SPL", "setHasLoggedInBefore(" + hasLoggedIn + ")");
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean("com.meetup.app.has_logged_in", hasLoggedIn)
                .apply();
        return this;
    }

    public MeetupSingleton logout(Activity activity) {
        //all null values
        this.user = new MeetupUser();
        this.saveUser(activity);
        this.user = null;
        this.setUserIsVerified(false);
        this.setLoginFailed(false);
        return this;
    }
}
