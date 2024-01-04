package com.thenextus.socialapp.classes.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    /*@Query("SELECT * FROM Users")
    fun getAllUser(): LiveData<List<User>>?*/

    @Query("SELECT * FROM Users WHERE userID=:userID")
    fun getSpesificUser(userID: String): Flow<User?>

    @Query("SELECT * FROM Users WHERE email=:email")
    fun getSpesificUserByEmail(email: String): Flow<User?>

    @Query("INSERT INTO Users (userID, firstName, lastName, email, pictureURL) VALUES (:userID, :firstName, :lastName, :email, :pictureURL)")
    suspend fun insertUser(userID: String, firstName: String?, lastName: String?, email: String, pictureURL: String?)

    @Query("UPDATE Users SET firstName= :firstName, lastName= :lastName, email= :email, pictureURL= :pictureURL WHERE userID= :userID")
    fun updateUserInfo(userID: String, firstName: String?, lastName: String?, email: String, pictureURL: String?)
}