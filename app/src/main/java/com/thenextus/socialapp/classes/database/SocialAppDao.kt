package com.thenextus.socialapp.classes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SocialAppDao {

    @Query("SELECT * FROM Users")
    fun getAllUser(): LiveData<List<User>>?

    @Query("SELECT * FROM Users WHERE userID=:userID")
    fun getSpesificUser(userID: String): LiveData<User>

    @Query("INSERT INTO Users (userID, name, email, pictureURL) VALUES (:userID, :name, :email, :pictureURL)")
    suspend fun insertUser(userID: String, name: String, email: String, pictureURL: String)

    @Query("DELETE FROM Users WHERE userID=:userID")
    suspend fun deleteUser(userID: String)

}