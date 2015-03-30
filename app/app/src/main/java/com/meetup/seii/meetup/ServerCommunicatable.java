package com.meetup.seii.meetup;

/**
 * Created by reid on 29/03/15.
 */
public interface ServerCommunicatable {
    public void createUserResponse(ResponseStatus status, boolean success, String message);
}