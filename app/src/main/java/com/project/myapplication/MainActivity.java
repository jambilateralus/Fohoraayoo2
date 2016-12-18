package com.project.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static boolean active = false;



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    static AppCompatTextView distance;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        distance = (AppCompatTextView) findViewById(R.id.text_distance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set collapsing toolbar
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        // Display group as saved in preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String group = prefs.getString("groupNumber", "1");
        int grp = Integer.parseInt(group);
        mViewPager.setCurrentItem(grp-1);

        // Show current info
        showCurrentStatus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // Start settings activity
        if (id == R.id.action_settings) {
            startActivity(new Intent(getBaseContext(),SettingsActivity.class));
            return true;
        }

        // Save gps location to database
        else if (id == R.id.gps_settings){
            if (!GPSLocation.gpsIsOn(getBaseContext())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Confirm");
                builder.setMessage("Do you want to open location settings?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        GPSLocation.openGpsSettings(getBaseContext());
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            } else{
                GPSLocation.saveCurrentLocation(getBaseContext());
            }
        }

        // Update schedule
        else if (id == R.id.action_update){
            ScheduleUpdater.updateSchedule(getBaseContext());
        }

        // Open maps
        else if(id == R.id.open_maps){
            if (!AppLocalData.isLocationSet(getBaseContext())){
                Toast.makeText(this, "Set your location first.", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(getBaseContext(), MapsActivity.class));
            }
        }

        //////////////////////////////////////////////////////////////
        // test
        else if (id==R.id.action_test){
            startService(new Intent(getBaseContext(), MyService.class));
        }


        return super.onOptionsItemSelected(item);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ////////////////////////

            int currentTab = getArguments().getInt(ARG_SECTION_NUMBER);





            // Insert items to database
            //handler.insertItem("x","o","o","i","o","x","x");
            DBHelper handler = new DBHelper(getContext());
            SQLiteDatabase db = handler.getWritableDatabase();
            String query  = "SELECT * FROM scheduleTable";
            Cursor cursor = db.rawQuery(query,null);

            ListView listView = (ListView) rootView.findViewById(R.id.list_view);
            // ListView with onItemClickListener
            /**
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), "You clicked on Item: "+position, Toast.LENGTH_SHORT).show();
                }
            });

            // ListView longClick listener
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), "Long clicked: "+position, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });*/

            // Set custom adapter to listView
            CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(getContext(), cursor, currentTab);
            listView.setAdapter(customCursorAdapter);
            /////////////////////////////////////


            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1";
                case 1:
                    return "2";
                case 2:
                    return "3";
                case 3:
                    return "4";
                case 4:
                    return "5";
                case 5:
                    return "6";
                case 6:
                    return "7";
            }
            return null;
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        // Display group as saved in preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String group = prefs.getString("groupNumber", "1");
        int grp = Integer.parseInt(group);
        mViewPager.setCurrentItem(grp-1);

        //Update current info if group is changed.
        showCurrentStatus();
    }


    /** Show current status on app header **/
    private void showCurrentStatus(){
        AppCompatTextView group, day, type;
        group = (AppCompatTextView) findViewById(R.id.text_group);
        day = (AppCompatTextView) findViewById(R.id.text_day);
        type =(AppCompatTextView) findViewById(R.id.text_type);

        // Display group as saved in preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String groupNumber = prefs.getString("groupNumber", "1");
        int grp = Integer.parseInt(groupNumber);
        group.setText("Group: "+grp);

        // Display current day
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
        day.setText(getDay(weekDay));

        // Display type
        DBHelper handler = new DBHelper(getBaseContext());
        SQLiteDatabase db = handler.getWritableDatabase();
        String query  = "SELECT item"+grp+" FROM scheduleTable;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        for (int i =0;i<=weekDay;i++){
            c.moveToNext();
        }
        //c.getString(weekDay);

        type.setText(getVehicleType(c.getString(weekDay)));
        //type.setText("Organic");
    }

    /** get day **/
    private String getDay(int position) {
        switch (position) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return null;
    }

    private String getVehicleType(String x){
        switch (x){
            case "o":
                return "Organic";
            case "i":
                return "Inorganic";
            case "x":
                return "--";
        }
        return null;
    }

    public void displayDistance(double distance){
        AppCompatTextView dis = (AppCompatTextView) findViewById(R.id.text_distance);
        dis.setText(""+distance);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active=false;
    }
}
