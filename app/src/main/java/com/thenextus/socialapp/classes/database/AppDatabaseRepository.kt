package com.thenextus.socialapp.classes.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun updateUserInfo(changedUser: User) {
        withContext(Dispatchers.IO) {
            appDao.updateUserInfo(changedUser.userID, changedUser.firstName, changedUser.lastName, changedUser.email, changedUser.picture)
        }
    }

    //ApiUsers

    fun getSpecificApiUser(apiUserID: String): LiveData<ApiUser> {
        return appDao.getSpecificApiUser(apiUserID)
    }

    suspend fun getUsersByIdList(friendIDList: List<String>): List<ApiUser> {
        return withContext(Dispatchers.IO) {
            appDao.getUsersByIdList(friendIDList)
        }
    }

    suspend fun insertApiUser(apiUser: ApiUser) {
        appDao.insertApiUser(apiUser.userID, apiUser.firstName, apiUser.lastName, apiUser.email, apiUser.picture)
    }

    //Friends

    suspend fun insertFriend(friend: Friend) {
        appDao.insertFriend(friend.friendRowID, friend.friendUserID, friend.apiUserID)
    }

    suspend fun deleteFriendship(friendRowID: String) {
        withContext(Dispatchers.IO) {
            appDao.deleteFriendShip(friendRowID)
        }
    }

    fun getSpecificFriend(userID: String, apiUserID: String): LiveData<Friend> {
        return appDao.getSpecificFriend(userID, apiUserID)
    }

    fun getAllFriendsByID(userID: String): LiveData<List<Friend>> {
        return appDao.getAllFriendsByID(userID)
    }

    suspend fun checkFriendshipStatus(userID: String, userIDList: List<String>): List<Boolean> {
        val friendshipList = withContext(Dispatchers.IO) {
            appDao.getFriendshipStatus(userID, userIDList)
        }

        return userIDList.map { friendID ->
            friendshipList.any { friendship ->
                friendship.apiUserID == friendID
            }
        }
    }

}