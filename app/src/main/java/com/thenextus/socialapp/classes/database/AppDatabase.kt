package com.thenextus.socialapp.classes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.Dispatchers

@Database(entities = [User::class, Friend::class, ApiUser::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var instanceF: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instanceF ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "SocialAppDatabase"
                )
                    .build()
                instanceF = instance
                instance
            }
        }

    }

}