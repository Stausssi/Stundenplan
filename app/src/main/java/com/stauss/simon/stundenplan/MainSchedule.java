package com.stauss.simon.stundenplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainSchedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScheduleOverviewFragment.OnFragmentInteractionListener, ScheduleTodayFragment.OnFragmentInteractionListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEdit;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public String day;
    String[] days = {"", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    int dayNr;

    List<String> subjects;
    String subjectString;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);

        day = getDay();

        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        prefEdit = sharedPreferences.edit();

        boolean firstLaunch = sharedPreferences.getBoolean("firstLaunch", true);
        if(firstLaunch) {
            firstLaunch();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView name = navigationView.getHeaderView(0).findViewById(R.id.userName);
        name.setText(sharedPreferences.getString("userName", getString(R.string.userName)));

        subjects = getSubjects();

        Log.d("Fächer", subjects.toString());

        openFragment(new ScheduleTodayFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_schedule, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent i = new Intent();

        if (id == R.id.scheduleToday) {
            //Open today fragment
            openFragment(new ScheduleTodayFragment());
        } else if (id == R.id.scheduleOverview) {
            //Open Overview Fragment
            openFragment(new ScheduleOverviewFragment());
        } else if (id == R.id.scheduleEdit) {
            //Open ScheduleEdit Activity
            i.putExtra("day", getDayNr());
            openActivity(i, ScheduleEdit.class);
        } else if (id == R.id.homeworkAdd) {
            //Open HomworkAdd Activity
        } else if (id == R.id.homeworkOverview) {
            //Open HomeworkOverview Fragment
        } else if(id == R.id.settings) {
            //Open Settings Activity
            openActivity(i, SettingsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //No Interaction with Fragments needed
    }

    private void firstLaunch() {
        Intent i = new Intent();
        i.setClass(this, FirstLaunch.class);
        startActivity(i);

    }

    private void openActivity(Intent i, Class c) {
        i.setClass(this, c);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    private void openFragment(Fragment f) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, f);
        fragmentTransaction.addToBackStack(null);

        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public String getDay() {
        day = days[getDayNr()];
        return day;
    }

    public int getDayNr() {
        dayNr = Integer.parseInt(new SimpleDateFormat("u").format(new Date()));
        return dayNr;
    }

    public String[] getWeek() {
        return days;
    }

    public void addSubject(String subject) {
        if(!getSubjects().contains(subject)) {
            subjects.add(subject);
            saveSubjects();
        } else {
            Log.d("", "Fach " + subject + " bereits enthalten!");
        }
    }

    public List<String> getSubjects() {
        subjectString = sharedPreferences.getString("subjects", null);
        if(subjectString != null) {
            String[] subjectArray = subjectString.split(";");
            subjects = new ArrayList<>(Arrays.asList(subjectArray));
        } else {
            Log.d("", "Bisher keine Fächer in SP gespeichert -> Neue Liste");
            subjects = new ArrayList<>();
        }
        return subjects;
    }

    public void saveSubjects() {
        StringBuilder sb = new StringBuilder();
        for(String s : subjects) {
            sb.append(s);
            sb.append(";");
        }
        subjectString = sb.toString();
        prefEdit.putString("subjects", subjectString);
        prefEdit.apply();
    }

}