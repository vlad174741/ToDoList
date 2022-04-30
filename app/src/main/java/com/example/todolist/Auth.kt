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
import kotlinx.android.synthetic.main.authorization.*
import kotlinx.coroutines.DelicateCoroutinesApi

class Auth: AppCompatActivity() {
    private var login = "empty"
    private var pass = "empty"
    private var prefsAuth: SharedPreferences? = null
    private var prefsPass: SharedPreferences? = null
    private var prefsTheme: SharedPreferences? = null
    private var themeSet = 4
    private val theme = ChangeTheme()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization)

    }

    override fun onResume(){
        super.onResume()

        //Принятие значений переменных с экрана Option//
        intent.putExtra("classLogin", login)
        intent.putExtra("classPass", pass)

        intent.putExtra("classTheme", themeSet)

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!

        prefsAuth = getSharedPreferences("auth", Context.MODE_PRIVATE)
        login = prefsAuth?.getString("auth", "empty")!!

        prefsPass = getSharedPreferences("pass", Context.MODE_PRIVATE)
        pass = prefsPass?.getString("pass", "empty")!!
        authorization()
        theme.themeChange(themeSet,delegate)


    }
    @OptIn(DelicateCoroutinesApi::class)
    fun  authorization() {

        val main = Intent(this, MainActivity::class.java)
        val reg = Intent(this, RegistrationForm::class.java)


        val singInWithoutReg = findViewById<Button>(R.id.button_singIn_without_reg)
        val singIn = findViewById<Button>(R.id.button_singIn)
        val regAgain = findViewById<Button>(R.id.button_reg)
        val firstReg = findViewById<Button>(R.id.button_reg_first)

        val edLogin = findViewById<EditText>(R.id.editText_login)
        val edPass = findViewById<EditText>(R.id.editText_password)

        if(login == "empty" && pass == "empty"){
            edLogin.visibility=(View.GONE)
            edPass.visibility=(View.GONE)
            regAgain.visibility=(View.GONE)
            singIn.visibility=(View.GONE)
            singInWithoutReg.visibility=(View.VISIBLE)
            firstReg.visibility=(View.VISIBLE)
        }

        singInWithoutReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { startActivity(main); finish() })

        firstReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { startActivity(reg) })

        button_reg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {startActivity(reg) })

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
    }

}