package com.krraju.vise.yearshedule;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.timetable.TimeTable;

public class YearSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_shedule);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_navigation, R.string.close_navigation);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(YearSchedule.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(YearSchedule.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(YearSchedule.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(YearSchedule.this, TimeTable.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(YearSchedule.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(YearSchedule.this, HomeScreen.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(YearSchedule.this, HomeScreen.class));
        finish();
    }
}
