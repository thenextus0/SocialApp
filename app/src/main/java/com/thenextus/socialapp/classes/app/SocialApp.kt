package com.thenextus.socialapp.classes.app

import android.app.Application
import com.thenextus.socialapp.classes.database.SocialAppDatabase

class SocialApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val socialAppDatabase = SocialAppDatabase.getInstance(this)
        ServiceLocator.initDatabase(socialAppDatabase)
    }
}