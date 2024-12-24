package com.android.kickwish

import android.app.Application
import com.google.firebase.FirebaseApp

class KickWish : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}