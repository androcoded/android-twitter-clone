package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("i5aRKVS4n5AEDhrC8An7FA3Ca5fAknKkTCZH286g")
                .clientKey("ioZC8h5hmXmkpXBz3pxPDk6EapC066jrogYAFlTy")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
