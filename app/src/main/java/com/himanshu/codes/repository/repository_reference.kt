package com.himanshu.codes.repository

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class repository_reference {
    companion object {

        fun getReference(): FirebaseDatabase {

            return Firebase.database
        }
    }
}