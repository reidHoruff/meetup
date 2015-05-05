package com.meetup.seii.meetup;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

public class ServerCommunicatableActivity extends Activity implements ServerCommunicatable {

    protected ServerCommunicator comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.comm = new ServerCommunicator(this);
    }

    protected ServerCommunicator getServerComm() {
        return this.comm;
    }

    /**
     * to override
     */
    public void createUserResponse(ResponseStatus status, boolean success, String message) {
    }

    public void listAllInterestsResponse(ResponseStatus status, ArrayList<Interest> interests) {
    }

    public void loginResponse(ResponseStatus status, MeetupUser user) {
    }

    public void updateInterestsResponse(ResponseStatus status, boolean success) {
    }

    public void messageSendResponse(ResponseStatus status, boolean success) {
    }

    public void getThreadResponse(ResponseStatus status, MessageThread thread) {
    }
}
