package com.meetup.seii.meetup;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by reid on 29/03/15.
 */
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
}
