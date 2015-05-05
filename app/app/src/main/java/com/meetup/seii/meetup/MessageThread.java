package com.meetup.seii.meetup;

import java.util.ArrayList;
import java.util.Collections;

public class MessageThread {
    private ArrayList<Message> thread;

    public MessageThread() {
        this.thread = new ArrayList<>();
    }

    public void addMessage(Message m) {
        this.thread.add(m);
    }

    public void copyThread(MessageThread other) {
        this.thread.clear();
        this.thread.addAll(other.thread);
    }

    public ArrayList<Message> getThreadList() {
        Collections.sort(this.thread);
        return this.thread;
    }
}
