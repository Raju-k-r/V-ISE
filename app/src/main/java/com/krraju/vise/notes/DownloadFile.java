package com.krraju.vise.notes;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class DownloadFile {

    private Context context;
    private String fileName;
    private String subjectName;
    private String uri;

    public DownloadFile(Context context, String fileName, String subjectName, String uri){
        this.context = context;
        this.fileName = fileName + ".pdf";
        this.subjectName = subjectName;
        this.uri = uri;
    }

    public void start(){
        new FileDownload().execute(uri);
    }

    @SuppressLint("StaticFieldLeak")
    private class FileDownload extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Downloading...",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/Announcements");

            if(!directory.exists()){
                directory.mkdirs();
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(strings[0]));
            request.setDescription("Downloading...")
                    .setTitle(fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir("/Announcements",subjectName.substring(0,4) + "_" +fileName)
                    .allowScanningByMediaScanner();
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }
}
