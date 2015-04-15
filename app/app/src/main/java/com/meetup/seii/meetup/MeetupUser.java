package com.meetup.seii.meetup;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by reid on 29/03/15.
 */
public class MeetupUser {
    public String firstName, lastName, sex, username, password, age;
    private HashMap<String, Interest> interestMap;
    private ArrayList<MeetupUser> matches;

    public MeetupUser() {
        this.matches = new ArrayList<>();
        this.interestMap = new HashMap<>();
    }

    public MeetupUser(String username, String fname, String lname, String sex, String age, String password) {
        this();
        this.firstName = fname;
        this.lastName = lname;
        this.sex = sex;
        this.username = username;
        this.age = age;
        this.password = password;
    }

    public MeetupUser(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    /**
     * create User from json blob from server
     * @param json
     */
    public MeetupUser(JSONObject data) {
        this();
        this.setBasicInfo(data);

        JSONObject interests = (JSONObject)data.get("interests");
        Iterator iterator = interests.keySet().iterator();

        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            String value = (String)interests.get(key);
            this.interestMap.put(key, new Interest(key, value));
            Log.i("REST", key + " : " + value);
        }

        JSONArray matches = (JSONArray)data.get("matches");

        for (int i = 0; i < matches.size(); i++) {
            JSONObject matchData = (JSONObject)matches.get(i);
            String score = (String)matchData.get("score");
            JSONObject userData = (JSONObject)matchData.get("user");
            MeetupUser user = new MeetupUser().setBasicInfo(userData);
            this.matches.add(user);
            Log.i("REST", "match: " + user.username);
        }
    }

    private MeetupUser setBasicInfo(JSONObject data) {
        this.username = (String)data.get("username");
        this.firstName = (String)data.get("firstname");
        this.lastName = (String)data.get("lastname");
        this.sex = (String)data.get("sex");
        this.age = (String)data.get("age");
        return this;
    }

    public MeetupUser pullFrom(MeetupUser other) {
        if (Helper.uslessString(this.firstName)) this.firstName = other.firstName;
        if (Helper.uslessString(this.lastName)) this.lastName = other.lastName;
        if (Helper.uslessString(this.username)) this.username = other.username;
        if (Helper.uslessString(this.password)) this.password = other.password;
        if (Helper.uslessString(this.sex)) this.sex = other.sex;
        if (Helper.uslessString(this.age)) this.age = other.age;
        return this;
    }

    public HashMap<String, Interest> getInterestMap() {
        return this.interestMap;
    }

    /*
    is this object capable of authing itself
     */
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
