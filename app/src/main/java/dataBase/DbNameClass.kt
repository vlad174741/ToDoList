package dataBase

import android.provider.BaseColumns

object DbNameClass {

    const val TABLE_NAME = "my_table"    					                //Имя таблицы//
    const val COLUMN_NAME_TITLE = "column_title"				            //Имя колонки с заголовками//
    const val COLUMN_NAME_SUBTITLE = "column_subtitle"			            //Имя колонки с подзаголовками//
    const val COLUMN_NAME_IMG_URL = "url"
    const val COLUMN_NAME_TIME = "time"
    const val COLUMN_NAME_TAG = "tag"


    const val DATABASE_VERSION = 3						                    //Версия БД//
    const val DATABASE_NAME = "ToDoListDataBase1.db"			                	//Имя БД//

    const val CREATE_TABLE ="CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +	//Для создания таблицы (в случае если таблица не существует, будет создана таблица из переменной TABLE_NAME)//

            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_SUBTITLE TEXT," +
            "$COLUMN_NAME_IMG_URL TEXT, $COLUMN_NAME_TIME TEXT, $COLUMN_NAME_TAG TEXT)"  		//Создание первого столбца таблицы с ID идентификторами// //Создание второго столбца таблицы с заголовками// //Создание третьего столбца таблицы с подзаголовками//


    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"		    //Для удаления таблицы(нужно при обновлении таблицы)//

}