package com.example.garrison.capstone;

import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 4/2/14.
 */
public interface ServerCommunicatable {
    public void pingResponse(boolean success);
    public void createUserResponse(boolean success, String message);

    /*
    public void createRouteResponse(long route_id) {}
    public void getRouteResponse(Route route) {}
    public void getRouteListResponse(ArrayList<Route> routes) {}
    public void addCoordinateResponse(boolean success) {}
    public void setCurrentBusPositionResponse(boolean success) {}
    public void getCurrentBusPositionResponse(BusPosition position) {}
    public void addStopResponse(boolean success) {}
    public void deleteStopSubscriptionResponse(boolean success) {}
    public void addStopSubscriptionResponse(boolean success) {}
    public void getStopSubscriptionsResponse(ArrayList<StopSubscription> subs) {}
    */
}
