package com.example.chrislin.recordrouteapplication;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Chrislin on 2018/3/6.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
