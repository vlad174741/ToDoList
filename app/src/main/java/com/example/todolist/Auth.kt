package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.authorization.*
import kotlinx.coroutines.DelicateCoroutinesApi

class Auth: AppCompatActivity() {
    private var login = "empty"
    private var pass = "empty"
    private var prefsAuth: SharedPreferences? = null
    private var prefsPass: SharedPreferences? = null
    private var prefsTheme: SharedPreferences? = null
    private var themeSet = 4
    val save = SaveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization)


        //Сохранение настроек темы//
        intent.putExtra("classTheme", themeSet)

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!

        prefsAuth = getSharedPreferences("auth", Context.MODE_PRIVATE)
        login = prefsAuth?.getString("auth", "empty")!!

        prefsPass = getSharedPreferences("pass", Context.MODE_PRIVATE)
        pass = prefsPass?.getString("pass", "empty")!!
        authorization()
        themeChange()


    }
    @OptIn(DelicateCoroutinesApi::class)
    fun  authorization() {
        val edLogin = findViewById<EditText>(R.id.editText_login)
        val edPass = findViewById<EditText>(R.id.editText_password)
        val main = Intent(this, MainActivity::class.java)

        button_singIn.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {

            if (edLogin.text.isNotEmpty()){

                if (edPass.text.isNotEmpty()) {

                    if (edLogin.text.toString() == login && edPass.text.toString() == pass) {
                        startActivity(main)
                        finish()
                    } else {
                        Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Введите логин", Toast.LENGTH_SHORT).show()
            }




        })

        button_reg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {
            login = edLogin.text.toString()
            save.saveDataString(login,prefsAuth,"auth")
            pass = edPass.text.toString()
            save.saveDataString(pass,prefsPass,"pass")



        })

    }

    private fun themeChange() {

        if (themeSet == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            delegate.applyDayNight()

        }
        if (themeSet == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        }
        if (themeSet == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
    }



}