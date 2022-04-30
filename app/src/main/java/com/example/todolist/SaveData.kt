package com.example.todolist

import android.content.SharedPreferences

class SaveData {

    fun saveDataString(res:String,pref:SharedPreferences?=null,key:String){

        val editorAuth = pref?.edit()
        editorAuth?.putString(key, res)
        editorAuth?.apply()

    }

    fun saveDataInt(res:Int,pref:SharedPreferences?=null,key:String){

        val editorAuth = pref?.edit()
        editorAuth?.putInt(key, res)
        editorAuth?.apply()

    }
}