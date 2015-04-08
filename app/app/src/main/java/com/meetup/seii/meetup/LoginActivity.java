package com.meetup.seii.meetup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class LoginActivity extends ServerCommunicatableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginButtonClicked(View view) {
        String username = Helper.getButtonText(this, R.id.li_username);
        String password = Helper.getButtonText(this, R.id.li_pw);
        this.comm.loginUser(username, password);
    }

    public void createAccountButtonClicked(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    private void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        this.findViewById(R.id.li_error_message).setVisibility(View.VISIBLE);
    }

    private void hideError() {
        this.findViewById(R.id.li_error_message).setVisibility(View.GONE);
    }

    @Override
    public void loginResponse(ResponseStatus status, boolean success) {
        if (status != ResponseStatus.SUCCESS) {
            showToast("Server Error");
        } else if (success) {
            MeetupSingleton.get().setLoginFailed(false);
            MeetupSingleton.get().setUserIsVerified(true);
            this.hideError();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else {
            MeetupSingleton.get().setLoginFailed(true);
            MeetupSingleton.get().setUserIsVerified(false);
            this.showError();
        }
    }
}
