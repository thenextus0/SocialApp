package com.thenextus.socialapp.classes.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Friends")
data class Friend(
    @PrimaryKey @ColumnInfo(name= "friendRowID") var friendID: String,
    @ColumnInfo(name= "friendUserID") var userID: String,
    @ColumnInfo(name= "apiUserID") var userFriendID: String
)
