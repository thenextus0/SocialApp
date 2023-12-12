package com.thenextus.socialapp.classes.database

import android.icu.util.Freezable
import android.util.Log
import androidx.lifecycle.LiveData
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User

class AppDatabaseRepository(private val appDao: AppDao) {

    //Users

    fun getAllUsers(): LiveData<List<User>>? {
        return appDao.getAllUser()
    }

    fun getSpesificUser(userID: String): LiveData<User> {
        return appDao.getSpesificUser(userID)
    }
    fun getSpesificUserByEmail(email: String): LiveData<User>? {
        return appDao.getSpesificUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        appDao.insertUser(user.userID, user.firstName, user.lastName, user.email, user.picture)
    }

    suspend fun deleteUser(userID: String) {
        appDao.deleteUser(userID)
    }

    //ApiUsers

    fun getAllApiUsers(): LiveData<List<ApiUser>>? {
        return appDao.getAllApiUser()
    }

    fun getSpesificApiUser(userID: String): ApiUser? {
        return appDao.getSpesificApiUser(userID)
    }

    suspend fun insertApiUser(user: ApiUser) {
        appDao.insertApiUser(user.userID, user.firstName, user.lastName, user.email, user.picture)
    }

    suspend fun deleteApiUser(userID: String) {
        appDao.deleteApiUser(userID)
    }

    //Friends

    fun getAllFriends(): LiveData<List<Friend>>? {
        return appDao.getAllFriends()
    }

    suspend fun insertFriend(friend: Friend){
        var test = appDao.insertFriend(friend.friendID, friend.userID, friend.userFriendID)
        Log.d("Fatal", test.toString())
    }

    fun getAllFriendsByUserID(userID: String): LiveData<List<Friend>>? {
        return appDao.getAllFriendsByUserID(userID)
    }

    suspend fun deleteFriend(friendRowID: String) {
        appDao.deleteFriendShip(friendRowID)
    }

    /*fun getSpesificFriendByID(friendID: String): LiveData<Friends> {
        return appDao.getSpesificFriendByID(friendID)
    }*/


}