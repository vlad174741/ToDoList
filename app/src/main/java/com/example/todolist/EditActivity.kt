package com.example.todolist

import dataBase.DbManagerClass
import dataBase.MyIntentConstant
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.tag_activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private var urlImgDb = "empty"
    var id = 0
    private var isEditState = false
    private val dbManager = DbManagerClass(this)
    private var tagIntent = "empty"
    private val tag = Tags()


    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK ) {
            image_view_edit.setImageURI(it.data?.data)
            urlImgDb = it.data?.data.toString()
            contentResolver.takePersistableUriPermission(it.data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            edit_img_layout.visibility = View.VISIBLE
            action_button_img.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        getMyIntents()
    }


    override fun onResume() {
        super.onResume()
        dbManager.openDb()
        tagClick()
        ed_img_but.isClickable=true
        action_button_img.isClickable=true
        ifElseCheck()
    }


    fun onClickAddImg(@Suppress("UNUSED_PARAMETER")view: View) {

        action_button_img.isClickable=false
        val intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGallery.type = "image/*"
        getResult.launch(intentGallery)
    }


    fun onClickDeleteIMG(@Suppress("UNUSED_PARAMETER")view: View) {

        edit_img_layout.visibility = View.GONE
        action_button_img.visibility = View.VISIBLE

        urlImgDb = "empty"
    }


    fun onClickChooseImg(@Suppress("UNUSED_PARAMETER")view: View) {

        ed_img_but.isClickable=false
        val intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGallery.type = "image/*"
        getResult.launch(intentGallery)
    }


    fun onClickSave(@Suppress("UNUSED_PARAMETER")view: View) {

        val titleFull = ed_title.text.toString()
        val contentFull = ed_content.text.toString()

        if (titleFull != "") {

            action_button_edit.visibility = (@Suppress("UNUSED_PARAMETER") View.GONE)
            action_button_edit2.visibility = (@Suppress("UNUSED_PARAMETER") View.VISIBLE)

            if (isEditState) {
                dbManager.updateItem(titleFull, contentFull, urlImgDb, id, getTime(), tagIntent)
            } else {
                dbManager.insertToDb(titleFull, contentFull, urlImgDb, getTime(), tagIntent)
            }
            finish()
        }
    }


    private fun getMyIntents() {

        val i = intent

        if (i != null) {

            if (i.getStringExtra(MyIntentConstant.INTENT_TITLE_KEY) != null) {

                isEditState = true
                ed_title.setText(i.getStringExtra(MyIntentConstant.INTENT_TITLE_KEY))
                ed_content.setText(i.getStringExtra(MyIntentConstant.INTENT_CONTENT_KEY))

                //Теги обновление//
                tagIntent=(i.getStringExtra(MyIntentConstant.INTENT_TAG_KEY)!!)

                id = i.getIntExtra(MyIntentConstant.INTENT_ID_KEY, 0)

                if(i.getStringExtra(MyIntentConstant.INTENT_URL_KEY) != "empty" ){
                    edit_img_layout.visibility = View.VISIBLE
                    action_button_img.visibility = View.GONE
                    urlImgDb=i.getStringExtra(MyIntentConstant.INTENT_URL_KEY)!!
                    image_view_edit. setImageURI(Uri.parse(urlImgDb))

                }
            }
        }
    }


    //Дата и время создания заметки//
    private fun getTime(): String {

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)

    }


    //Теги//
    private fun tagClick(){


        check_get_tag_home.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_home.isChecked){ tagIntent = tag.homeTag } else { tag.homeTag ="" } })

        check_get_tag_shop.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_shop.isChecked){ tagIntent = tag.shopTag } else { tag.shopTag = "" } })

        check_get_tag_work.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_work.isChecked){ tagIntent = tag.workTag } else { tag.workTag = "" } })

        check_get_tag_weekend.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_weekend.isChecked){ tagIntent = tag.weekendTag } else { tag.weekendTag = "" } })

        check_get_tag_bank.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_bank.isChecked){ tagIntent = tag.bankTag } else { tag.bankTag = "" } })

        check_get_tag_other.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (check_get_tag_other.isChecked){ tagIntent = tag.sportTag } else { tag.sportTag = "" } })
    }

    fun onClickAddTag(@Suppress("UNUSED_PARAMETER")view: View) {

        tag_window.visibility=View.VISIBLE
        action_button_tag.visibility=View.GONE
        action_button_tag2.visibility=View.VISIBLE

    }

    fun onClickAddTagOff(@Suppress("UNUSED_PARAMETER")view: View) {

        tag_window.visibility=View.GONE
        action_button_tag.visibility=View.VISIBLE
        action_button_tag2.visibility=View.GONE

    }

    private fun ifElseCheck(){
        //Проверка выбранных тегов//
        if (tagIntent == tag.homeTag){ check_get_tag_home.isChecked = true }
        if (tagIntent == tag.shopTag){ check_get_tag_shop.isChecked = true }
        if (tagIntent == tag.workTag){ check_get_tag_work.isChecked = true }
        if (tagIntent == tag.weekendTag){ check_get_tag_weekend.isChecked = true }
        if (tagIntent == tag.bankTag){ check_get_tag_bank.isChecked = true }
        if (tagIntent == tag.sportTag){ check_get_tag_other.isChecked = true }

    }




    //Обработка нажатий на поле ввода текста, для открытия и закрытия клавиатуры//
    //  fun closeEditTextTitle(view: android.view.View) {closeKeyboardTitle();ed_title.isEnabled=true}
    //  fun closeEditTextContent(view: android.view.View) {closeKeyboardContent();ed_content.isEnabled=true;ed_title.isEnabled=true}
    //  fun closeKeyboardTitle(){ed_title.isEnabled=false}
    //  fun closeKeyboardContent(){ed_content.isEnabled=false}


    //Закрытие окна//
    override fun onDestroy() { super.onDestroy(); dbManager.closeDB() }


}
