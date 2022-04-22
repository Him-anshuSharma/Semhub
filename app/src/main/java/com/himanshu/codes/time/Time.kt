package com.himanshu.codes.time

class Time(private val classTime :String,
           private val className:String, private val subCode:String) {

    //getters
    fun getClassTime():String{
        return classTime
    }
    fun getClassName():String{
        return className
    }
    fun getSubCode():String{
        return subCode
    }
}