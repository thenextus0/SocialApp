package com.thenextus.socialapp.classes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class SocialAppDatabase: RoomDatabase() {

    abstract fun socialAppDao(): SocialAppDao

    companion object {
        @Volatile
        private var instanceF: SocialAppDatabase? = null

        fun getInstance(context: Context): SocialAppDatabase {
            return instanceF ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocialAppDatabase::class.java,
                    "SocialAppDatabase"
                ).build()
                instanceF = instance
                instance
            }
        }

    }
}