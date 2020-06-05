package com.krraju.vise.timetable;

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

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.yearshedule.YearSchedule;

public class TimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        PhotoView photoView = findViewById(R.id.timetable);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_navigation, R.string.close_navigation);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(TimeTable.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(TimeTable.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(TimeTable.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(TimeTable.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(TimeTable.this, YearSchedule.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    static class Image{
        private String image;

        public Image() {
        }

        public Image(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(TimeTable.this, HomeScreen.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TimeTable.this,HomeScreen.class));
        finish();
    }
}
