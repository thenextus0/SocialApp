package com.thenextus.socialapp.classes.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Friends")
data class Friend(
    @PrimaryKey @ColumnInfo(name= "friendRowID") var friendRowID: String,
    @ColumnInfo(name= "friendUserID") var friendUserID: String,
    @ColumnInfo(name= "apiUserID") var apiUserID: String
    )
