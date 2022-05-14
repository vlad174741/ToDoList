package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistrationForm: AppCompatActivity() {

    private var login = "empty"
    private var pass = "empty"
    private var prefsAuth: SharedPreferences? = null
    private var prefsPass: SharedPreferences? = null
    private var prefsTheme: SharedPreferences? = null
    private var themeSet = 4
    private val save = SaveData()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_form)

        login= intent.getStringExtra("classLogin").toString()
        pass= intent.getStringExtra("classPass").toString()



        //Сохранение настроек темы//
        intent.putExtra("classTheme", themeSet)

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!

        prefsAuth = getSharedPreferences("auth", Context.MODE_PRIVATE)
        login = prefsAuth?.getString("auth", "empty")!!

        prefsPass = getSharedPreferences("pass", Context.MODE_PRIVATE)
        pass = prefsPass?.getString("pass", "empty")!!
        registration()



    }

    fun  registration() {

        val authClass = Intent(this,Auth::class.java)
        val buttonReg = findViewById<Button>(R.id.button_reg_first_form)

        val edLoginFirsForm = findViewById<EditText>(R.id.editText_login_first_reg_form)
        val edPassFirstForm = findViewById<EditText>(R.id.editText_password_first_reg_form)

        val edLoginOldForm = findViewById<EditText>(R.id.editText_login_old_reg_form)
        val edPassOldForm = findViewById<EditText>(R.id.editText_password_old_reg_form)

        fun saveLoginAndPass(){
            login = edLoginFirsForm.text.toString()
            save.saveDataString(login, prefsAuth, "auth")
            pass = edPassFirstForm.text.toString()
            save.saveDataString(pass, prefsPass, "pass")

            finishAffinity()
            startActivity(authClass)

        }

        if(login == "empty" && pass == "empty"){
            edLoginOldForm.visibility=(View.GONE)
            edPassOldForm.visibility=(View.GONE)

        }


        buttonReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {
            if(login == "empty" && pass == "empty"){

                if (edLoginFirsForm.text.isNotEmpty()) {
                    if (edPassFirstForm.text.isNotEmpty()) {
                        saveLoginAndPass()
                    }
                    else{ Toast.makeText(this, "Введите новый пароль", Toast.LENGTH_SHORT).show() }

                }else{ Toast.makeText(this, "Введите новый логин", Toast.LENGTH_SHORT).show() }

            }else{

                if (edLoginOldForm.text.isNotEmpty()){
                    if (edPassOldForm.text.isNotEmpty()){

                        if (edLoginFirsForm.text.isNotEmpty()) {
                            if (edPassFirstForm.text.isNotEmpty()) {

                                if(edLoginOldForm.text.toString() == login && edPassOldForm.text.toString() == pass) {
                                    saveLoginAndPass()
                                }
                                else{ Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show() }

                            }else{ Toast.makeText(this, "Введите новый пароль", Toast.LENGTH_SHORT).show() }

                        }else{ Toast.makeText(this, "Введите новый логин", Toast.LENGTH_SHORT).show() }

                    }else{ Toast.makeText(this, "Введите старый пароль", Toast.LENGTH_SHORT).show() }

                }else { Toast.makeText(this, "Введите старый логин", Toast.LENGTH_SHORT).show() }

            }
        })
    }


}
