package com.thenextus.socialapp.classes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User

@Dao
interface AppDao {

    //Users

    @Query("SELECT * FROM Users")
    fun getAllUser(): LiveData<List<User>>?

    @Query("SELECT * FROM Users WHERE userID=:userID")
    fun getSpesificUser(userID: String): LiveData<User>

    @Query("SELECT * FROM Users WHERE email=:email")
    fun getSpesificUserByEmail(email: String): LiveData<User>?

    @Query("INSERT INTO Users (userID, firstName, lastName, email, pictureURL) VALUES (:userID, :firstName, :lastName, :email, :pictureURL)")
    suspend fun insertUser(userID: String, firstName: String?, lastName: String?, email: String, pictureURL: String?)

    @Query("DELETE FROM Users WHERE userID=:userID")
    suspend fun deleteUser(userID: String)

    //ApiUsers

    @Query("SELECT * FROM ApiUsers WHERE userID=:apiUserID")
    fun getSpecificApiUser(apiUserID: String): LiveData<ApiUser>

    @Query("SELECT * FROM ApiUsers WHERE userID IN (:friendList)")
    suspend fun getUsersByIdList(friendList: List<String>): List<ApiUser>


    @Query("INSERT INTO ApiUsers (userID, firstName, lastName, email, pictureURL) VALUES (:userID, :firstName, :lastName, :email, :pictureURL)")
    suspend fun insertApiUser(userID: String, firstName: String?, lastName: String?, email: String, pictureURL: String?)

    @Query("DELETE FROM ApiUsers WHERE userID=:userID")
    suspend fun deleteApiUser(userID: String)

    //Friends

    @Query("INSERT INTO Friends (friendRowID, friendUserID, apiUserID) VALUES (:friendRowID, :friendUserID, :apiUserID)")
    suspend fun insertFriend(friendRowID: String, friendUserID: String, apiUserID: String)

    @Query("DELETE FROM Friends WHERE friendRowID=:friendRowID")
    suspend fun deleteFriendShip(friendRowID: String)

    @Query("SELECT * FROM Friends WHERE friendUserID=:userID AND apiUserID=:apiUserID")
    fun getSpecificFriend(userID: String, apiUserID: String): LiveData<Friend>

    @Query("SELECT * FROM Friends WHERE friendUserID=:userID")
    fun getAllFriendsByID(userID: String): LiveData<List<Friend>>



}