package com.thenextus.socialapp.classes.socialapp

import com.thenextus.socialapp.classes.database.AppDatabase
import com.thenextus.socialapp.classes.database.AppDatabaseRepository

object ServiceLocator {
    private var appDatabase: AppDatabase? = null
    private var appDatabaseRepository: AppDatabaseRepository? = null

    fun initDatabase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase
        appDatabaseRepository = appDatabase.appDao().let { AppDatabaseRepository(it) }
    }

    fun provideAppDatabase(): AppDatabase {
        return appDatabase ?: throw IllegalArgumentException("Database in ServiceLocator not initialized")
    }

    fun provideRepository(): AppDatabaseRepository {
        return appDatabaseRepository ?: throw IllegalArgumentException("Repository in ServiceLocator not initialized")
    }


}