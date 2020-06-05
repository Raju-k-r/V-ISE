package com.krraju.vise.homescreen;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.timetable.TimeTable;
import com.krraju.vise.uploads.PostAnnouncement;
import com.krraju.vise.uploads.UploadNotes;
import com.krraju.vise.yearshedule.YearSchedule;

public class HomeScreen extends AppCompatActivity {

    private static final String USER_LOGIN_INFO = "userLoginInfo";
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        FloatingActionMenu floatingActionMenu = findViewById(R.id.floating_action_menu);
        FloatingActionButton announcementButton = findViewById(R.id.announcement_button);
        FloatingActionButton notesButton = findViewById(R.id.notes_button);

        if(!getSharedPreferences(USER_LOGIN_INFO, MODE_PRIVATE).getBoolean("isAdmin", false)){
            floatingActionMenu.setVisibility(View.GONE);
        }


        MainAnnouncementAdaptor adaptor = new MainAnnouncementAdaptor(this);
        RecyclerView recyclerView = findViewById(R.id.homeScreen_recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adaptor);
        }


        NavigationView navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(HomeScreen.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(HomeScreen.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(HomeScreen.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(HomeScreen.this,TimeTable.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(HomeScreen.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(this, YearSchedule.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        announcementButton.setOnClickListener((v) -> startActivity(new Intent(HomeScreen.this, PostAnnouncement.class)));
        notesButton.setOnClickListener((v) -> startActivity(new Intent(HomeScreen.this, UploadNotes.class)));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}