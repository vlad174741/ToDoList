package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.auth_pin_form.*
import kotlinx.coroutines.DelicateCoroutinesApi

class AuthPinCode: AppCompatActivity() {

    private var pin = ""
    private var prefPIN: SharedPreferences? = null

    private var authWithPINSet = 0
    private var prefsAuthWithPIN: SharedPreferences? = null

    private val save= SaveData()
    private val toastText = ToastText()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_pin_form)

        prefPIN = getSharedPreferences("PIN", Context.MODE_PRIVATE)
        pin = prefPIN?.getString("PIN","")!!

        prefsAuthWithPIN = getSharedPreferences("settingsAuthPIN", Context.MODE_PRIVATE)
        authWithPINSet = prefsAuthWithPIN?.getInt("settingsAuthPIN",0)!!

        authorizationPIN()


    }





    @OptIn(DelicateCoroutinesApi::class)
    fun authorizationPIN(){


        val main = Intent(this, MainActivity::class.java)
        val auth = Intent(this, Auth::class.java)

        textView8.text=pin


        fun saveDataAuthPIN(res:Int) { save.saveDataInt(res, prefsAuthWithPIN, "settingsAuthPIN") }

        fun saveDataPIN(res:String) { save.saveDataString(res, prefPIN, "PIN") }
        

        if (authWithPINSet==2){saveDataAuthPIN(1)}

        val buttonNumber = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val buttonNumber2 = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val buttonNumber3 = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        val buttonNumber4 = findViewById<FloatingActionButton>(R.id.floatingActionButton4)
        val buttonNumber5 = findViewById<FloatingActionButton>(R.id.floatingActionButton5)
        val buttonNumber6 = findViewById<FloatingActionButton>(R.id.floatingActionButton6)
        val buttonNumber7 = findViewById<FloatingActionButton>(R.id.floatingActionButton7)
        val buttonNumber8 = findViewById<FloatingActionButton>(R.id.floatingActionButton8)
        val buttonNumber9 = findViewById<FloatingActionButton>(R.id.floatingActionButton9)
        val buttonNumber0 = findViewById<FloatingActionButton>(R.id.floatingActionButton0)
        val buttonNumberOK = findViewById<FloatingActionButton>(R.id.floatingActionButtonOk)
        val buttonNumberNO = findViewById<FloatingActionButton>(R.id.floatingActionButtonNo)
        val textViewPIN = findViewById<TextView>(R.id.textViewPin)

        fun buttonClickable(){ buttonNumberOK.isClickable=false; buttonNumberNO.isClickable=false}


        buttonNumber.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("1") })

        buttonNumber2.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("2")})

        buttonNumber3.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("3")})

        buttonNumber4.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("4")})

        buttonNumber5.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("5")})

        buttonNumber6.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("6")})

        buttonNumber7.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("7")})

        buttonNumber8.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("8")})

        buttonNumber9.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("9")})

        buttonNumber0.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {textViewPIN.append("0")})

        buttonNumberNO.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {
            if (textViewPIN.text.isNotEmpty()){
                val back = textViewPIN.text.toString()
                textViewPIN.text = back.substring(0, back.length - 1)
            }else{textViewPIN.append("")}
        })

        buttonNumberOK.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {

            if (authWithPINSet==2){

                if (textViewPIN.text.length == 4)
                {   buttonClickable()
                    pin = textViewPIN.text.toString()
                    saveDataPIN(pin)
                    finish()
                    startActivity(auth)
                }
                else { toastText.toastTextShort("Введите 4 символа",this) }

            }else {

                if (pin == "") {
                    if (textViewPIN.text.length == 4) {
                        buttonClickable()
                        pin = textViewPIN.text.toString()
                        saveDataAuthPIN(1)
                        saveDataPIN(pin)
                        finish()
                    } else { toastText.toastTextShort("Введите 4 символа",this)  }

                } else {

                    if (textViewPIN.text.length == 4) {
                        if (textViewPIN.text.toString() == pin) {
                            buttonClickable()
                            saveDataAuthPIN(1)
                            startActivity(main)
                            finishAffinity()
                        } else { toastText.toastTextShort("Неверный ПИН код",this) }

                    }else{toastText.toastTextShort("Введите 4 символа",this) }
                }
            }
        })




    }

    override fun onDestroy() { super.onDestroy(); finish() }






}