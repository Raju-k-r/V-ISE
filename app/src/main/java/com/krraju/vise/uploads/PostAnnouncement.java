package com.krraju.vise.uploads;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.announcement.DataClass;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PostAnnouncement extends AppCompatActivity {

    private static final String USER_LOGIN_INFO = "userLoginInfo";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference("Announcements");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Announcements");

    private EditText message;
    private Uri fileURL;
    private String fileType;
    private Button sendButton;

    private final int GET_IMAGE_REQUEST_CODE = 120;
    private final int GET_FILE_REQUEST_CODE = 220;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_announcement);

        SharedPreferences userInfo = getSharedPreferences(USER_LOGIN_INFO,MODE_PRIVATE);
        if (!userInfo.getBoolean("isAdmin",false)) {
            Toast.makeText(this,"User Not Admin",Toast.LENGTH_LONG).show();
        }

        fileURL = null;
        fileType = "null";

        Button attachFileButton = findViewById(R.id.attachFile);
        message = findViewById(R.id.message);

        attachFileButton.setOnClickListener((v) ->{
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GET_FILE_REQUEST_CODE);
        });

        Button attachImageButton = findViewById(R.id.attachImage);
        attachImageButton.setOnClickListener((v)-> {
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imageIntent,GET_IMAGE_REQUEST_CODE);
        });

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener((v) -> {
            if(!message.getText().toString().trim().equals("")) {
                uploadFile();
            }else {
                Toast.makeText(getApplicationContext(), "Message is Empty!..",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFile() {
        sendButton.setClickable(false);
        if(fileType.equals("Image") || fileType.equals("File")){
            if(fileURL!=null){

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setProgress(0);
                progressDialog.setCancelable(false);
                progressDialog.show();

                StorageReference newReference = storageReference.child(System.currentTimeMillis() + getFileExtension(fileURL));
                newReference.putFile(fileURL)
                        .addOnSuccessListener((taskSnapshot)-> {
                                if(taskSnapshot != null && taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference()!=null){
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener((uri)-> {
                                            DataClass dataClass = new DataClass(message.getText().toString(),
                                                    fileType, uri.toString(), simpleDateTimeFormatter.format(new Date()));
                                            databaseReference.push().setValue(dataClass, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),String.format("%s %s", fileType,"Updated Successfully"),Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });

                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(),"Something went wrong !",Toast.LENGTH_SHORT).show();
                                }
                        })
                        .addOnProgressListener((taskSnapshot)-> {
                                double process = 100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                                progressDialog.setProgress((int) process);
                                progressDialog.setMessage("Uploading " + (int)process);
                        });
            }else{
                Toast.makeText(getApplicationContext(),"Please Select a file",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            DataClass dataClass = new DataClass(message.getText().toString(), fileType,"null", simpleDateTimeFormatter.format(new Date()));
            databaseReference.push().setValue(dataClass, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                }
            });
            Toast.makeText(getApplicationContext(),"The Message Sent",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == GET_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            fileURL = data.getData();
            fileType = "Image";
            Toast.makeText(getApplicationContext(),"Image Attached",Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == GET_FILE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            fileURL = data.getData();
            fileType =  "File";
            Toast.makeText(getApplicationContext(),"File Attached",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
