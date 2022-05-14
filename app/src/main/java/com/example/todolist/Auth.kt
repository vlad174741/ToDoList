package com.example.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.authorization.*
import kotlinx.coroutines.DelicateCoroutinesApi

class Auth: AppCompatActivity() {
    private var login = "empty"
    private var pass = "empty"

    private var themeSet = 0
    private var authSet = 0
    private var authWithoutRegSet = 0
    private var authRememberMe = 0

    private var prefsAuth: SharedPreferences? = null
    private var prefsAuthRememberMe: SharedPreferences? = null
    private var prefsAuthWithoutReg: SharedPreferences? = null
    private var prefsPass: SharedPreferences? = null
    private var prefsTheme: SharedPreferences? = null
    private var prefsLogin: SharedPreferences? = null

    private val save = SaveData()
    private val theme = ChangeTheme()






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization)

    }

    override fun onResume(){
        super.onResume()

        //Принятие значений переменных с экрана Option//

        prefsAuthWithoutReg = getSharedPreferences("settingsAuthWithoutReg", Context.MODE_PRIVATE)
        authWithoutRegSet = prefsAuthWithoutReg?.getInt("settingsAuthWithoutReg", 0)!!

        prefsAuthRememberMe = getSharedPreferences("settingsAuthRemMe", Context.MODE_PRIVATE)
        authRememberMe = prefsAuthRememberMe?.getInt("settingsAuthRemMe", 0)!!

        prefsAuth = getSharedPreferences("settingsAuth", Context.MODE_PRIVATE)
        authSet = prefsAuth?.getInt("settingsAuth", 0)!!

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!

        prefsLogin = getSharedPreferences("auth", Context.MODE_PRIVATE)
        login = prefsLogin?.getString("auth", "empty")!!

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
        val rememberMeCheck = findViewById<CheckBox>(R.id.remMe)

        val edLogin = findViewById<EditText>(R.id.editText_login)
        val edPass = findViewById<EditText>(R.id.editText_password)

        if (authWithoutRegSet==1){startActivity(main); finish()}


        if(authRememberMe==1){edLogin.setText(login)}
        rememberMeCheck.isChecked = authRememberMe==1 /*if(authSet==1){remMe.isChecked=true}else{remMe.isChecked=false}*/


        if(login == "empty" && pass == "empty"){
            edLogin.visibility=(View.GONE)
            edPass.visibility=(View.GONE)
            regAgain.visibility=(View.GONE)
            singIn.visibility=(View.GONE)
            rememberMeCheck.visibility=(View.GONE)
            singInWithoutReg.visibility=(View.VISIBLE)
            firstReg.visibility=(View.VISIBLE)
        }




        singInWithoutReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { save.saveDataInt(1,prefsAuthWithoutReg,"settingsAuthWithoutReg"); onResume()})

        firstReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { startActivity(reg) })

        button_reg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {startActivity(reg) })

        button_singIn.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {
            if (edLogin.text.isNotEmpty()){

                if (edPass.text.isNotEmpty()) {

                    if (edLogin.text.toString() == login && edPass.text.toString() == pass) {

                        authRememberMe = if (rememberMeCheck.isChecked){ 1 } else{ 0 } /*if (rememberMeCheck.isChecked){authRememberMe=1} else{authRememberMe=0}*/
                        save.saveDataInt(authRememberMe,prefsAuthRememberMe,"settingsAuthRemMe")
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