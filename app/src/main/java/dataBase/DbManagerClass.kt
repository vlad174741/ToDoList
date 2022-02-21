package dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns


class DbManagerClass (context: Context) {

    private val myDbHelper = DbHelperClass(context)            //объявляем класс через переменную//
    private var db: SQLiteDatabase? = null                     //объявляем SQLite через переменную//

    fun openDb() {

        db = myDbHelper.writableDatabase

    }

    fun insertToDb(title: String, content: String, url: String, time:String, tag:String)  {

        val values = ContentValues().apply {

            put(DbNameClass.COLUMN_NAME_TITLE, title)
            put(DbNameClass.COLUMN_NAME_SUBTITLE, content)
            put(DbNameClass.COLUMN_NAME_IMG_URL, url)
            put(DbNameClass.COLUMN_NAME_TIME, time)
            put(DbNameClass.COLUMN_NAME_TAG, tag)

        }
        db?.insert(DbNameClass.TABLE_NAME, null, values)




    }


    fun removeItemToDb( id: String) {

        val selection = BaseColumns._ID + "=$id"
        db?.delete(DbNameClass.TABLE_NAME, selection, null)

    }


    fun updateItem(title: String, content: String, url: String, id: Int, time:String, tag:String)  {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(DbNameClass.TABLE_NAME, selection, null)

        val values = ContentValues().apply {

            put(DbNameClass.COLUMN_NAME_TITLE, title)
            put(DbNameClass.COLUMN_NAME_SUBTITLE, content)
            put(DbNameClass.COLUMN_NAME_IMG_URL, url)
            put(DbNameClass.COLUMN_NAME_TIME, time)
            put(DbNameClass.COLUMN_NAME_TAG, tag)


        }
        db?.insert(DbNameClass.TABLE_NAME, null, values)


    }


    //Заметки//

    @SuppressLint("Range")
    fun readDbData(searchText:String) : ArrayList<ListItemDB>  {
        val dataList = ArrayList<ListItemDB>()
        val selection = "${DbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(DbNameClass.TABLE_NAME, null, selection,
            arrayOf("%$searchText%"), null, null, null)


        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_SUBTITLE))
            val dataIMG = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_IMG_URL))
            val dataID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val dataTime = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TIME))
            val dataTag = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TAG))

            val item = ListItemDB()
            item.titleLI = dataTitle
            item.contentLI = dataContent
            item.urlLI = dataIMG
            item.idLI = dataID
            item.time = dataTime
            item.tag = dataTag

            dataList.add(item)

        }

        cursor.close()
        return dataList

    }


    //Теги//

    @SuppressLint("Range")
    fun readDbDataTag(searchText:String) : ArrayList<ListItemDB>  {
        val dataList = ArrayList<ListItemDB>()
        val selection = "${DbNameClass.COLUMN_NAME_TAG} like ?"
        val cursor = db?.query(DbNameClass.TABLE_NAME, null, selection,
            arrayOf("%$searchText%"), null, null, null)


        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_SUBTITLE))
            val dataIMG = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_IMG_URL))
            val dataID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val dataTime = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TIME))
            val dataTag = cursor.getString(cursor.getColumnIndex(DbNameClass.COLUMN_NAME_TAG))

            val item = ListItemDB()
            item.titleLI = dataTitle
            item.contentLI = dataContent
            item.urlLI = dataIMG
            item.idLI = dataID
            item.time = dataTime
            item.tag = dataTag

            dataList.add(item)

        }

        cursor.close()
        return dataList

    }

    fun closeDB() {
        myDbHelper.close()
    }


}

