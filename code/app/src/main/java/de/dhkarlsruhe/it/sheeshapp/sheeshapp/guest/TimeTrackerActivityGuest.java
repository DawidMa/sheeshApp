package de.dhkarlsruhe.it.sheeshapp.sheeshapp.guest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.TimeTrackerFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.TrackerFriendsFragment;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;

public class TimeTrackerActivityGuest extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    //private Toolbar toolbar;
    private TimeTrackerFragmentGuest timeTrackerFragment;
    //protected PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracker);

        // toolbar = (Toolbar) findViewById(R.id.tbTracker);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        //mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        //mWakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        timeTrackerFragment = new TimeTrackerFragmentGuest();

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_time_tracker, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.tiTvTopTitle);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public void pressedStart(View view) {
        timeTrackerFragment.fragmentPressedStart();
    }
    public void pressedPause(View view) {
        timeTrackerFragment.fragmentPressedPause();
    }
    public void pressedEnd(View view) {
        timeTrackerFragment.fragmentPressedEnd();
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
          //  return PlaceholderFragment.newInstance(position + 1);

            switch (position) {
                case 0:
                    return timeTrackerFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case 1:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Heute beginnt " + TimeTrackerFragmentGuest.firstFriend + "!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeTrackerFragment.playPositive();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Willst du wirklich gehen?");
                builder2.setCancelable(true);
                builder2.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeTrackerFragment.endPositiv();
                    }
                });
                builder2.setNegativeButton("Nein!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // timeTrackerFragment.endNegative();
                    }
                });

                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onDestroy() {
       // this.mWakeLock.release();
        super.onDestroy();
    }
    @Override
    public void onRestart() {
        timeTrackerFragment.myResume();
        super.onRestart();
    }
    @Override
    public void onStop() {
        timeTrackerFragment.myStop();
        super.onStop();
    }
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if(KeyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(KeyCode,event);
    }

    public PendingIntent myPendingIntent() {
        Intent intent = new Intent(this, TimeTrackerActivityGuest.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
