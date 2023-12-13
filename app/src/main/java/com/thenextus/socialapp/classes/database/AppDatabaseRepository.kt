package com.thenextus.socialapp.classes.database

import android.icu.util.Freezable
import android.util.Log
import androidx.lifecycle.LiveData
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User

class AppDatabaseRepository(val appDao: AppDao) {

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

    fun getAll(): List<Friend> {
        return appDao.getAll()
    }

    fun insertDefault(friend: Friend): Long {
        return appDao.insertDefault(friend)
    }

    suspend fun deleteFriend(friendRowID: String) {
        appDao.deleteFriendShip(friendRowID)
    }

    fun getSpecificFriends(userID: String, apiUserID: String): Friend? {
        return appDao.getSpecificFriends(userID, apiUserID)
    }

    fun getAllFriendsByID(userID: String): List<Friend>? {
        return appDao.getAllFriendsByID(userID)
    }


}