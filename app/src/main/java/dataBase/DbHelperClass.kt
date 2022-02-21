package dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelperClass (context: Context) : SQLiteOpenHelper(context, DbNameClass.DATABASE_NAME,
    null, DbNameClass.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) { 				//функция для создания таблицы//
        db?.execSQL(DbNameClass.CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { 	//функция для обновления таблицы//

        db?.execSQL(DbNameClass.SQL_DELETE_TABLE)
        onCreate(db)
    }
}



