package com.krraju.vise.attendancemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AttendanceStorage {
    private static final String LIST_KYE = "AttendanceList";

    public static void write(Context context, List<Attendance> list){
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KYE,jsonString);
        editor.apply();
    }

    public static ArrayList<Attendance> read(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = preferences.getString(LIST_KYE,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Attendance>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static void resetTheData(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LIST_KYE,"");
        editor.apply();
    }
}
