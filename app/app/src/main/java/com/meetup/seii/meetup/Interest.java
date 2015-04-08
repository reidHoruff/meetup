package com.meetup.seii.meetup;

/**
 * Created by reid on 7/04/15.
 */
public class Interest {
    /*
    its easier to store the id as a string
    even though it will always represent an integer.
     */
    String id, name;

    public Interest() {
    }

    public Interest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return "interest: " + this.id + ": " + this.name;
    }
}
