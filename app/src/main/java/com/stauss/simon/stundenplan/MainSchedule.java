package com.stauss.simon.stundenplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.Calendar;
import java.util.Date;

public class MainSchedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEdit;

    String day;
    String[] days = {"", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_schedule);

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

        TableLayout table = findViewById(R.id.table);

        TextView text = findViewById(R.id.textView);
        text.setVisibility(View.VISIBLE);
        String date = new SimpleDateFormat("EEEE, dd. MMMM yyyy").format(new Date());
        text.setText("Heute, " + date + ", hast du folgende Fächer:" );

        int daynr = Integer.parseInt(new SimpleDateFormat("u").format(new Date()));
        day = days[daynr];

        TextView name = findViewById(R.id.userName);
        //name.setText(sharedPreferences.getString("name", getString(R.string.nav_header_name)));

        buildDailySchedule(table);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = new Intent();

        if (id == R.id.scheduleToday) {
            //This very activity -> Do nothing
        } else if (id == R.id.scheduleOverview) {
            //Open Overview activity
        } else if (id == R.id.scheduleEdit) {
            i.putExtra("day", 1);
            openActivity(i, ScheduleEdit.class);
        } else if (id == R.id.homeworkAdd) {

        } else if (id == R.id.homeworkOverview) {

        } else if(id == R.id.settings) {

        } else {
            return false;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void buildDailySchedule(TableLayout tableLayout) {
        for (int i = 1; i <= 11; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 5, 0, 5);

            TextView h = new TextView(this);
            h.setText("" + i);
            h.setGravity(Gravity.CENTER);

            TextView sub = new TextView(this);
            sub.setText(sharedPreferences.getString(day + i + "s", "-"));
            sub.setGravity(Gravity.CENTER);

            TextView room = new TextView(this);
            room.setText(sharedPreferences.getString(day + i + "r", "-"));
            room.setPadding(0,0,50, 0);
            room.setGravity(Gravity.CENTER);

            row.addView(h, new TableRow.LayoutParams(0));
            row.addView(sub, new TableRow.LayoutParams(1));
            row.addView(room, new TableRow.LayoutParams(2));

            tableLayout.addView(row);
        }
    }

    private void firstLaunch() {
        Intent i = new Intent();
        i.setClass(this, FirstLaunch.class);
        startActivity(i);

    }

    private void openActivity(Intent i, Class c) {
        i.setClass(this, c);
        startActivity(i);
    }
}