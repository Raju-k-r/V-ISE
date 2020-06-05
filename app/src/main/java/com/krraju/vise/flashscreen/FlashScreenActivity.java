package com.krraju.vise.flashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;
import com.krraju.vise.homescreen.announcement.SingleTon;
import com.krraju.vise.register.RegisterActivity;

import java.util.LinkedList;

public class FlashScreenActivity extends AppCompatActivity {

    private static final String USER_LOGIN_INFO = "userLoginInfo";
    private SharedPreferences userInfo;
    private LinkedList<DataSnapshot> dataSnapshots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        userInfo = getSharedPreferences(USER_LOGIN_INFO, MODE_PRIVATE);
        dataSnapshots = SingleTon.getInstance().getDataSnapshots();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Announcements");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataSnapshots.addFirst(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshots.remove(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(()-> {
            if(userInfo.getBoolean("IS_USER_LOGGED_IN",false)){
                Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_LONG).show();
                startActivity(new Intent(FlashScreenActivity.this, HomeScreen.class));
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "User Is not Registered",Toast.LENGTH_LONG).show();
                startActivity(new Intent(FlashScreenActivity.this, RegisterActivity.class));
                finish();
            }
        },2000);
    }
}
