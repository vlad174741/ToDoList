package com.example.todolist

import android.annotation.SuppressLint
import dataBase.DbManagerClass
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.MainActivityBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val dbManager = DbManagerClass(this)
    val myAdapter = MyAdapterRC(ArrayList(), this)
    private val save = SaveData()

    //Пременные для тегов//
    private val tag = Tags()
    private var clear = 0

    //Пременные для удаления заметок//
    var del = false
    var con = 1
    var timer: Timer? = null
    var run: Boolean = false
    var many: Boolean = false

    //Перемнные для сохранения параметров настроек//
    private var themeSet = 4
    private var deleteSet = 1
    private var authWithoutRegSet = 0
    private var authWithPINSet = 0

    private var prefsAuthWithPIN: SharedPreferences? = null
    private var prefsTheme: SharedPreferences? = null
    private var prefsDelete: SharedPreferences? = null
    private var prefsAuthWithoutReg: SharedPreferences? = null


    private val handler = Handler(Looper.getMainLooper())

    lateinit var binding:MainActivityBinding

    private val theme = ChangeTheme()





    override fun onCreate(savedInstanceState: Bundle?) {

        binding = MainActivityBinding.inflate(LayoutInflater.from(this))

        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.navViewMenu.setNavigationItemSelectedListener(this)


        //Сохранение настроек темы//
        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!
        //Сохранение настроек удаления//
        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        deleteSet = prefsDelete?.getInt("settingsDelete", 0)!!

        prefsAuthWithPIN = getSharedPreferences("settingsAuthPIN", Context.MODE_PRIVATE)
        authWithPINSet = prefsAuthWithPIN?.getInt("settingsAuthPIN",0)!!

        prefsAuthWithoutReg = getSharedPreferences("settingsAuthWithoutReg", Context.MODE_PRIVATE)
        authWithoutRegSet = prefsAuthWithoutReg?.getInt("settingsAuthWithoutReg", 0)!!


    }

    //Обновление//
    override fun onResume() {

        super.onResume()
        dbManager.openDb()
        init()
        searchView()
        clearSearchView()
        chekItem(""); chekItemTag()
        checkDelete()
        tagClick()


    }

    //Меню//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val option = Intent(this, Option::class.java)
        val reg = Intent(this, RegistrationForm::class.java)
        val logout = Intent(this, Auth::class.java)

        when (item.itemId) {
            R.id.id_menu_settings -> {
                startActivity(option)
                binding.drawerLayout.close()



            }

            R.id.id_menu_reg -> {
                save.saveDataInt(0,prefsAuthWithoutReg,"settingsAuthWithoutReg")
                finish()
                startActivity(reg)
            }

            R.id.id_menu_logOut -> {
                save.saveDataInt(0,prefsAuthWithoutReg,"settingsAuthWithoutReg")
                save.saveDataInt(0,prefsAuthWithPIN,"settingsAuthPIN")
                finish()
                startActivity(logout)
            }
        }
        return true

    }

    //Функция для кнопки создания заметок//
    fun onClickNew(@Suppress("UNUSED_PARAMETER") view: View) {
        val editActivity = Intent(this, EditActivity::class.java)

        binding.apply {
            addButton.isClickable = false
        }
        startActivity(editActivity)
    }

    //RecyclerView//
    private fun init()= with(binding) {
        rcView.adapter = myAdapter
        val swapHelper = getSwap()
        swapHelper.attachToRecyclerView(rcView)
        rcView.setHasFixedSize(true)

        addButton.isClickable = true
        binding.navViewMenu.checkedItem?.setChecked(false)





    }
    private fun rcViewUp(){ binding.rcView.scrollBy(-1,-900000)}

    //Поиск//
    private fun clearSearchView() = with(binding)
    {
        searchViewMain.clearFocus()
        searchViewMain.setQuery("", false)
        searchViewMain.isIconified = true
    }

    private fun searchView()= with(binding) {

        if(myAdapter.itemCount==0){clearSearchView()}
        searchViewMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(text: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                binding.rdTagActivity.clearCheck(); clearButtonGone()
                chekItem(text!!)

                return true

            }
        })
    }

    //Функция для проверки элементов для поиска//
    private fun chekItem(text: String) = with(binding) {


        val list = dbManager.readDbData(text)


        myAdapter.updateAdapter(list)
        if (list.size > 0) { tvNoElements.visibility = View.GONE }
        else { tvNoElements.visibility = View.VISIBLE }




    }

    //Свайпы и удаление//

    //Функция для проверки настройки удаления//
    private fun checkDelete() {
        if (deleteSet == 0) { many = true;del = true } else { many = false }
        if(!many){binding.buttonDelCancel.visibility = (View.GONE)}
    }

    //Функция для кнопки удаления ДА//
    fun deleteYes(@Suppress("UNUSED_PARAMETER") view: View) {
        del = true; con = 999; yesOrNoGone()
    }

    //Функция для кнопки удаления НЕТ//
    @SuppressLint("NotifyDataSetChanged")
    fun deleteNo(@Suppress("UNUSED_PARAMETER") view: View) {
        myAdapter.notifyDataSetChanged()
        del = false; con = 999; yesOrNoGone()
    }

    //Функция для кнопки удаления УДАЛИТ НЕСКОЛЬКО//
    fun deleteMany(@Suppress("UNUSED_PARAMETER") view: View) {
        many = true; del = true; con = 999;binding.buttonDelCancel.visibility =
            (View.VISIBLE);binding.addButton.visibility = (View.GONE)
        yesOrNoGone()
    }

    //Функция для кнопки отменение удаления УДАЛИТ НЕСКОЛЬКО//
    fun manyDelCancel(@Suppress("UNUSED_PARAMETER") view: View) { many = false; del = false
        binding.buttonDelCancel.visibility = (View.GONE)
        binding.addButton.visibility = (View.VISIBLE) }
    //Функция для вызова контекстного окна удаления//
    fun yesOrNoVisible() { binding.backgroundDeleteWindow.visibility = (View.VISIBLE)
        binding.deleteWindow.visibility = (View.VISIBLE) }

    //Функция для отмены вызова контекстного окна удаления//
    private fun yesOrNoGone() { binding.backgroundDeleteWindow.visibility = (View.GONE)
        binding.deleteWindow.visibility = (View.GONE) }

    //Функция для свапа заметки(вправо или влево)//



    private fun getSwap(): ItemTouchHelper {

        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback
            (0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
        {
            override fun onMove(recyclerView: RecyclerView, viewHolder:
            RecyclerView.ViewHolder, target: RecyclerView.ViewHolder):Boolean
            { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                run = true

                //Функция для удаления заметки//
                fun del() {

                    if (del)
                    { myAdapter.removeItem(viewHolder.adapterPosition, dbManager)
                        searchView()
                        run = false; timer?.cancel()

                        if(myAdapter.itemCount==0) { binding.tvNoElements.visibility = View.VISIBLE }
                        else { binding.tvNoElements.visibility = View.GONE }}

                    else { searchView(); del = false; run = false; timer?.cancel() } }

                fun startStop() {
                    timer = Timer(); timer?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                con++
                                if (con == 1000) del()
                                if (con == 998) con = 0
                                if (con == 1000) con = 0
                            }
                        }

                    }, 0, 1000)

                }

                if (many) { del() } else { yesOrNoVisible();startStop() }

            }
        })

    }


    //Теги//

    //Обработчики нажатий на кнопки с тегами//
    private fun tagClick() = with(binding) {


        fun tagButton(button: RadioButton){
            button.isClickable=false; clear = 0; chekItemTag();clearSearchView()
        }


        checkTagHome.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {  tagButton(checkTagHome) })

        checkTagShop.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { tagButton(checkTagShop) })

        checkTagWork.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { tagButton(checkTagWork) })

        checkTagWeekend.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { tagButton(checkTagWeekend) })

        checkTagBank.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { tagButton(checkTagBank) })

        checkTagSport.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { tagButton(checkTagSport) })


        fun clearButton(button: ImageButton, clearID:Int){
            button.isClickable=false; clear = clearID; chekItemTag(); binding.rdTagActivity.clearCheck()
            button.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        }


        clearHome.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearHome,1)
        })

        clearShop.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearShop,2)
        })

        clearWork.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearWork,3)
        })

        clearWeekend.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearWeekend,4)
        })

        clearBank.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearBank,5)
        })

        clearSport.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clearButton(clearSport,6)
        })
    }


    fun clearButtonGone() = with(binding)
    {
        clearHome.visibility = (View.GONE); checkTagHome.isClickable=true
        clearShop.visibility = (View.GONE); checkTagShop.isClickable=true
        clearWork.visibility = (View.GONE); checkTagWork.isClickable=true
        clearWeekend.visibility = (View.GONE); checkTagWeekend.isClickable=true
        clearBank.visibility = (View.GONE); checkTagBank.isClickable=true
        clearSport.visibility = (View.GONE); checkTagSport.isClickable=true
    }


    //Функция для проверки и выбора тега, а так же выбора заметок принадлежащих этому тегу//
    private fun chekItemTag() = with(binding) {

        var textTag = ""


        fun chekTagIf(tags: String, buttonImage: ImageButton){
            textTag = tags; buttonImage.visibility = (View.VISIBLE)
        }

        fun chekTagElse(buttonImage: ImageButton, buttonRadio: RadioButton){
            buttonImage.visibility = (View.GONE); buttonRadio.isClickable=true
        }

        binding.apply {
            if (checkTagHome.isChecked) { chekTagIf(tag.homeTag,clearHome) }
            else { chekTagElse(clearHome,checkTagHome) }

            if (checkTagShop.isChecked) { chekTagIf(tag.shopTag,clearShop) }
            else { chekTagElse(clearShop,checkTagShop) }

            if (checkTagWork.isChecked) { chekTagIf(tag.workTag,clearWork) }
            else { chekTagElse(clearWork,checkTagWork) }

            if (checkTagWeekend.isChecked) { chekTagIf(tag.weekendTag,clearWeekend) }
            else { chekTagElse(clearWeekend,checkTagWeekend) }

            if (checkTagBank.isChecked) { chekTagIf(tag.bankTag,clearBank) }
            else { chekTagElse(clearBank,checkTagBank) }

            if (checkTagSport.isChecked) { chekTagIf(tag.sportTag,clearSport) }
            else { chekTagElse(clearSport,checkTagSport) }
        }

        fun clearTagIf( buttonRadio: RadioButton){ textTag = ""; buttonRadio.isClickable=true }

        fun clearTagElse(buttonImage: ImageButton){ buttonImage.isClickable=true }

        if (clear == 1) { clearTagIf(checkTagHome) } else {clearTagElse(clearHome)}

        if (clear == 2) { clearTagIf(checkTagShop) } else {clearTagElse(clearShop)}

        if (clear == 3) { clearTagIf(checkTagWork) } else {clearTagElse(clearWork)}

        if (clear == 4) { clearTagIf(checkTagWeekend) } else {clearTagElse(clearWeekend)}

        if (clear == 5) {clearTagIf(checkTagBank) } else {clearTagElse(clearBank)}

        if (clear == 6) { clearTagIf(checkTagSport) } else {clearTagElse(clearSport)}


        val list = dbManager.readDbDataTag(textTag)
        myAdapter.updateAdapter(list)
        if (list.size > 0) { tvNoElements.visibility = View.GONE }
        else { tvNoElements.visibility = View.VISIBLE }


    }



    //Закрытие окна//
    override fun onDestroy() { super.onDestroy() ; dbManager.closeDB() }

}



/*//плавная прокрутка//
    fun RecyclerView?.perfectScroll(size: Int,up:Boolean = true ,smooth: Boolean = true) {
        this?.apply {
            if (size > 0) {
                if (smooth) {
                    val minDirectScroll = 10 // left item to scroll
                    //smooth scroll
                    if (size > minDirectScroll) {
                        //scroll directly to certain position
                        val newSize = if (up) minDirectScroll else size - minDirectScroll
                        //scroll to new position
                        val newPos = newSize  - 1
                        //direct scroll
                        scrollToPosition(newPos)
                        //smooth scroll to rest
                        perfectScroll(minDirectScroll, true)

                    } else {
                        //direct smooth scroll
                        smoothScrollToPosition(if (up) 0 else size-1)
                    }
                } else {
                    //direct scroll
                    scrollToPosition(if (up) 0 else size-1)
                }
            }
        } }*/