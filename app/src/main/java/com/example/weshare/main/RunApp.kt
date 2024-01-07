package com.example.weshare.main

import android.app.Application
import android.os.Bundle
import com.google.firebase.FirebaseApp

class RunApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Any other global initialization can be done here
    }
}