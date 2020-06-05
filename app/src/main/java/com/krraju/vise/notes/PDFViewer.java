package com.krraju.vise.notes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.clans.fab.FloatingActionButton;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFViewer extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 121;
    private Toolbar toolbar;
    private PDFView pdfView;
    private FloatingActionButton downloadButton;
    private Dialog progressDialog;
    private int outSideClick;

    private String title;
    private String subjectName;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        outSideClick =0;

        Intent receivedIntent = getIntent();
        if(receivedIntent == null){
            Toast.makeText(getApplicationContext(),"Can't load the PDF",Toast.LENGTH_LONG).show();
            finish();
        }

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        ((RelativeLayout) progressDialog.findViewById(R.id.touch_outside)).setOnClickListener(v->{
            outSideClick++;
            if(outSideClick == 2){
                onBackPressed();
            }
        });
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Toast.makeText(this,"Double tap on dialog to dismiss",Toast.LENGTH_SHORT).show();

        title = receivedIntent.getStringExtra("title");
        toolbar.setTitle(title);
        uri = receivedIntent.getStringExtra("uri");
        subjectName = receivedIntent.getStringExtra("subjectName");
        pdfView = findViewById(R.id.pdfViewer);
        downloadButton = findViewById(R.id.download_pdf);

        new RetrievePdfStream().execute(uri);

        downloadButton.setOnClickListener((v)-> {
            checkForPermission();
        });

    }

    private void downloadFile() {

        try{
            DownloadFile downloadFile = new DownloadFile(this,title,subjectName,uri);
            downloadFile.start();
        }catch (Exception e){
            Toast.makeText(this,"Download Failed..",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            else{
                downloadFile();
            }
        }else {
            downloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0){
            if(requestCode == PERMISSION_REQUEST_CODE){
                downloadFile();
            }
        }else {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent homeScreenIntent = new Intent(PDFViewer.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        finish();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class RetrievePdfStream extends AsyncTask<String, Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .onLoad( i-> progressDialog.dismiss())
                    .load();
        }
    }
}
