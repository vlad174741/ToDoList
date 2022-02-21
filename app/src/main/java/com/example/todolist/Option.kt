package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_option.*
import kotlinx.android.synthetic.main.delete_activity_option.*
import kotlinx.android.synthetic.main.theme_activity_option.*
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class Option : AppCompatActivity() {

    private var themeSet = 0
    private var deleteSet  = 1
    private var prefsTheme: SharedPreferences?=null
    private var prefsDelete: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        //Сохранение настроек//
        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        themeSet=prefsTheme?.getInt("settingsTheme",0)!!
        deleteSet=prefsDelete?.getInt("settingsDelete",0)!!

        //Предача настроек в главное Activity//
        intent.getIntExtra("classTheme", themeSet)
        intent.getIntExtra("classDelete", deleteSet)

        //Проверка выбранных настроек//
        ifElseCheck()
        //text()

        //Кнопка выхода из настройки//
        imageButton.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { val option = Intent(this, MainActivity::class.java); startActivity(option)
            saveData(themeSet);saveDataDelete(deleteSet) })

        //Настройки темы//
        button_system_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_system_theme.isChecked){themeSet=0; saveData(themeSet); themeChange()} })
        button_dark_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_dark_theme.isChecked){themeSet=1; saveData(themeSet); themeChange()} })
        button_light_theme.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_light_theme.isChecked){themeSet=2; saveData(themeSet); themeChange()} })

        //Настройки удаление//
        button_option_delete_yes.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        {if (button_option_delete_yes.isChecked){deleteSet=1;saveDataDelete(deleteSet) }  })
        button_option_delete_no.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener
        { if (button_option_delete_no.isChecked){deleteSet=0;saveDataDelete(deleteSet) }  })
    }

    //Функция для проверки значения переменных в настройках//
    fun text(){textView.text=themeSet.toString();textView8.text=deleteSet.toString()}

    //Функция для изменения темы//
    private fun themeChange(){

        if (themeSet==0){  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            delegate.applyDayNight()}
        if (themeSet==1){  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()}
        if (themeSet==2){  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()}
    }

    //Функция для сохранения переменной настройки темы//
    private fun saveData(resSave:Int){

        val editor = prefsTheme?.edit()
        editor?.putInt("settingsTheme",resSave)
        editor?.apply()
    }

    //Функция для сохранения переменной настройки удаления//
    private fun saveDataDelete(resDel:Int){

        val editorDelete = prefsDelete?.edit()
        editorDelete?.putInt("settingsDelete",resDel)
        editorDelete?.apply()
    }

    //Функция для проверки выбранных настроек//
    private fun ifElseCheck(){
        //Проверка темы//
        if (themeSet==0){ button_system_theme.isChecked=true }
        if (themeSet==1){ button_dark_theme.isChecked=true}
        if (themeSet==2){ button_light_theme.isChecked=true}
        //Проверка удаления//
        if (deleteSet==0){button_option_delete_no.isChecked=true}
        if (deleteSet==1){button_option_delete_yes.isChecked=true}
    }
}


