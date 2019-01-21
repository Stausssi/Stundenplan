package com.stauss.simon.stundenplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleEdit extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEdit;

    int day;
    String[] days = {"", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        prefEdit = sharedPreferences.edit();

        day = getIntent().getIntExtra("day", 1);

        TextView header = findViewById(R.id.header);
        header.setText(getString(R.string.schedule_edit_header).replace("%DAY%", days[day]));

        buildTable((TableLayout)findViewById(R.id.table));

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                if(prefEdit.commit()) {
                    Intent i = new Intent();
                    if(day <= 4) {
                        i.putExtra("day", day + 1);
                        openActivity(i, ScheduleEdit.class);
                    } else {
                        openActivity(i, MainSchedule.class);
                    }

                }
            }
        });

        findViewById(R.id.finishedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                if(prefEdit.commit()) {
                    openActivity(new Intent(), MainSchedule.class);
                }
            }
        });
    }

    private void buildTable(TableLayout tableLayout) {
        for (int i = 1; i <= 11; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 5, 0, 5);

            TextView h = new TextView(this);
            h.setText("" + i);
            h.setGravity(Gravity.CENTER);

            EditText sub = new EditText(this);
            sub.setId(20+i);
            sub.setHint("Fach" + i);
            sub.setText(sharedPreferences.getString(days[day] + i + "s", ""));
            sub.setGravity(Gravity.CENTER);
            sub.setSingleLine(true);

            EditText room = new EditText(this);
            room.setId(40+i);
            room.setHint("Raum" + i);
            room.setText(sharedPreferences.getString(days[day] + i + "r", ""));
            room.setGravity(Gravity.CENTER);
            room.setSingleLine(true);

            row.addView(h, new TableRow.LayoutParams(0));
            row.addView(sub, new TableRow.LayoutParams(1));
            row.addView(room, new TableRow.LayoutParams(2));
            
            tableLayout.addView(row);
        }
    }

    private void getInput() {
        for(int i = 1; i <= 11; i++) {
            EditText subject = findViewById(20+i);
            EditText room = findViewById(40+i);
            setLesson(days[day], i, subject.getText().toString(), room.getText().toString());
        }
    }

    private void setLesson(String day, int hour, String subject, String room) {
        if(!subject.equalsIgnoreCase("")) {
            prefEdit.putString(day + hour + "s", subject);
        } else {
            prefEdit.putString(day + hour + "s", "-");
        }

        if(!room.equalsIgnoreCase("")) {
            prefEdit.putString(day + hour + "r", room);
        } else {
            prefEdit.putString(day + hour + "r", "-");
        }
    }

    private void openActivity(Intent i, Class c) {
        i.setClass(getApplicationContext(), c);
        startActivity(i);
    }
}