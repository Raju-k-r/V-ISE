package com.krraju.vise.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.uploads.NotesData;

import java.util.ArrayList;

public class ListViewClass extends AppCompatActivity {

    private ListView listView;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private ArrayList<NotesData> notesDataArrayList;
    private TextView emptyTextView;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent receivedIntent = getIntent();
        subject = receivedIntent.getStringExtra("subject");
        toolbar.setTitle(subject);

        databaseReference = database.getReference().child("Notes/" + subject);
        listView = findViewById(R.id.list_View);
        emptyTextView = findViewById(R.id.empty_list);

        notesDataArrayList = new ArrayList<>();
        viewAllFiles();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NotesData notesData = notesDataArrayList.get(position);
            Intent intent = new Intent(ListViewClass.this, PDFViewer.class);
            intent.putExtra("title",notesData.getFileName());
            intent.putExtra("uri",notesData.getFilePath());
            intent.putExtra("subjectName",subject);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent homeScreenIntent = new Intent(ListViewClass.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_button,menu);
        return true;
    }

    private void viewAllFiles() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesDataArrayList.clear();
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    NotesData notesData = postSnapShot.getValue(NotesData.class);
                    notesDataArrayList.add(notesData);
                }

                if(notesDataArrayList.isEmpty()){
                    emptyTextView.setVisibility(View.VISIBLE);
                }

                String[] notesTitle = new String[notesDataArrayList.size()];
                for(int i = 0; i< notesDataArrayList.size(); i++){
                    notesTitle[i] = notesDataArrayList.get(i).getFileName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,notesTitle);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
