package com.krraju.vise.register;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krraju.vise.R;
import com.krraju.vise.homescreen.HomeScreen;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private static final String USER_LOGIN_INFO = "userLoginInfo";
    private EditText userName;
    private EditText usnNumber;
    private FirebaseAuth firebaseAuth;
    private Button registerButton;
    private Dialog dialog;

    private DatabaseReference reference;
    private ArrayList<IsAdmin> admins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.userNameEditText);
        usnNumber = findViewById(R.id.usnNameEditText);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener( (view) -> registerUser());
        userName.setFilters(new InputFilter[]{ new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        usnNumber.setFilters(new InputFilter[]{ new InputFilter.AllCaps(), new InputFilter.LengthFilter(10)});
        firebaseAuth = FirebaseAuth.getInstance();
        admins = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    IsAdmin admin = snapshot.getValue(IsAdmin.class);
                    admins.add(admin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void registerUser() {
        registerButton.setClickable(false);

        String userName = this.userName.getText().toString();
        String usnNumber = this.usnNumber.getText().toString();

        // Verify User Name
        if(!verifyUserName(userName)){
            Toast.makeText(this,"Invalid Name",Toast.LENGTH_LONG).show();
            return;
        }

        // Verify USN Number
        if(!verifyUSNNumber(usnNumber)) {
            Toast.makeText(this, "Invalid USN Number", Toast.LENGTH_LONG).show();
            return;
        }

        // Show the Dialog Box
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.progress_dialog);
        ((TextView)dialog.findViewById(R.id.message)).setText("Please Wait");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setCancelable(false);

        String email = userName.replaceAll(" ","_") + "@uvce.com";
        firebaseAuth.createUserWithEmailAndPassword(email,usnNumber)
                .addOnCompleteListener(this, (task) -> {
                    if(task.isSuccessful()){
                        changeToHomeScree();
                    }
                })
                .addOnFailureListener(this, e -> firebaseAuth.signInWithEmailAndPassword(email,usnNumber)
                        .addOnSuccessListener((authResult) -> changeToHomeScree())
                        .addOnFailureListener((exception) -> Toast.makeText(getApplicationContext(), "Some thing Went wrong Plz try after some time..",Toast.LENGTH_SHORT).show()));
    }

    private void changeToHomeScree(){

        checkForAdmin();

        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
        Intent homeScreenIntent = new Intent(RegisterActivity.this, HomeScreen.class);
        startActivity(homeScreenIntent);
        SharedPreferences sharedPreferences = getSharedPreferences(USER_LOGIN_INFO,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("IS_USER_LOGGED_IN",true).apply();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        finish();
    }

    private static class IsAdmin{
        private String usnNumber;

        public String getUsnNumber() {
            return usnNumber;
        }

        public IsAdmin() {
        }

    }

    private void checkForAdmin() {
        String s = usnNumber.getText().toString().trim();
        for(IsAdmin admin : admins){
            String ad = admin.usnNumber;
            if(s.equals(ad.trim())){
                getSharedPreferences(USER_LOGIN_INFO,MODE_PRIVATE)
                        .edit()
                        .putBoolean("isAdmin",true)
                        .apply();
                break;
            }

        }
    }

    private boolean verifyUserName(String userName) {
        return !userName.trim().equals("") && userName.length() > 4;
    }

    private boolean verifyUSNNumber(String usnName) {
        return usnName.matches("18GA[A-Z]{2}60[0-9]{2}");
    }
}
