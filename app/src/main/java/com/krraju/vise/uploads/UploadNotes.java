package com.krraju.vise.uploads;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krraju.vise.R;

public class UploadNotes extends AppCompatActivity {

    private static final int CHOOSE_FILE_INTENT_CODE = 121;
    private Spinner spinner;
    private Button attachFileButton;
    private Button uploadButton;
    private EditText fileName;
    private String subject;
    private Uri filePath = null;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference("Notes");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Notes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);
        spinner = findViewById(R.id.sub_spinner);
        attachFileButton = findViewById(R.id.file_attach);
        uploadButton = findViewById(R.id.uploadButton);
        fileName = findViewById(R.id.fileName);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.subject_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), subject, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        attachFileButton.setOnClickListener((v) -> chooseAFile());

        uploadButton.setOnClickListener((v) -> uploadFile());
    }

    private void uploadFile() {

        if (fileName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "File name should not be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadButton.setClickable(false);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgress(0);
        dialog.setCancelable(false);
        dialog.show();


        if (filePath != null) {
            StorageReference fileReference = storageReference.child(subject).child(System.currentTimeMillis() + getFileExtension(filePath));
            fileReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                databaseReference.child(subject).push().setValue(new NotesData(fileName.getText().toString(), uri.toString()))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getApplicationContext(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploading " + (int) progress);
                            dialog.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void chooseAFile() {
        Intent chooseFileIntent = new Intent();
        chooseFileIntent.setType("application/pdf");
        chooseFileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooseFileIntent, CHOOSE_FILE_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CHOOSE_FILE_INTENT_CODE && data != null && resultCode == RESULT_OK
                && data.getData() != null) {
            filePath = data.getData();
            Toast.makeText(getApplicationContext(), "File Attached", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
