package com.meetup.seii.meetup;

public class Message implements Comparable {
    private boolean fromMainUser;
    private String body;
    private int id;

    public Message(boolean fromMainUser,String body, int id) {
        this.fromMainUser = fromMainUser;
        this.body = body;
        this.id = id;
    }

    public Message() {
    }

    public boolean fromMainUser() {
        return this.fromMainUser;
    }

    public String getBody() {
        return this.body;
    }

    public int getID() {
        return this.id;
    }

    public int compareTo(Object other) {
        Message o = (Message) other;
        return o.id - this.id;
    }
}
