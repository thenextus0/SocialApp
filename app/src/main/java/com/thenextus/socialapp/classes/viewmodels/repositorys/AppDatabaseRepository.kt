package com.thenextus.socialapp.classes.viewmodels.repositorys

import androidx.lifecycle.LiveData
import com.thenextus.socialapp.classes.database.dao.ApiUsersDao
import com.thenextus.socialapp.classes.database.dao.FriendsDao
import com.thenextus.socialapp.classes.database.dao.UsersDao
import com.thenextus.socialapp.classes.database.entities.ApiUser
import com.thenextus.socialapp.classes.database.entities.Friend
import com.thenextus.socialapp.classes.database.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppDatabaseRepository(val usersDao: UsersDao, val apiUsersDao: ApiUsersDao, val friendsDao: FriendsDao) {

    //Users

    /*fun getAllUsers(): LiveData<List<User>>? {
        return usersDao.getAllUser()
    }*/

    fun getSpesificUser(userID: String): Flow<User?> {
        return usersDao.getSpesificUser(userID)
    }
    fun getSpesificUserByEmail(email: String): Flow<User?> {
        return usersDao.getSpesificUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        usersDao.insertUser(user.userID, user.firstName, user.lastName, user.email, user.picture)
    }

    suspend fun updateUserInfo(changedUser: User) {
        withContext(Dispatchers.IO) {
            usersDao.updateUserInfo(changedUser.userID, changedUser.firstName, changedUser.lastName, changedUser.email, changedUser.picture)
        }
    }

    //ApiUsers

    fun getSpecificApiUser(apiUserID: String): Flow<ApiUser?> {
        return apiUsersDao.getSpecificApiUser(apiUserID)
    }

    suspend fun getUsersByIdList(friendIDList: List<String>): List<ApiUser> {
        return withContext(Dispatchers.IO) { apiUsersDao.getUsersByIdList(friendIDList) }
    }

    suspend fun insertApiUser(apiUser: ApiUser) {
        apiUsersDao.insertApiUser(apiUser.userID, apiUser.firstName, apiUser.lastName, apiUser.email, apiUser.picture)
    }

    //Friends

    suspend fun insertFriend(friend: Friend) {
        friendsDao.insertFriend(friend.friendRowID, friend.friendUserID, friend.apiUserID)
    }

    suspend fun deleteFriendship(friendRowID: String) {
        withContext(Dispatchers.IO) {
            friendsDao.deleteFriendShip(friendRowID)
        }
    }

    fun getSpecificFriend(userID: String, apiUserID: String): Flow<Friend?> {
        return friendsDao.getSpecificFriend(userID, apiUserID)
    }

    fun getAllFriendsByID(userID: String): Flow<List<Friend>> {
        return friendsDao.getAllFriendsByID(userID)
    }

    suspend fun checkFriendshipStatus(userID: String, userIDList: List<String>): List<Boolean> {
        val friendshipList = withContext(Dispatchers.IO) {
            friendsDao.getFriendshipStatus(userID, userIDList)
        }

        return userIDList.map { friendID ->
            friendshipList.any { friendship ->
                friendship.apiUserID == friendID
            }
        }
    }

}