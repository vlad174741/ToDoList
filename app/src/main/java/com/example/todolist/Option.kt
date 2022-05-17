package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.delete_activity_option.*
import kotlinx.android.synthetic.main.login_pin_activity_option.*
import kotlinx.android.synthetic.main.theme_activity_option.*
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class Option : AppCompatActivity() {

    private var themeSet = 0
    private var deleteSet  = 1
    private var authPINSet  = 1
    private var prefsTheme: SharedPreferences?=null
    private var prefsDelete: SharedPreferences?=null
    private var prefsAuthPIN: SharedPreferences?=null
    private val save = SaveData()

    var pin = ""
    var prefPIN: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        val authClass = Intent(this,Auth::class.java)
        val main = Intent(this, MainActivity::class.java)

        //Сохранение настроек//
        fun saveTheme(res:Int){ save.saveDataInt(res,prefsTheme,"settingsTheme") }
        fun saveDelete(res:Int){ save.saveDataInt(res,prefsDelete,"settingsDelete") }
        fun saveAuthPIN(res:Int){ save.saveDataInt(res,prefsAuthPIN,"settingsAuthPIN") }

        fun savePIN(){ save.saveDataString("",prefPIN,"PIN") ; }

        prefsAuthPIN = getSharedPreferences("settingsAuthPIN", Context.MODE_PRIVATE)
        authPINSet=prefsAuthPIN?.getInt("settingsAuthPIN",0)!!

        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        deleteSet=prefsDelete?.getInt("settingsDelete",0)!!

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet=prefsTheme?.getInt("settingsTheme",0)!!

        prefPIN = getSharedPreferences("PIN", Context.MODE_PRIVATE)
        pin = prefPIN?.getString("PIN","")!!


        //Проверка выбранных настроек//
        ifElseCheck()

        button_option_PIN_no.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_PIN_no.isChecked){authPINSet=0; saveAuthPIN(0) }  })

        button_option_PIN_yes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_PIN_yes.isChecked){authPINSet=1; saveAuthPIN(1) }  })

        button_option_PIN_change.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_PIN_change.isChecked){saveAuthPIN(2) ; finishAffinity();  startActivity(authClass) }  })



        //Кнопка выхода из настройки//
        imageButton.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { startActivity(main);finishAffinity()})

        //Настройки темы//
        button_system_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_system_theme.isChecked){themeSet=0; saveTheme(0); themeChange()} })
        button_dark_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_dark_theme.isChecked){ themeSet=1; saveTheme(1); themeChange()} })
        button_light_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_light_theme.isChecked){themeSet=2; saveTheme(2); themeChange()} })

        //Настройки удаление//
        button_option_delete_yes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_delete_yes.isChecked){deleteSet=1; saveDelete(1) }  })
        button_option_delete_no.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { if (button_option_delete_no.isChecked){deleteSet=0; saveDelete(0) }  })
    }

    //Функция для изменения темы//
    private fun themeChange(){ val theme = ChangeTheme();theme.themeChange(themeSet,delegate)}

    //Функция для проверки выбранных настроек//
    private fun ifElseCheck(){
        //Проверка темы//
        if (themeSet==0){ button_system_theme.isChecked=true }
        if (themeSet==1){ button_dark_theme.isChecked=true}
        if (themeSet==2){ button_light_theme.isChecked=true}
        //Проверка удаления//
        if (deleteSet==0){button_option_delete_no.isChecked=true}
        if (deleteSet==1){button_option_delete_yes.isChecked=true}

        if (authPINSet==1){button_option_PIN_yes.isChecked=true}
        if (authPINSet==0){button_option_PIN_no.isChecked=true}
    }
}


