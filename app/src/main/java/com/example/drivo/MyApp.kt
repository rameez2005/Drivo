package com.example.drivo

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase. Expects google-services.json to be present in app/ if using default project
        FirebaseApp.initializeApp(this)
    }
}

