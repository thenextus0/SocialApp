package com.thenextus.socialapp.classes.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thenextus.socialapp.classes.database.entities.ApiUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ApiUsersDao {

    @Query("SELECT * FROM ApiUsers WHERE userID=:apiUserID")
    fun getSpecificApiUser(apiUserID: String): Flow<ApiUser?>

    @Query("SELECT * FROM ApiUsers WHERE userID IN (:friendList)")
    suspend fun getUsersByIdList(friendList: List<String>): List<ApiUser>


    @Query("INSERT INTO ApiUsers (userID, firstName, lastName, email, pictureURL) VALUES (:userID, :firstName, :lastName, :email, :pictureURL)")
    suspend fun insertApiUser(userID: String, firstName: String?, lastName: String?, email: String, pictureURL: String?)

    @Query("DELETE FROM ApiUsers WHERE userID=:userID")
    suspend fun deleteApiUser(userID: String)
}