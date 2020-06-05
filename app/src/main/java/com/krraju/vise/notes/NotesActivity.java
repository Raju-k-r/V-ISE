package com.krraju.vise.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.timetable.TimeTable;
import com.krraju.vise.yearshedule.YearSchedule;

public class NotesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.navigationDrawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        toggleButton = new ActionBarDrawerToggle(NotesActivity.this,drawerLayout, toolbar,R.string.open_navigation,R.string.close_navigation);
        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(NotesActivity.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(NotesActivity.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(NotesActivity.this, TimeTable.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(NotesActivity.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(NotesActivity.this, YearSchedule.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void buttonClicked(View v){
        CardView cardView = findViewById(v.getId());
        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
        TextView textView = (TextView) linearLayout.getChildAt(1);
        Intent intent = new Intent(NotesActivity.this, ListViewClass.class);
        intent.putExtra("subject",textView.getText());
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent homeScreenIntent = new Intent(NotesActivity.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        finish();
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            startActivity(new Intent(NotesActivity.this, HomeScreen.class));
            finish();
        }
    }

}
