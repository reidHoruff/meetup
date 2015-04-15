package com.meetup.seii.meetup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
public class MatchesSectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matches_section_fragment, container, false);

        ListView myListView = (ListView) rootView.findViewById(R.id.match_list_view);

        ArrayList<MeetupUser> interests = MeetupSingleton.get().getUser().getMatches();
        ArrayAdapter<MeetupUser> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, interests);
        myListView.setAdapter(adapter);

        return rootView;
    }
}

