package com.krraju.vise.syllabus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.cgpacalculator.CGPACalculator;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.timetable.TimeTable;
import com.krraju.vise.yearshedule.YearSchedule;

public class Syllabus extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sylabus);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);
        toggleButton = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation,R.string.close_navigation);
        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();


        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, CGPACalculator.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, TimeTable.class));
                    finish();
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(Syllabus.this, YearSchedule.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
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
        Toast.makeText(this,item.getTitle(),Toast.LENGTH_LONG).show();
        Intent homeScreenIntent = new Intent(Syllabus.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            startActivity(new Intent(Syllabus.this, HomeScreen.class));
            finish();
        }
    }

    public void buttonClicked(View v){
        CardView cardView = findViewById(v.getId());
        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
        TextView textView = (TextView) linearLayout.getChildAt(1);

        Intent newIntent = new Intent(Syllabus.this, SyllabusViewer.class);
        newIntent.putExtra("subjectName",textView.getText());
        startActivity(newIntent);
    }
}
