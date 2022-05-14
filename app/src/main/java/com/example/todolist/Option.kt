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
    private var authSet  = 1
    private var prefsTheme: SharedPreferences?=null
    private var prefsDelete: SharedPreferences?=null
    private var prefsAuth: SharedPreferences?=null
    private val save = SaveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        //Сохранение настроек//
        fun saveTheme(res:Int){ save.saveDataInt(res,prefsTheme,"settingsTheme") }
        fun saveDelete(res:Int){ save.saveDataInt(res,prefsDelete,"settingsDelete") }
        fun saveAuth(res:Int){ save.saveDataInt(res,prefsAuth,"settingsAuth") }

        prefsAuth = getSharedPreferences("settingsAuth", Context.MODE_PRIVATE)
        authSet=prefsAuth?.getInt("settingsAuth",0)!!

        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        deleteSet=prefsDelete?.getInt("settingsDelete",0)!!

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet=prefsTheme?.getInt("settingsTheme",0)!!


        //Проверка выбранных настроек//
        ifElseCheck()

        button_option_inAuto_no.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_inAuto_no.isChecked){authSet=0; saveAuth(0) }  })

        button_option_inAuto_yes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_inAuto_yes.isChecked){authSet=1; saveAuth(1) }  })



        //Кнопка выхода из настройки//
        imageButton.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { val option = Intent(this, MainActivity::class.java); startActivity(option)
            saveTheme(themeSet);saveDelete(deleteSet) })

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

        if (authSet==1){button_option_inAuto_yes.isChecked=true}
        if (authSet==0){button_option_inAuto_no.isChecked=true}
    }
}


