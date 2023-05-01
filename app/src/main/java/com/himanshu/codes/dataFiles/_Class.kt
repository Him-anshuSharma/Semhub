package com.himanshu.codes.dataFiles

class _Class(
    private val classTime: String,
    private val className: String, private val venue: String
) {

    //getters
    fun getClassTime(): String {
        return classTime
    }

    fun getClassName(): String {
        return className
    }

    fun getVenue(): String {
        return venue
    }
}