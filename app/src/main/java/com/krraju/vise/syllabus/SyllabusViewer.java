package com.krraju.vise.syllabus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;

public class SyllabusViewer extends AppCompatActivity {

    private Toolbar toolbar;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_viewer);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        pdfView = findViewById(R.id.pdfViewer);

        Intent intent = getIntent();
        if(intent==null){
            Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT).show();
            finish();
        }

        String fileName = intent.getStringExtra("subjectName");
        toolbar.setTitle(fileName);
        fileName = fileName.replaceAll(" ","_");
        fileName = fileName.toLowerCase();
        fileName += ".pdf";
        Log.d("ISE",fileName);
        pdfView.fromAsset(fileName)
                .load();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(SyllabusViewer.this, HomeScreen.class));
        finish();
        return true;
    }
}
