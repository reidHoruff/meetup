package com.meetup.seii.meetup;

import org.json.simple.JSONObject;

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

    public MeetupUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * create User from json blob from server
     * @param json
     */
    public MeetupUser createFromJSON(JSONObject json) {
        JSONObject data = (JSONObject)json.get("data");
        this.username = (String)data.get("username");
        this.firstName = (String)data.get("firstname");
        this.lastName = (String)data.get("lastname");
        this.sex = (String)data.get("sex");
        this.age = (String)data.get("age");
        //this.password = (String)data.get("password");
        return this;
    }

    public boolean isShill() {
        return
                this.username == null ||
                this.username.equals("") ||
                this.password == null ||
                this.password.equals("");
    }

    public String toString() {
       return "user: "
               + this.username
               + " - "
               + this.password;
    }
}