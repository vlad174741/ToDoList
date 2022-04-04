package com.example.todolist

import android.annotation.SuppressLint
import dataBase.DbManagerClass
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.authorization.*
import kotlinx.android.synthetic.main.main_menu_drawer.*
import kotlinx.android.synthetic.main.tag_main_activity.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val dbManager = DbManagerClass(this)
    val myAdapter = MyAdapterRC(ArrayList(), this)

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
    private var prefsTheme: SharedPreferences? = null
    private var prefsDelete: SharedPreferences? = null

    private val handler = Handler(Looper.getMainLooper())




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

            setContentView(R.layout.main_menu_drawer)
            nav_view_menu.setNavigationItemSelectedListener(this)




        //Сохранение настроек темы//
        prefsTheme = getSharedPreferences("settingsTheme", Context.MODE_PRIVATE)
        themeSet = prefsTheme?.getInt("settingsTheme", 0)!!
        //Сохранение настроек удаления//
        prefsDelete = getSharedPreferences("settingsDelete", Context.MODE_PRIVATE)
        deleteSet = prefsDelete?.getInt("settingsDelete", 0)!!

        //Принятие значений переменных с экрана Option//
        intent.putExtra("classTheme", themeSet)
        intent.putExtra("classDelete", deleteSet)

    }

    //Обновление//
    override fun onResume() {

        super.onResume()
            dbManager.openDb()
            themeChange()
            init()
            searchView()
            clearSearchView()
            chekItem(""); chekItemTag()
            saveDataTheme(themeSet)
            saveDataDelete(deleteSet)
            checkDelete()
            tagClick()





    }


    //Функция для сохранение настроек темы//
    private fun saveDataTheme(res: Int) {

        val editor = prefsTheme?.edit()
        editor?.putInt("settings", res)
        editor?.apply()

    }

    //Функция для сохранение настроек удаления//
    private fun saveDataDelete(resDel: Int) {

        val editorDelete = prefsDelete?.edit()
        editorDelete?.putInt("settingsDelete", resDel)
        editorDelete?.apply()

    }


    //Функция для изменения темы//
    private fun themeChange() {

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

        add_button.visibility = (View.VISIBLE)
        add_button2.visibility = (View.GONE)


    }

    //Меню//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val option = Intent(this, Option::class.java)
        val logout = Intent(this, Auth::class.java)

        when (item.itemId) {
            R.id.id_menu_settings -> {
                startActivity(option)
            }
            R.id.id_menu_logOut -> {
                finish()
                startActivity(logout)
            }
        }
        return true

    }

    //Функция для кнопки создания заметок//
    fun onClickNew(@Suppress("UNUSED_PARAMETER") view: View) {
        val editActivity = Intent(this, EditActivity::class.java)

        add_button.visibility = (View.GONE)
        add_button2.visibility = (View.VISIBLE)

        startActivity(editActivity)
    }

    //RecyclerView//
    private fun init() {
        rc_view.adapter = myAdapter
        val swapHelper = getSwap()
        swapHelper.attachToRecyclerView(rc_view)
        rc_view.setHasFixedSize(true)


    }
    private fun rcViewUp(){ rc_view.scrollBy(-1,-900000)}

    //Поиск//
    private fun clearSearchView()
    {
        search_view_main.clearFocus()
        search_view_main.setQuery("", false)
        search_view_main.isIconified = true
    }

    private fun searchView() {

        if(myAdapter.itemCount==0){clearSearchView()}
        search_view_main.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(text: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                rd1.clearCheck(); clearButtonGone()
                chekItem(text!!)

                return true

            }
        })
    }

    //Функция для проверки элементов для поиска//
    private fun chekItem(text: String) {


        val list = dbManager.readDbData(text)


        myAdapter.updateAdapter(list)
        if (list.size > 0) { tv_no_elements.visibility = View.GONE }
        else { tv_no_elements.visibility = View.VISIBLE }




    }

    //Свайпы и удаление//

    //Функция для проверки настройки удаления//
    private fun checkDelete() {
        if (deleteSet == 0) { many = true;del = true } else { many = false }
        if(!many){button_del_cancel.visibility = (View.GONE)}
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
        many = true; del = true; con = 999;button_del_cancel.visibility =
            (View.VISIBLE);add_button.visibility = (View.GONE)
        yesOrNoGone()
    }

    //Функция для кнопки отменение удаления УДАЛИТ НЕСКОЛЬКО//
    fun manyDelCancel(@Suppress("UNUSED_PARAMETER") view: View) { many = false; del = false
        button_del_cancel.visibility = (View.GONE)
        add_button.visibility = (View.VISIBLE) }

    //Функция для вызова контекстного окна удаления//
    fun yesOrNoVisible() { background_delete_window.visibility = (View.VISIBLE)
        delete_window_main_activity.visibility = (View.VISIBLE) }

    //Функция для отмены вызова контекстного окна удаления//
    private fun yesOrNoGone() { background_delete_window.visibility = (View.GONE)
        delete_window_main_activity.visibility = (View.GONE) }

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

                        if(myAdapter.itemCount==0) { tv_no_elements.visibility = View.VISIBLE }
                        else { tv_no_elements.visibility = View.GONE }}

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
    private fun tagClick() {

        check_tag_home.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        {  check_tag_home.isClickable=false; clear = 0; chekItemTag();clearSearchView() })

        check_tag_shop.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { check_tag_shop.isClickable=false; clear = 0;chekItemTag();clearSearchView() })

        check_tag_work.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { check_tag_work.isClickable=false; clear = 0;chekItemTag();clearSearchView() })

        check_tag_weekend.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { check_tag_weekend.isClickable=false; clear = 0;chekItemTag();clearSearchView() })

        check_tag_bank.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { check_tag_bank.isClickable=false; clear = 0;chekItemTag();clearSearchView() })

        check_tag_sport.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
        { check_tag_sport.isClickable=false; clear = 0; chekItemTag();clearSearchView() })


        clear_home.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_home.isClickable=false; clear = 1; chekItemTag(); rd1.clearCheck()
            clear_home.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)

        })

        clear_shop.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_shop.isClickable=false; clear = 2; chekItemTag(); rd1.clearCheck()
            clear_shop.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        })

        clear_work.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_work.isClickable=false; clear = 3; chekItemTag(); rd1.clearCheck()
            clear_work.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        })

        clear_weekend.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_weekend.isClickable=false; clear = 4; chekItemTag(); rd1.clearCheck()
            clear_weekend.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        })

        clear_bank.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_bank.isClickable=false; clear = 5; chekItemTag(); rd1.clearCheck()
            clear_bank.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        })

        clear_sport.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener {
            clear_sport.isClickable=false; clear = 6; chekItemTag();rd1.clearCheck()
            clear_sport.visibility = (View.GONE); handler.postDelayed({rcViewUp()}, 30)
        })
    }

    fun clearButtonGone()
    {
        clear_home.visibility = (View.GONE); check_tag_home.isClickable=true
        clear_shop.visibility = (View.GONE); check_tag_shop.isClickable=true
        clear_work.visibility = (View.GONE); check_tag_work.isClickable=true
        clear_weekend.visibility = (View.GONE); check_tag_weekend.isClickable=true
        clear_bank.visibility = (View.GONE); check_tag_bank.isClickable=true
        clear_sport.visibility = (View.GONE); check_tag_sport.isClickable=true
    }


    //Функция для проверки и выбора тега, а так же выбора заметок принадлежащих этому тегу//
    private fun chekItemTag() {
        var textTag = ""


        if (check_tag_home.isChecked) { textTag = tag.homeTag; clear_home.visibility = (View.VISIBLE) }
        else { clear_home.visibility = (View.GONE); check_tag_home.isClickable=true }

        if (check_tag_shop.isChecked) { textTag = tag.shopTag; clear_shop.visibility = (View.VISIBLE) }
        else { clear_shop.visibility = (View.GONE); check_tag_shop.isClickable=true }

        if (check_tag_work.isChecked) { textTag = tag.workTag; clear_work.visibility = (View.VISIBLE) }
        else { clear_work.visibility = (View.GONE); check_tag_work.isClickable=true }

        if (check_tag_weekend.isChecked) { textTag = tag.weekendTag; clear_weekend.visibility = (View.VISIBLE) }
        else { clear_weekend.visibility = (View.GONE); check_tag_weekend.isClickable=true }

        if (check_tag_bank.isChecked) { textTag = tag.bankTag; clear_bank.visibility = (View.VISIBLE) }
        else { clear_bank.visibility = (View.GONE); check_tag_bank.isClickable=true }

        if (check_tag_sport.isChecked) { textTag = tag.sportTag; clear_sport.visibility = (View.VISIBLE) }
        else { clear_sport.visibility = (View.GONE); check_tag_sport.isClickable=true }


        if (clear == 1) { textTag = ""; check_tag_home.isClickable=true }
        else{clear_home.isClickable=true}

        if (clear == 2) { textTag = ""; check_tag_shop.isClickable=true }
        else{clear_shop.isClickable=true}


        if (clear == 3) { textTag = ""; check_tag_work.isClickable=true }
        else{clear_work.isClickable=true}


        if (clear == 4) { textTag = ""; check_tag_weekend.isClickable=true }
        else{clear_weekend.isClickable=true}


        if (clear == 5) {textTag = ""; check_tag_bank.isClickable=true }
        else{clear_bank.isClickable=true}


        if (clear == 6) { textTag = ""; check_tag_sport.isClickable=true }
        else{clear_sport.isClickable=true}



        val list = dbManager.readDbDataTag(textTag)


        myAdapter.updateAdapter(list)
        if (list.size > 0) { tv_no_elements.visibility = View.GONE }
        else { tv_no_elements.visibility = View.VISIBLE }


    }







    //Закрытие окна//
    override fun onDestroy() { super.onDestroy() ; dbManager.closeDB()}
    fun reg(view: View) {}
    fun singIn(view: View) {}

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