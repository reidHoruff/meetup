package com.meetup.seii.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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

    public void setLoginFailed(boolean f) {
        this.loginFailed = f;
    }

    public boolean getLoginFailed() {
        return this.loginFailed;
    }

    public void setUserIsVerified(boolean v) {
        this.isUserVerified = v;
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

    public void setUser(MeetupUser user) {
        this.user = user;
    }

    public void saveUser(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("com.meetup.app.username", user.username)
                .putString("com.meetup.app.firstname", user.firstName)
                .putString("com.meetup.app.lastname", user.lastName)
                .putString("com.meetup.app.password", user.password)
                .putString("com.meetup.app.sex", user.sex)
                .putString("com.meetup.app.age", user.age)
                .apply();
    }

    public void readUser(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        MeetupUser user = new MeetupUser();
        user.username = prefs.getString("com.meetup.app.username", null);
        user.firstName = prefs.getString("com.meetup.app.firstname", null);
        user.lastName = prefs.getString("com.meetup.app.lastname", null);
        user.password = prefs.getString("com.meetup.app.password", null);
        user.sex = prefs.getString("com.meetup.app.sex", null);
        user.age = prefs.getString("com.meetup.app.age", null);

        this.user = user;
    }

    public boolean hasLoggedInBefore(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        return prefs.getBoolean("com.meetup.app.has_logged_in", false);
    }

    public void setHasLoggedInBefore(Activity activity, boolean hasLoggedIn) {
        SharedPreferences prefs = activity.getSharedPreferences("com.meetup.app", Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean("com.meetup.app.has_logged_in", hasLoggedIn)
                .apply();
    }

    public void logout(Activity activity) {
        //all null values
        this.user = new MeetupUser();
        this.saveUser(activity);
        this.user = null;
    }
}
