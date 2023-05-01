package com.himanshu.codes.dataFiles

class UserDetails{

    companion object {
        var UID: String? = null
        var username: String? = null
        fun getUid(): String? {
            return UID
        }
        fun getName():String?{
            return username
        }
        fun setUid(uid:String){
            UID = uid
        }
        fun setUserName(name:String){
            username = name
        }
    }
}