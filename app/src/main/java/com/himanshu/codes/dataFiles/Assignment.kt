package com.himanshu.codes.dataFiles

import java.io.Serializable

class Assignment(private var Title:String, private val Subject: String, private val Deadline: String = "2022-04-17"): Serializable {
    //getters

    fun getAssignmentTitle():String{
        return Title
    }

    fun getAssignmentSubject():String{
        return Subject
    }

    fun getAssignmentDeadline():String{
        return Deadline
    }
}