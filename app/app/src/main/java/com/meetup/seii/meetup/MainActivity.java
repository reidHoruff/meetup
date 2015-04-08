package com.meetup.seii.meetup;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * all this activity does is display the splash screen
 * and determine what screen to show next
 * [login, register, or home activity]
 */

public class MainActivity extends ServerCommunicatableActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MeetupSingleton.get().readUser(this);
        MeetupUser user = MeetupSingleton.get().getUser();

        /**
         * never logged in before... goto create account
         */
        if (!MeetupSingleton.get().hasLoggedInBefore(this)) {
            Log.i("SPL", "going to create account activity..");
            this.delayToCreateAccount();
        }
        /**
         * logged in before but no login info
         * saved... goto login page
         */
        else if (user.isShill()) {
            Log.i("SPL", "going to login activity..");
            this.delayToLogin();
        }
        /**
         * user info is saved.
         * try to log in...
         */
        else {
            Log.i("SPL", "attempting to login..");
            //this.comm.loginUser(user.username, user.password);
            this.delayToLogin();
        }
    }

    /**
     * yea there is a lot of copy pasta
     * below but im goin fast...
     */
    public void delayToCreateAccount() {
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }

    public void delayToLogin() {
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }

    public void delayToHome() {
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }

    @Override
    public void loginResponse(ResponseStatus status, boolean success) {
        if (success) {
            // to home page...
            Log.i("SPL", "login success..");
            MeetupSingleton.get().setLoginFailed(false);
            MeetupSingleton.get().setUserIsVerified(true);
            Log.i("SPL", "going to home activity..");
            delayToHome();
        } else {
            // to login page with error message...
            Log.i("SPL", "login failed..");
            MeetupSingleton.get().setLoginFailed(true);
            MeetupSingleton.get().setUserIsVerified(false);
            Log.i("SPL", "going to login page with error..");
        }
    }

}
