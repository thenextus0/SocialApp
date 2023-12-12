package com.thenextus.socialapp.classes.socialapp

import android.app.Application
import com.thenextus.socialapp.classes.database.AppDatabase

class SocialApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val appDatabase = AppDatabase.getInstance(this)
        ServiceLocator.initDatabase(appDatabase)
    }
}