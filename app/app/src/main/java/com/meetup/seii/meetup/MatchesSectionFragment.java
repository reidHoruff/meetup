package com.meetup.seii.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

/**
 * Created by reidhoruff on 4/12/15.
 */
public class MatchesSectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matches_section_fragment, container, false);
        ListView myListView = (ListView) rootView.findViewById(R.id.match_list_view);

        ArrayList<MeetupUser> matches = MeetupSingleton.get().getUser().getMatches();
        MatchesCustomAdapter adapter = new MatchesCustomAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                matches,
                getActivity()
        );
        myListView.setAdapter(adapter);

        return rootView;
    }
}

class MatchesCustomAdapter extends ArrayAdapter<MeetupUser> {

    private ArrayList<MeetupUser> matches;
    private Context context;
    private Activity activity;

    public MatchesCustomAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<MeetupUser> matches,
            Activity activity
    ) {
        super(context, textViewResourceId, matches);
        this.context = context;
        this.matches = matches;
        this.activity = activity;
    }

    private class ViewHolder {
        TextView username;
        MeetupUser user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.matches_list_frag, null);

            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.matches_li_username);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ViewHolder vh = (ViewHolder) v.getTag();
                MeetupUser user = vh.user;

                Log.i("REST", "user clicked: " + user.toString());
                Intent intent = new Intent(activity, ProfileChatActivity.class);
                intent.putExtra("username", user.username);
                activity.startActivity(intent);
            }
        });

        MeetupUser user = MeetupSingleton.get().getUser().getMatches().get(position);
        holder.username.setText(user.username);
        holder.user = user;

        return convertView;
    }
}

