package com.example.pyong.vehicle_tracking_system;

import android.app.Application;

import com.firebase.client.Firebase;

public class FireBaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
