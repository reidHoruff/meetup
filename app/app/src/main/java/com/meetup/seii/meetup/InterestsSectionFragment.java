package com.meetup.seii.meetup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class InterestsSectionFragment extends Fragment implements ServerCommunicatable {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* you can modify the users interest map and this will send the updates to the server */
        ServerCommunicator comm = new ServerCommunicator(this);

        /*
        grab relevant views
         */
        View rootView = inflater.inflate(R.layout.interests_section_fragment, container, false);
        ListView currentInterestsListView = (ListView) rootView.findViewById(R.id.int_list_view);

        /*
        get all interests
         */
        ArrayList<Interest> allInterests = MeetupSingleton.get().getAllInterestsSorted();

        /*
        get current interests
         */
        HashMap<String, Interest> currentInterestsMap = MeetupSingleton.get().getUser().getInterestMap();


        InterestsCustomAdapter dataAdapter = new InterestsCustomAdapter(getActivity(), R.layout.interests_list_frag, allInterests, currentInterestsMap, comm);
        currentInterestsListView.setAdapter(dataAdapter);

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
        MeetupSingleton.get().setMatchesDirty();
        Log.i("REST", "interests updated...");
    }

    public void messageSendResponse(ResponseStatus status, boolean success) {
    }

    public void getThreadResponse(ResponseStatus status, MessageThread thread) {
    }
}

class InterestsCustomAdapter extends ArrayAdapter<Interest> {

    private ArrayList<Interest> allInterestsList;
    private HashMap<String, Interest> currentInterestsMap;
    private ServerCommunicator comm;
    private Context context;

    public InterestsCustomAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<Interest> interestList,
            HashMap<String, Interest> currentInterestsMap,
            ServerCommunicator comm
    ) {
        super(context, textViewResourceId, interestList);
        this.context = context;
        this.allInterestsList = interestList;
        this.currentInterestsMap = currentInterestsMap;
        this.comm = comm;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.interests_list_frag, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox)v;
                    Interest interest = (Interest)cb.getTag();
                    if (cb.isChecked()) {
                        MeetupSingleton.get().getUser().getInterestMap().put(interest.getID(), interest);
                    } else {
                        MeetupSingleton.get().getUser().getInterestMap().remove(interest.getID());
                    }
                    comm.updateInterestsOfMainUser();
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Interest interest = allInterestsList.get(position);
        holder.code.setText(" (" +  interest.getID() + ")");
        holder.name.setText(interest.getName());
        holder.name.setChecked(this.currentInterestsMap.containsKey(interest.getID()));
        holder.name.setTag(interest);

        return convertView;
    }
}

