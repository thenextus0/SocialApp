package com.thenextus.socialapp.classes.socialapp

import android.media.metrics.Event
import com.thenextus.socialapp.classes.database.AppDatabase
import com.thenextus.socialapp.classes.viewmodels.repositorys.AppDatabaseRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private var appDatabase: AppDatabase? = null
    private var appDatabaseRepository: AppDatabaseRepository? = null

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://randomuser.me/"

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

    fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideRetrofit(): Retrofit {
        return retrofit ?: throw IllegalArgumentException("Retrofit is not initialized")
    }

}