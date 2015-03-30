package com.meetup.seii.meetup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LoginActivity extends ServerCommunicatableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginButtonClicked(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    public void createAccountButtonClicked(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }
}
