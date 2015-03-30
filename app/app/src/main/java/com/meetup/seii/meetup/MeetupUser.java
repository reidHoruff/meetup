package com.meetup.seii.meetup;

/**
 * Created by reid on 29/03/15.
 */
public class MeetupUser {
    public String firstName, lastName, sex, username, password, age;

    public MeetupUser() {

    }

    public MeetupUser(String username, String fname, String lname, String sex, String age, String password) {
        this.firstName = fname;
        this.lastName = lname;
        this.sex = sex;
        this.username = username;
        this.age = age;
        this.password = password;
    }
}
