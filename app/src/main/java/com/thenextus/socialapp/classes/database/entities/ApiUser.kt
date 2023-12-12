package com.thenextus.socialapp.classes.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ApiUsers")
data class ApiUser(
    @PrimaryKey @ColumnInfo(name="userID") var userID: String,
    @ColumnInfo(name= "firstName") var firstName: String?,
    @ColumnInfo(name= "lastName") var lastName: String?,
    @ColumnInfo(name= "email") var email: String,
    @ColumnInfo(name= "pictureURL") var picture: String?
    )
