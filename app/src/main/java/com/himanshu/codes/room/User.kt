package com.himanshu.codes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var userEmailID: String,
    @ColumnInfo(name = "UID") var userID: String?
)
