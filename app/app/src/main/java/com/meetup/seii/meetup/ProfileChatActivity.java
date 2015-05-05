package com.meetup.seii.meetup;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileChatActivity extends ServerCommunicatableActivity {
    private MeetupUser currentMatch;
    private MessageThread thread;
    private ChatCustomAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chat);

        TextView text = (TextView) findViewById(R.id.profile_username);
        String username = getIntent().getExtras().getString("username");
        MeetupUser user = MeetupSingleton.get().getUser().getMatchByUsername(username);
        this.currentMatch = user;
        text.setText(user.getFullName());

        /*
        setup message thread
         */

        this.thread = new MessageThread();

        ListView myListView = (ListView) findViewById(R.id.chat_list_view);
        this.chatAdapter = new ChatCustomAdapter(
                this,
                android.R.layout.simple_list_item_1,
                this.thread.getThreadList()
        );
        myListView.setAdapter(this.chatAdapter);

        Log.i("REST", "fetching thread...");
        this.getServerComm().fetchThread(currentMatch.username);
    }

    public void onSendButtonClicked(View view) {
        if (this.currentMatch != null) {
            EditText editText = (EditText)findViewById(R.id.message_input);
            String body = editText.getText().toString();
            this.getServerComm().sendMessage(this.currentMatch.username, body);
            editText.setText("");
            this.hideKeyboard();
        }
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    @Override
    public void getThreadResponse(ResponseStatus status, MessageThread thread) {
        if (status == ResponseStatus.SUCCESS) {
            this.thread.copyThread(thread);
            this.chatAdapter.notifyDataSetChanged();
        } else {
        }
    }
}

class ChatCustomAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> thread;
    private Context context;

    public ChatCustomAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<Message> thread
    ) {
        super(context, textViewResourceId, thread);
        this.context = context;
        this.thread = thread;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Message message = this.thread.get(position);

        if (message.fromMainUser()) {
            convertView = vi.inflate(R.layout.message_me_frag, null);
            TextView body = (TextView) convertView.findViewById(R.id.message_me);
            body.setText(message.getBody());
        } else {
            convertView = vi.inflate(R.layout.message_other_frag, null);
            TextView body = (TextView) convertView.findViewById(R.id.message_other);
            body.setText(message.getBody());
        }

        return convertView;
    }
}
