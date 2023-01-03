package com.example.myapplication.database

import android.provider.BaseColumns

object DBContract {

    class TodoItemEntry : BaseColumns{
        companion object{
            val TABLE_NAME= "todo_item"
            val COLUMN_ID = "id"
            val COLUMN_TITLE = "title"
            val COLUMN_NOTE = "note"
            val COLUMN_DATE = "date"
            val COLUMN_TIME = "time"

        }
    }

}