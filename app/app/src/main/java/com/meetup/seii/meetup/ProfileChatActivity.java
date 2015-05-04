package com.meetup.seii.meetup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileChatActivity extends ServerCommunicatableActivity {
    private MeetupUser currentMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chat);
        TextView text = (TextView) findViewById(R.id.profile_username);
        String username = getIntent().getExtras().getString("username");
        MeetupUser user = MeetupSingleton.get().getUser().getMatchByUsername(username);
        this.currentMatch = user;
        text.setText(user.getFullName());
    }

    public void onSendButtonClicked(View view) {
        if (this.currentMatch != null) {
            EditText editText = (EditText)findViewById(R.id.message_input);
            String body = editText.getText().toString();
            this.getServerComm().sendMessage(this.currentMatch.username, body);
            editText.setText("");
        }
    }

    @Override
    public void messageSendResponse(ResponseStatus status, boolean success) {
        if (status == ResponseStatus.SUCCESS || success) {
            Log.i("REST", "message sent succeeded.");
        } else {
            Log.i("REST", "message send failed.");
        }
    }
}
