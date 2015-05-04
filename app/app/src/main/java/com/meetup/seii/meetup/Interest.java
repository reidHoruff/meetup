package com.meetup.seii.meetup;

/**
 * Created by reid on 7/04/15.
 */
public class Interest implements Comparable {
    /*
    its easier to store the id as a string
    even though it will always represent an integer.
     */
    private String id, name;

    public Interest() {
    }

    public Interest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int compareTo(Object other) {
        Interest o = (Interest)other;
        return this.name.compareTo(o.name);
    }

    public String toString() {
        return "interest: " + this.id + ": " + this.name;
    }
}
