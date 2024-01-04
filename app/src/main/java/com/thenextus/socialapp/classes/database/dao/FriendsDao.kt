package com.thenextus.socialapp.classes.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.thenextus.socialapp.classes.database.entities.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("INSERT INTO Friends (friendRowID, friendUserID, apiUserID) VALUES (:friendRowID, :friendUserID, :apiUserID)")
    suspend fun insertFriend(friendRowID: String, friendUserID: String, apiUserID: String)

    @Query("DELETE FROM Friends WHERE friendRowID=:friendRowID")
    suspend fun deleteFriendShip(friendRowID: String)

    @Query("SELECT * FROM Friends WHERE friendUserID=:userID AND apiUserID=:apiUserID")
    fun getSpecificFriend(userID: String, apiUserID: String): Flow<Friend?>

    @Query("SELECT * FROM Friends WHERE friendUserID=:userID")
    fun getAllFriendsByID(userID: String): Flow<List<Friend>>

    @Query("SELECT * FROM Friends WHERE (friendUserID = :userID AND apiUserID IN (:userIDList))")
    fun getFriendshipStatus(userID: String, userIDList: List<String>): List<Friend>
}