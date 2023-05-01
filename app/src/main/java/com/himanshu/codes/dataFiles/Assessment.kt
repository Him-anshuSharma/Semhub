package com.himanshu.codes.dataFiles

import java.io.Serializable

class Assessment(
    private var Title: String,
    private val Subject: String,
    private val Deadline: String = "YYYY-MM-DD",
    val id:String = "null"
) : Serializable {
    //getters

    fun getAssessmentTitle(): String {
        return Title
    }

    fun getAssessmentSubject(): String {
        return Subject
    }

    fun getAssessmentDeadline(): String {
        return Deadline
    }
}