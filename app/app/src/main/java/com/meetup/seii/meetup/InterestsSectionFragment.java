package com.meetup.seii.meetup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reidhoruff on 4/12/15.
 */
public class InterestsSectionFragment extends Fragment implements ServerCommunicatable {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.interests_section_fragment, container, false);

        ListView myListView = (ListView) rootView.findViewById(R.id.int_list_view);

        /* turn intersts map into an ArrayList of interests */
        HashMap<String, Interest> interestMap = MeetupSingleton.get().getUser().getInterestMap();
        ArrayList<Interest> interests = new ArrayList<>();
        for (Map.Entry<String, Interest> i : interestMap.entrySet()) {
            interests.add(i.getValue());
        }
        
        /* Edited by Brian*/
        /*
        ArrayAdapter<Interest> adapter = new ArrayAdapter<Interest>(getActivity(), android.R.layout.simple_list_item_1, interests);
        myListView.setAdapter(adapter);
        */
        
        ArrayList<Interest> allPossibleInterests = new ArrayList<>();
        allPossibleInterests = MeetupSingleton.get().getAllInterests();

        Log.i("REST", "foo" + allPossibleInterests.size());




        ArrayAdapter<Interest> adapter = new ArrayAdapter<Interest>(getActivity(), android.R.layout.simple_list_item_1, allPossibleInterests);
        myListView.setAdapter(adapter);
        
        /*edits done*/

        /* you can modify the users interest map and this will send the updates to the server */
        ServerCommunicator comm = new ServerCommunicator(this);
        comm.updateInterestsOfMainUser();

        return rootView;
    }

    /**
     * these below must all be implemented...
     */

    public void createUserResponse(ResponseStatus status, boolean success, String message) {
    }

    public void listAllInterestsResponse(ResponseStatus status, ArrayList<Interest> interests) {
    }

    public void loginResponse(ResponseStatus status, MeetupUser user) {
    }

    public void updateInterestsResponse(ResponseStatus status, boolean success) {
        Log.i("REST", "interests updated...");
    }
}
