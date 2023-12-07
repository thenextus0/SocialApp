package com.thenextus.socialapp.classes.database

import androidx.lifecycle.LiveData

class SocialAppRepository(private val socialAppDao: SocialAppDao) {

    fun getAllUsers(): LiveData<List<User>>? {
        return socialAppDao.getAllUser()
    }

    fun getSpesificUser(userID: String): LiveData<User> {
        return socialAppDao.getSpesificUser(userID)
    }

    suspend fun insertUser(user: User) {
        socialAppDao.insertUser(user.userID, user.name, user.email, user.pictureURL)
    }

    suspend fun deleteUser(userID: String) {
        socialAppDao.deleteUser(userID)
    }

}