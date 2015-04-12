package com.meetup.seii.meetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CreateAccountActivity extends ServerCommunicatableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.comm.fetchAllIntersts();
    }

    public void loginButtonClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void submitButtonClicked(View view) {
        String username = Helper.getButtonText(this, R.id.su_username);
        String firstname = Helper.getButtonText(this, R.id.su_fname);
        String age = Helper.getButtonText(this, R.id.su_age);
        String lastname = Helper.getButtonText(this, R.id.su_lname);
        String password = Helper.getButtonText(this, R.id.su_pw);
        String password2 = Helper.getButtonText(this, R.id.su_pw2);
        String sex = Helper.getRadioChecked(this, R.id.su_sex_male) ? "m" : "f";

        if (!password.equals(password2)) {
            showToast("Passwords do not match.");
            return;
        }

        MeetupUser user = new MeetupUser(
                username,
                firstname,
                lastname,
                sex,
                age,
                password
        );

        /*
        user is a candidate.
        will save user is response is successful
         */
        MeetupSingleton.get().setUser(user);

        this.comm.createUser(user);
    }

    @Override
    public void createUserResponse(ResponseStatus status, boolean success, String message) {
        if (status == ResponseStatus.SUCCESS) {
            if (success) {
                MeetupSingleton.get().saveUser(this);
                MeetupSingleton.get().setLoginFailed(false);
                MeetupSingleton.get().setUserIsVerified(true);
                MeetupSingleton.get().setHasLoggedInBefore(this, true);
                startActivity(new Intent(this, WelcomeActivity.class));
            } else {
                showToast(message);
            }
        } else {
            showToast("Network Error.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
