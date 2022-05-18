package com.example.todolist

import android.content.Context
import android.widget.Toast

class ToastText {
    fun toastTextShort(res: String, context: Context) { Toast.makeText(context, res, Toast.LENGTH_SHORT).show() }
    fun toastTextLong(res: String, context: Context) { Toast.makeText(context, res, Toast.LENGTH_LONG).show() }
}