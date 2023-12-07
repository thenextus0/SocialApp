package com.thenextus.socialapp.classes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey @ColumnInfo(name= "userID") var userID: String,
    @ColumnInfo(name= "name") var name: String,
    @ColumnInfo(name= "email") var email: String,
    @ColumnInfo(name= "pictureURL") var pictureURL: String,

    )