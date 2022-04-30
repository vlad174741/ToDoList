package com.example.todolist

import androidx.appcompat.app.AppCompatDelegate

class ChangeTheme {

    fun themeChange(themeSet:Int, delegate: AppCompatDelegate) {

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