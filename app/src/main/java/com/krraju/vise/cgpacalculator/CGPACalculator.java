package com.krraju.vise.cgpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.krraju.vise.R;
import com.krraju.vise.attendancemanager.AttendanceManager;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.notes.NotesActivity;
import com.krraju.vise.syllabus.Syllabus;
import com.krraju.vise.timetable.TimeTable;
import com.krraju.vise.yearshedule.YearSchedule;

import java.util.LinkedHashMap;
import java.util.Map;

public class CGPACalculator extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner softwareArchitecture;
    private Spinner probability;
    private Spinner operationResearch;
    private Spinner advanceJava;
    private Spinner python;
    private Spinner computerOrganisation;

    private Map<String, String> cgpa;
    private Map<String, Double> cgpaValue;
    private Map<String, Integer> value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_g_p_a_calulator);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        cgpa = new LinkedHashMap<>();
        cgpaValue = new LinkedHashMap<>();
        value = new LinkedHashMap<>();

        initialize();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cgpa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner computerNetworking = findViewById(R.id.computer_networking);
        computerNetworking.setAdapter(adapter);
        computerNetworking.setOnItemSelectedListener(this);

        Spinner artificialIntelligence = findViewById(R.id.artificial_intelligence);
        artificialIntelligence.setAdapter(adapter);
        artificialIntelligence.setOnItemSelectedListener(this);

        Spinner computerGraphics = findViewById(R.id.computer_graphics);
        computerGraphics.setAdapter(adapter);
        computerGraphics.setOnItemSelectedListener(this);

        Spinner computerGraphicsLab = findViewById(R.id.computer_graphics_lab);
        computerGraphicsLab.setAdapter(adapter);
        computerGraphicsLab.setOnItemSelectedListener(this);

        Spinner dbms = findViewById(R.id.database_management_system);
        dbms.setAdapter(adapter);
        dbms.setOnItemSelectedListener(this);

        Spinner dbmsLab = findViewById(R.id.database_management_system_lab);
        dbmsLab.setAdapter(adapter);
        dbmsLab.setOnItemSelectedListener(this);

        softwareArchitecture = findViewById(R.id.software_architecture_and_testing);
        softwareArchitecture.setAdapter(adapter);
        softwareArchitecture.setOnItemSelectedListener(this);

        probability = findViewById(R.id.probability_and_stochastic_process);
        probability.setAdapter(adapter);
        probability.setOnItemSelectedListener(this);

        operationResearch = findViewById(R.id.operation_research);
        operationResearch.setAdapter(adapter);
        operationResearch.setOnItemSelectedListener(this);

        advanceJava = findViewById(R.id.advance_j2ee);
        advanceJava.setAdapter(adapter);
        advanceJava.setOnItemSelectedListener(this);


        python = findViewById(R.id.python);
        python.setAdapter(adapter);
        python.setOnItemSelectedListener(this);

        computerOrganisation = findViewById(R.id.computer_architecture);
        computerOrganisation.setAdapter(adapter);
        computerOrganisation.setOnItemSelectedListener(this);

        Button button = findViewById(R.id.calculateButton);
        button.setOnClickListener(v -> calculate());

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.syllabus:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(CGPACalculator.this, Syllabus.class));
                    finish();
                    break;
                case R.id.notes:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(CGPACalculator.this, NotesActivity.class));
                    finish();
                    break;
                case R.id.AttendanceManager:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(CGPACalculator.this, AttendanceManager.class));
                    finish();
                    break;
                case R.id.timetable:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(CGPACalculator.this, TimeTable.class));
                    finish();
                    break;
                case R.id.CGPACalculator:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.yearSchedule:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(CGPACalculator.this, YearSchedule.class));
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initialize() {
        cgpaValue.put("Computer Networking", 4.0);
        cgpaValue.put("Artificial Intelligence", 4.0);
        cgpaValue.put("Computer Graphics", 3.0);
        cgpaValue.put("Computer Graphics Lab", 1.5);
        cgpaValue.put("DBMS", 4.0);
        cgpaValue.put("DBMS LAB", 1.5);
        cgpaValue.put("Software Testing", 3.0);
        cgpaValue.put("Probability", 3.0);
        cgpaValue.put("Operation", 3.0);
        cgpaValue.put("Advance Java", 3.0);
        cgpaValue.put("Python", 3.0);
        cgpaValue.put("Architecture", 3.0);

        value.put("S",10);
        value.put("A",9);
        value.put("B",8);
        value.put("C",7);
        value.put("D",6);
        value.put("E",5);
        value.put("F",0);
        value.put("NA",0);
    }

    private void calculate() {
        double CGPA = 0.0;
        for(Map.Entry<String, String> c : cgpa.entrySet()){
            String kye = c.getKey();
            String value = c.getValue();

            double cgpa = cgpaValue.get(kye);

            int grade = this.value.get(value);

            CGPA += (cgpa * grade);
        }

        Toast.makeText(this ,"Your CGPA is: " +  String.format("%.2f",CGPA/24.0), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent homeScreenIntent = new Intent(CGPACalculator.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CGPACalculator.this, HomeScreen.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = (String) parent.getItemAtPosition(position);
        switch (parent.getId()) {
            // Main Subject
            case R.id.computer_networking:
                cgpa.put("Computer Networking", s);
                break;
            case R.id.artificial_intelligence:
                cgpa.put("Artificial Intelligence", s);
                break;
            case R.id.computer_graphics:
                cgpa.put("Computer Graphics", s);
                break;
            case R.id.database_management_system:
                cgpa.put("DBMS", s);
                break;

            // Lab's
            case R.id.computer_graphics_lab:
                cgpa.put("Computer Graphics Lab", s);
                break;
            case R.id.database_management_system_lab:
                cgpa.put("DBMS LAB", s);
                break;

            // Professional Electives
            case R.id.software_architecture_and_testing:
                if(!s.equals("NA")){
                    cgpa.put("Software Testing", s);
                    cgpa.put("Probability","NA");
                    cgpa.put("Operation", "NA");
                    probability.setSelection(7);
                    operationResearch.setSelection(7);
                }else {
                    String p = (String) probability.getSelectedItem();
                    String o = (String) operationResearch.getSelectedItem();

                    if(p.equals("NA") && o.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        softwareArchitecture.setSelection(0);
                    }
                }
                break;
            case R.id.probability_and_stochastic_process:
                if(!s.equals("NA")){
                    cgpa.put("Software Testing", "NA");
                    cgpa.put("Probability",s);
                    cgpa.put("Operation", "NA");
                    operationResearch.setSelection(7);
                    softwareArchitecture.setSelection(7);
                }else{
                    String o = (String) operationResearch.getSelectedItem();
                    String sa = (String) softwareArchitecture.getSelectedItem();

                    if(o.equals("NA") && sa.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        probability.setSelection(0);
                    }
                }
                break;
            case R.id.operation_research:
                if(!s.equals("NA")){
                    cgpa.put("Operation", s);
                    cgpa.put("Software Testing", "NA");
                    cgpa.put("Probability","NA");
                    softwareArchitecture.setSelection(7);
                    probability.setSelection(7);
                }else{
                    String o = (String) probability.getSelectedItem();
                    String sa = (String) softwareArchitecture.getSelectedItem();

                    if(o.equals("NA") && sa.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        operationResearch.setSelection(0);
                    }
                }
                break;

            // Open Elective
            case R.id.advance_j2ee:
                if(!s.equals("NA")){
                    cgpa.put("Advance Java", s);
                    cgpa.put("Python", "NA");
                    cgpa.put("Architecture", "NA");
                    python.setSelection(7);
                    computerOrganisation.setSelection(7);
                }else{
                    String o = (String) python.getSelectedItem();
                    String sa = (String) computerOrganisation.getSelectedItem();

                    if(o.equals("NA") && sa.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        advanceJava.setSelection(0);
                    }
                }
                break;
            case R.id.python:
                if(!s.equals("NA")){
                    cgpa.put("Advance Java", "NA");
                    cgpa.put("Python", s);
                    cgpa.put("Architecture", "NA");
                    advanceJava.setSelection(7);
                    computerOrganisation.setSelection(7);
                }else{
                    String o = (String) computerOrganisation.getSelectedItem();
                    String sa = (String) advanceJava.getSelectedItem();

                    if(o.equals("NA") && sa.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        python.setSelection(0);
                    }
                }
                break;
            case R.id.computer_architecture:
                if(!s.equals("NA")){
                    cgpa.put("Advance Java", "NA");
                    cgpa.put("Python", "NA");
                    cgpa.put("Architecture", s);
                    advanceJava.setSelection(7);
                    python.setSelection(7);
                }else{
                    String o = (String) advanceJava.getSelectedItem();
                    String sa = (String) python.getSelectedItem();

                    if(o.equals("NA") && sa.equals("NA")){
                        Toast.makeText(this, "You must select any One",Toast.LENGTH_SHORT).show();
                        computerOrganisation.setSelection(0);
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
