package com.thenextus.socialapp.classes.socialapp

import com.thenextus.socialapp.classes.database.AppDatabase
import com.thenextus.socialapp.classes.database.AppDatabaseRepository

object ServiceLocator {
    private var appDatabase: AppDatabase? = null
    private var appDatabaseRepository: AppDatabaseRepository? = null

    fun initDatabase(appDatabase: AppDatabase) {
        this.appDatabase = appDatabase

        appDatabaseRepository = appDatabase.let { databaseClass ->

            val usersDao = databaseClass.usersDao()
            val apiUsersDao = databaseClass.apiUsersDao()
            val friendsDao = databaseClass.friendsDao()
            AppDatabaseRepository(usersDao, apiUsersDao, friendsDao)

        }

        //appDatabaseRepository = appDatabase.appDao().let { AppDatabaseRepository(it) }

    }

    fun provideAppDatabase(): AppDatabase {
        return appDatabase ?: throw IllegalArgumentException("Database in ServiceLocator not initialized")
    }

    fun provideRepository(): AppDatabaseRepository {
        return appDatabaseRepository ?: throw IllegalArgumentException("Repository in ServiceLocator not initialized")
    }


}