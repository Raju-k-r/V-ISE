package com.krraju.vise.homescreen.announcement;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class SingleTon {
    private static SingleTon instance;
    private LinkedList<DataSnapshot> dataSnapshots;
    private DatabaseReference reference;

    private SingleTon(){
        reference = FirebaseDatabase.getInstance().getReference().child("Announcements");
        dataSnapshots = new LinkedList<>();
    }

    public static SingleTon getInstance(){
        if(instance == null){
            instance = new SingleTon();
        }
        return instance;
    }

    public LinkedList<DataSnapshot> getDataSnapshots() {
        return dataSnapshots;
    }
}
