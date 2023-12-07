package com.thenextus.socialapp.classes.app

import com.thenextus.socialapp.classes.database.SocialAppDatabase
import com.thenextus.socialapp.classes.database.SocialAppRepository

object ServiceLocator {
    private var socialAppDatabase: SocialAppDatabase? = null
    private var socialAppRepository: SocialAppRepository? = null

    fun initDatabase(socialAppDatabase: SocialAppDatabase) {
        this.socialAppDatabase = socialAppDatabase
        socialAppRepository = socialAppDatabase.socialAppDao().let { SocialAppRepository(it) }
    }

    fun provideSocialAppDatabase(): SocialAppDatabase {
        return socialAppDatabase ?: throw IllegalArgumentException("Database in ServiceLocator not initialized")
    }

    fun provideSocialAppRepository(): SocialAppRepository {
        return socialAppRepository ?: throw IllegalArgumentException("Repository in ServiceLocator not initialized")
    }
}