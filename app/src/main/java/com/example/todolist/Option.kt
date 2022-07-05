package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.example.todolist.databinding.ActivityOptionBinding
import dataBase.DbManagerClass
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class Option : AppCompatActivity() {


    private var themeSet = 0
    private var prefsTheme: SharedPreferences?=null

    private var deleteSet  = 1
    private var prefsDelete: SharedPreferences?=null

    private var authPINSet  = 1
    private var prefsAuthPIN: SharedPreferences?=null

    private val dbManager = DbManagerClass(this)
    private val save = SaveData()



    lateinit var binding: ActivityOptionBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val authClass = Intent(this,Auth::class.java)
        val main = Intent(this, MainActivity::class.java)

        //Сохранение настроек//
        fun saveTheme(res:Int){ save.saveDataInt(res,prefsTheme,"settingsTheme") }
        fun saveDelete(res:Int){ save.saveDataInt(res,prefsDelete,"settingsDelete") }
        fun saveAuthPIN(res:Int){ save.saveDataInt(res,prefsAuthPIN,"settingsAuthPIN") }


        prefsAuthPIN = getSharedPreferences("settingsAuthPIN", Context.MODE_PRIVATE)
        authPINSet=prefsAuthPIN?.getInt("settingsAuthPIN",0)!!

        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        deleteSet=prefsDelete?.getInt("settingsDelete",0)!!

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet=prefsTheme?.getInt("settingsTheme",0)!!


        //Проверка выбранных настроек//
        ifElseCheck()

        binding.buttonOptionPINNo.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonOptionPINNo.isChecked){authPINSet=0; saveAuthPIN(0) }  })

        binding.buttonOptionPINYes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonOptionPINYes.isChecked){authPINSet=1; saveAuthPIN(1) }  })

        binding.buttonOptionPINChange.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonOptionPINChange.isChecked){saveAuthPIN(2) ; finishAffinity();  startActivity(authClass) }  })



        //Кнопка выхода из настройки//
        binding.imageButton.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { startActivity(main);finish()})

        //Настройки темы//
        binding.buttonSystemTheme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonSystemTheme.isChecked){themeSet=0; saveTheme(0); themeChange()} })
        binding.buttonDarkTheme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonDarkTheme.isChecked){ themeSet=1; saveTheme(1); themeChange()} })
        binding.buttonLightTheme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonLightTheme.isChecked){themeSet=2; saveTheme(2); themeChange()} })

        //Настройки удаление//
        binding.buttonOptionDeleteYes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (binding.buttonOptionDeleteYes.isChecked){deleteSet=1; saveDelete(1) }  })
        binding.buttonOptionDeleteNo.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { if (binding.buttonOptionDeleteNo.isChecked){deleteSet=0; saveDelete(0) }  })
    }


    //Функция для изменения темы//
    private fun themeChange(){ val theme = ChangeTheme();theme.themeChange(themeSet,delegate)}

    //Функция для проверки выбранных настроек//
    private fun ifElseCheck(){
        //Проверка темы//
        if (themeSet==0){ binding.buttonSystemTheme.isChecked=true }
        if (themeSet==1){ binding.buttonDarkTheme.isChecked=true}
        if (themeSet==2){ binding.buttonLightTheme.isChecked=true}
        //Проверка удаления//
        if (deleteSet==0){binding.buttonOptionDeleteNo.isChecked=true}
        if (deleteSet==1){binding.buttonOptionDeleteYes.isChecked=true}

        if (authPINSet==1){binding.buttonOptionPINYes.isChecked=true}
        if (authPINSet==0){binding.buttonOptionPINNo.isChecked=true}
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDB()
    }

}


