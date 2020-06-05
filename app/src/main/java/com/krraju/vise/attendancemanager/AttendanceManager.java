package com.krraju.vise.attendancemanager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.timetable.TimeTable;
import com.krraju.vise.yearshedule.YearSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceManager extends AppCompatActivity {

    private ArrayList<Attendance> attendanceList;
    private LinearLayout linearLayout;
    private Map<String, Integer> subjectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_manager);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        subjectIndex = new HashMap<>();

        linearLayout = findViewById(R.id.attendanceList);

        DrawerLayout drawerLayout = findViewById(R.id.navigationDrawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        attendanceList = AttendanceStorage.read(this);
        if (attendanceList == null) {
            attendanceList = new ArrayList<>();
            initializeList(attendanceList);
        }
        initializeMap();
        initializeAttendance();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AttendanceManager.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AttendanceManager.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AttendanceManager.this, TimeTable.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AttendanceManager.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(AttendanceManager.this, YearSchedule.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initializeMap() {
        subjectIndex.put("Computer Networking", 0);
        subjectIndex.put("Artificial Intelligence", 1);
        subjectIndex.put("Database Management System", 2);
        subjectIndex.put("Computer Graphics", 3);

        subjectIndex.put("Computer Graphics Lab", 4);
        subjectIndex.put("Database Management System Lab", 5);

        subjectIndex.put("Software Architecture And Testing", 6);
        subjectIndex.put("Probability And Stochastic Process", 7);
        subjectIndex.put("Operation Research", 8);

        subjectIndex.put("Advance Java And J2EE", 9);
        subjectIndex.put("Python Programming", 10);
        subjectIndex.put("Computer Organisation And Architecture", 11);
    }

    private void initializeAttendance() {
        for (int i = 1; i < linearLayout.getChildCount(); i++) {
            if (i == 5 || i == 8 || i == 12) {
                continue;
            }
            CardView cardView = (CardView) linearLayout.getChildAt(i);
            RelativeLayout imageBackground = (RelativeLayout) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(1)).getChildAt(0);
            TextView subjectName = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(0);
            TextView attendanceText = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(1);
            TextView attendancePer = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(2);

            Attendance attendance = this.attendanceList.get(subjectIndex.get(subjectName.getText()));
            attendanceText.setText(String.format("%s%d%s", "The Attendance is ", (int) attendance.getPercentage(), " %"));
            attendancePer.setText(String.format("%d/%d", attendance.getNoOfClassAttended(), attendance.getNoOfClass()));
            if (attendance.getPercentage() >= 75.00 || attendance.getNoOfClass() == 0) {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_green);
            } else {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_red);
            }
        }
    }

    private void initializeList(ArrayList<Attendance> attendance) {
        attendance.add(new Attendance("Computer Networking"));
        attendance.add(new Attendance("Artificial Intelligence"));
        attendance.add(new Attendance("Database Management System"));
        attendance.add(new Attendance("Computer Graphics"));

        attendance.add(new Attendance("Computer Graphics Lab"));
        attendance.add(new Attendance("Database Management System Lab"));

        attendance.add(new Attendance("Software Architecture And Testing"));
        attendance.add(new Attendance("Probability And Stochastic Process"));
        attendance.add(new Attendance("Operation Research"));

        attendance.add(new Attendance("Advance Java And J2EE"));
        attendance.add(new Attendance("Python Programming"));
        attendance.add(new Attendance("Computer Organisation And Architecture"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(AttendanceManager.this, HomeScreen.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AttendanceManager.this, HomeScreen.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AttendanceStorage.write(this, attendanceList);
    }

    public void addAttendance(View view) {
        CardView cardView = (CardView) view;
        RelativeLayout imageBackground = (RelativeLayout) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(1)).getChildAt(0);
        TextView subjectName = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(0);
        TextView attendanceText = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(1);
        TextView attendancePer = (TextView) ((RelativeLayout) ((LinearLayout) cardView.getChildAt(0)).getChildAt(0)).getChildAt(2);
        Toast.makeText(this, subjectName.getText(), Toast.LENGTH_SHORT).show();

        Attendance attendance = this.attendanceList.get(subjectIndex.get(subjectName.getText()));
        updateAttendance(attendance, attendanceText, attendancePer, imageBackground);
    }

    private void updateAttendance(Attendance attendance, TextView attendanceText, TextView attendancePer, RelativeLayout imageBackground) {
        Dialog dialog = new Dialog(this);
        setUpDialog(dialog, attendance);
        Button presentButton = dialog.findViewById(R.id.present);
        Button absentButton = dialog.findViewById(R.id.absent);
        TextView closeButton = dialog.findViewById(R.id.closeDialog);
        dialog.show();
        presentButton.setOnClickListener(v -> {
            attendance.add(true);
            dialog.dismiss();
            attendanceText.setText(String.format("%s%d%s", "The Attendance is ", (int) attendance.getPercentage(), " %"));
            attendancePer.setText(String.format("%d/%d", attendance.getNoOfClassAttended(), attendance.getNoOfClass()));

            if (attendance.getPercentage() >= 75.00) {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_green);
            } else {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_red);
            }

        });
        absentButton.setOnClickListener(v -> {
            attendance.add(false);
            dialog.dismiss();
            attendanceText.setText(String.format("%s%d%s", "The Attendance is ", (int) attendance.getPercentage(), " %"));
            attendancePer.setText(String.format("%d/%d", attendance.getNoOfClassAttended(), attendance.getNoOfClass()));
            if (attendance.getPercentage() >= 75.00) {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_green);
            } else {
                imageBackground.setBackgroundResource(R.drawable.image_back_ground_red);
            }
        });
        closeButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void setUpDialog(Dialog dialog, Attendance attendance) {
        dialog.setContentView(R.layout.attendance_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        TextView subjectName = dialog.findViewById(R.id.subject_name);
        TextView attendanceNumber = dialog.findViewById(R.id.attendance_Number);
        subjectName.setText(attendance.getSubjectName());
        attendanceNumber.setText(String.format("%s%d%s", "Your ", attendance.getNoOfClass() + 1, " Attendance is:"));
    }

}
