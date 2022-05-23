package com.example.todolist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todolist.databinding.AuthorizationFormBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi

class Auth: AppCompatActivity() {


    private var themeSet = 0
    private var prefsTheme: SharedPreferences? = null

    private var authWithPINSet = 0
    private var prefsAuthWithPIN: SharedPreferences? = null

    private var authWithoutRegSet = 0
    private var prefsAuthWithoutReg: SharedPreferences? = null

    private var authRememberMe = 0
    private var prefsAuthRememberMe: SharedPreferences? = null

    private var pass = "empty"
    private var prefsPass: SharedPreferences? = null

    private var login = "empty"
    private var prefsLogin: SharedPreferences? = null

    private val save = SaveData()
    private val theme = ChangeTheme()
    private val toastText = ToastText()


    private var pin = ""
    private var prefPIN: SharedPreferences? = null


    private var delayForFinish = false
    private val handler = Handler(Looper.getMainLooper())

    lateinit var binding: AuthorizationFormBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AuthorizationFormBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_form)
    }

    override fun onResume() {
        super.onResume()

        //Принятие значений переменных с экрана Option//

        prefsAuthWithoutReg = getSharedPreferences("settingsAuthWithoutReg", Context.MODE_PRIVATE)
        authWithoutRegSet = prefsAuthWithoutReg?.getInt("settingsAuthWithoutReg", 0)!!

        prefsAuthRememberMe = getSharedPreferences("settingsAuthRemMe", Context.MODE_PRIVATE)
        authRememberMe = prefsAuthRememberMe?.getInt("settingsAuthRemMe", 0)!!

        prefsAuthWithPIN = getSharedPreferences("settingsAuthPIN", Context.MODE_PRIVATE)
        authWithPINSet = prefsAuthWithPIN?.getInt("settingsAuthPIN", 0)!!

        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!

        prefsLogin = getSharedPreferences("auth", Context.MODE_PRIVATE)
        login = prefsLogin?.getString("auth", "empty")!!

        prefsPass = getSharedPreferences("pass", Context.MODE_PRIVATE)
        pass = prefsPass?.getString("pass", "empty")!!

        prefPIN = getSharedPreferences("PIN", Context.MODE_PRIVATE)
        pin = prefPIN?.getString("PIN","")!!

        theme.themeChange(themeSet,delegate)
        authorization()


    }





    @OptIn(DelicateCoroutinesApi::class)
    fun  authorization() {


        val main = Intent(this, MainActivity::class.java)
        val reg = Intent(this, RegistrationForm::class.java)
        val pin = Intent(this, AuthPinCode::class.java)



        val secondAuth = findViewById<ConstraintLayout>(R.id.second_auth)
        val firstAuth = findViewById<ConstraintLayout>(R.id.first_auth)
        val pinAuth = findViewById<ConstraintLayout>(R.id.pin_auth)

        val singInWithoutReg = findViewById<Button>(R.id.button_singIn_without_reg)
        val singIn = findViewById<Button>(R.id.button_singIn)
        val regAgain = findViewById<Button>(R.id.button_reg)
        val firstReg = findViewById<Button>(R.id.button_reg_first)
        val firstRegPin = findViewById<Button>(R.id.button_reg_PIN)
        val rememberMeCheck = findViewById<CheckBox>(R.id.remMe)

        val edLogin = findViewById<EditText>(R.id.editText_login)
        val edPass = findViewById<EditText>(R.id.editText_password)



        if (authWithPINSet > 0){
            firstAuth.visibility=View.GONE; pinAuth.visibility = View.VISIBLE
            authorizationPIN()
        }else {

            if (login == "empty" && pass == "empty") {

                if (authWithoutRegSet == 1) {
                    finish(); startActivity(main)
                }


                singInWithoutReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
                {
                    save.saveDataInt(
                        1,
                        prefsAuthWithoutReg,
                        "settingsAuthWithoutReg"
                    ); onResume()
                })

                firstReg.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
                { startActivity(reg) })

                firstRegPin.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
                { startActivity(pin) })
            } else {
                firstAuth.visibility = View.GONE
                secondAuth.visibility = View.VISIBLE

                if (authRememberMe == 1) { edLogin.setText(login) }

                rememberMeCheck.isChecked = authRememberMe == 1 /*if(authSet==1){remMe.isChecked=true}else{remMe.isChecked=false}*/

                regAgain.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
                { startActivity(reg) })

                singIn.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
                {
                    if (edLogin.text.isNotEmpty()) {

                        if (edPass.text.isNotEmpty()) {

                            if (edLogin.text.toString() == login && edPass.text.toString() == pass) {

                                authRememberMe = if (rememberMeCheck.isChecked) {
                                    1
                                } else {
                                    0
                                } /*if (rememberMeCheck.isChecked){authRememberMe=1} else{authRememberMe=0}*/
                                save.saveDataInt(
                                    authRememberMe,
                                    prefsAuthRememberMe,
                                    "settingsAuthRemMe"
                                )
                                startActivity(main)
                                finish()
                            } else {
                                toastText.toastTextShort("Неверный логин или пароль",this)
                            }
                        } else {
                            toastText.toastTextShort("Введите пароль",this)
                        }
                    } else {
                        toastText.toastTextShort("Введите логин",this)
                    }

                })
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun  authorizationPIN(){



        val main = Intent(this, MainActivity::class.java)
        val auth = Intent(this, Auth::class.java)

        val testTextPin = findViewById<TextView>(R.id.textView8)


        testTextPin.text=pin


        fun saveDataAuthPIN(res:Int) { save.saveDataInt(res, prefsAuthWithPIN, "settingsAuthPIN") }

        fun saveDataPIN(res:String) { save.saveDataString(res, prefPIN, "PIN") }

        fun toastText(res:String) { Toast.makeText(this, res, Toast.LENGTH_SHORT).show() }


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
                else { toastText("Введите 4 символа") }

            }else {

                if (pin == "") {
                    if (textViewPIN.text.length == 4) {
                        buttonClickable()
                        pin = textViewPIN.text.toString()
                        saveDataAuthPIN(1)
                        saveDataPIN(pin)
                        finish()
                    } else { toastText("Введите 4 символа") }

                } else {

                    if (textViewPIN.text.length == 4) {
                        if (textViewPIN.text.toString() == pin) {
                            buttonClickable()
                            saveDataAuthPIN(1)
                            startActivity(main)
                            finishAffinity()
                        } else { toastText("Неверный ПИН код") }

                    }else{toastText("Введите 4 символа")}
                }
            }
        })




    }

    override fun onDestroy() {
        super.onDestroy()
        if (themeSet>0){handler.postDelayed({delayForFinish=true},0)}
        else{finishAffinity()}
        if (delayForFinish==true){finishAffinity()}}
}