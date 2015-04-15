package com.meetup.seii.meetup;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HomeFragmentActivity extends FragmentActivity implements ServerCommunicatable {

    private ServerCommunicator comm;

    public static final int SETTINGS_PAGE = 0;
    public static final int INTERESTS_PAGE = 1;
    public static final int MATCHES_PAGE = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fragment);

        this.comm = new ServerCommunicator(this);
        //this.comm.updateInterestsOfUser(MeetupSingleton.get().getUser());

        HomeSectionsPagerAdapter pageAdapter = new HomeSectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pageAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class HomeSectionsPagerAdapter extends FragmentPagerAdapter {

        public HomeSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case SETTINGS_PAGE:
                    return new SettingsSectionFragment();
                case INTERESTS_PAGE:
                    return new InterestsSectionFragment();
                case MATCHES_PAGE:
                    return new MatchesSectionFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title: " + position;
        }
    }

    public void logoutButtonClicked(View view) {
        MeetupSingleton.get().logout(this);
        startActivity(new Intent(HomeFragmentActivity.this, LoginActivity.class));
    }

    public void createUserResponse(ResponseStatus status, boolean success, String message) {
        //...
    }

    public void listAllInterestsResponse(ResponseStatus status, ArrayList<Interest> interests) {
        //...
    }

    public void loginResponse(ResponseStatus status, MeetupUser user) {
        //...
    }

    public void updateInterestsResponse(ResponseStatus status, boolean success) {
        //...
    }
}

