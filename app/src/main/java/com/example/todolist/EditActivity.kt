package com.example.todolist

import dataBase.DbManagerClass
import dataBase.MyIntentConstant
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todolist.databinding.ActivityEditBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private var urlImgDb = "empty"
    var id = 0
    private var isEditState = false
    private val dbManager = DbManagerClass(this)
    private var tagIntent = "empty"
    private val tag = Tags()

    lateinit var binding: ActivityEditBinding


    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK ) {
            binding.imageViewEdit.setImageURI(it.data?.data)
            urlImgDb = it.data?.data.toString()
            contentResolver.takePersistableUriPermission(it.data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            binding.editImgLayout.visibility = View.VISIBLE
            binding.actionButtonImg.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        getMyIntents()
    }


    override fun onResume() {
        super.onResume()
        dbManager.openDb()
        tagClick()
        binding.edImgBut.isClickable=true
        binding.actionButtonImg.isClickable=true
        ifElseCheck()
    }


    fun onClickAddImg(@Suppress("UNUSED_PARAMETER")view: View) {

        binding.actionButtonImg.isClickable=false
        val intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGallery.type = "image/*"
        getResult.launch(intentGallery)
    }


    fun onClickDeleteIMG(@Suppress("UNUSED_PARAMETER")view: View) {

        binding.editImgLayout.visibility = View.GONE
        binding.actionButtonImg.visibility = View.VISIBLE

        urlImgDb = "empty"
    }


    fun onClickChooseImg(@Suppress("UNUSED_PARAMETER")view: View) {

        binding.edImgBut.isClickable=false
        val intentGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentGallery.type = "image/*"
        getResult.launch(intentGallery)
    }


    fun onClickSave(@Suppress("UNUSED_PARAMETER")view: View) {


        val titleFull = binding.edTitle.text.toString()
        val contentFull = binding.edContent.text.toString()


        if (titleFull != "") {

            binding.actionButtonEdit.visibility = (@Suppress("UNUSED_PARAMETER") View.GONE)
            binding.actionButtonEdit2.visibility = (@Suppress("UNUSED_PARAMETER") View.VISIBLE)

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
                binding.edTitle.setText(i.getStringExtra(MyIntentConstant.INTENT_TITLE_KEY))
                binding.edContent.setText(i.getStringExtra(MyIntentConstant.INTENT_CONTENT_KEY))

                //Теги обновление//
                tagIntent=(i.getStringExtra(MyIntentConstant.INTENT_TAG_KEY)!!)

                id = i.getIntExtra(MyIntentConstant.INTENT_ID_KEY, 0)

                if(i.getStringExtra(MyIntentConstant.INTENT_URL_KEY) != "empty" ){
                    binding.editImgLayout.visibility = View.VISIBLE
                    binding.actionButtonImg.visibility = View.GONE
                    urlImgDb=i.getStringExtra(MyIntentConstant.INTENT_URL_KEY)!!
                    binding.imageViewEdit. setImageURI(Uri.parse(urlImgDb))

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
    private fun tagClick()= with(binding){


        checkGetTagHome.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagHome.isChecked){ tagIntent = tag.homeTag } else { tag.homeTag ="" } })

        checkGetTagShop.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagShop.isChecked){ tagIntent = tag.shopTag } else { tag.shopTag = "" } })

        checkGetTagWork.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagWork.isChecked){ tagIntent = tag.workTag } else { tag.workTag = "" } })

        checkGetTagWeekend.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagWeekend.isChecked){ tagIntent = tag.weekendTag } else { tag.weekendTag = "" } })

        checkGetTagBank.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagBank.isChecked){ tagIntent = tag.bankTag } else { tag.bankTag = "" } })

        checkGetTagSport.setOnClickListener(@Suppress("UNUSED_PARAMETER")View.OnClickListener {
            if (checkGetTagSport.isChecked){ tagIntent = tag.sportTag } else { tag.sportTag = "" } })
    }

    fun onClickAddTag(@Suppress("UNUSED_PARAMETER")view: View) = with(binding) {

        tagWindow.visibility=View.VISIBLE
        actionButtonTag.visibility=View.GONE
        actionButtonTag2.visibility=View.VISIBLE

    }

    fun onClickAddTagOff(@Suppress("UNUSED_PARAMETER")view: View) = with(binding)  {

        tagWindow.visibility=View.GONE
        actionButtonTag.visibility=View.VISIBLE
        actionButtonTag2.visibility=View.GONE

    }

    private fun ifElseCheck()= with(binding) {
        //Проверка выбранных тегов//
        if (tagIntent == tag.homeTag){ checkGetTagHome.isChecked = true }
        if (tagIntent == tag.shopTag){ checkGetTagShop.isChecked = true }
        if (tagIntent == tag.workTag){ checkGetTagWork.isChecked = true }
        if (tagIntent == tag.weekendTag){ checkGetTagWeekend.isChecked = true }
        if (tagIntent == tag.bankTag){ checkGetTagBank.isChecked = true }
        if (tagIntent == tag.sportTag){ checkGetTagSport.isChecked = true }

    }




    //Обработка нажатий на поле ввода текста, для открытия и закрытия клавиатуры//
    //  fun closeEditTextTitle(view: android.view.View) {closeKeyboardTitle();ed_title.isEnabled=true}
    //  fun closeEditTextContent(view: android.view.View) {closeKeyboardContent();ed_content.isEnabled=true;ed_title.isEnabled=true}
    //  fun closeKeyboardTitle(){ed_title.isEnabled=false}
    //  fun closeKeyboardContent(){ed_content.isEnabled=false}


    //Закрытие окна//
    override fun onDestroy() {

        super.onDestroy()
        dbManager.closeDB()
        finish()

    }


}
