package com.meetup.seii.meetup;

import java.util.ArrayList;

/**
 * Created by reid on 29/03/15.
 */
public interface ServerCommunicatable {
    public void createUserResponse(ResponseStatus status, boolean success, String message);
    public void listAllInterestsResponse(ResponseStatus status, ArrayList<Interest> interests);
    public void loginResponse(ResponseStatus status, MeetupUser user);
    public void updateInterestsResponse(ResponseStatus status, boolean success);
    public void messageSendResponse(ResponseStatus status, boolean success);
}