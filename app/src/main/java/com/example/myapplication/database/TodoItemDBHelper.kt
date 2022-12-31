package com.example.myapplication.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.TodoItem

class TodoItemDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.TodoItemEntry.TABLE_NAME + " (" +
                    DBContract.TodoItemEntry.COLUMN_ID + " TEXT PRIMARY KEY AUTOINCREMENT," +
                    DBContract.TodoItemEntry.COLUMN_NOTE + " TEXT," +
                    DBContract.TodoItemEntry.COLUMN_TITLE + " TEXT," +
                    DBContract.TodoItemEntry.COLUMN_TIME + " TEXT," +
                    DBContract.TodoItemEntry.COLUMN_DATE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.TodoItemEntry.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertTododItem(todoItem:TodoItem): Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.TodoItemEntry.COLUMN_ID, todoItem.Id)
        values.put(DBContract.TodoItemEntry.COLUMN_TITLE, todoItem.Title)
        values.put(DBContract.TodoItemEntry.COLUMN_NOTE, todoItem.Note)
        values.put(DBContract.TodoItemEntry.COLUMN_DATE, todoItem.Date)
        values.put(DBContract.TodoItemEntry.COLUMN_TIME, todoItem.Time)

        val newRowId = db.insert(DBContract.TodoItemEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(userid: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.TodoItemEntry.COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(userid)
        // Issue SQL statement.
        db.delete(DBContract.TodoItemEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    @SuppressLint("Range")
    fun readUser(todoId: String): ArrayList<TodoItem> {
        val todoItems = ArrayList<TodoItem>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.TodoItemEntry.TABLE_NAME + " WHERE " + DBContract.TodoItemEntry.COLUMN_ID + "='" + todoId + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var title: String
        var id: String
        var note: String
        var date: String
        var time: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_ID))
                title = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_TITLE))
                note = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_NOTE))
                date = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_DATE))
                time  = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_TIME))

                todoItems.add(TodoItem(id,title,note,date,time))
                cursor.moveToNext()
            }
        }
        return todoItems
    }

    @SuppressLint("Range")
    fun readAllUsers(): ArrayList<TodoItem> {
        val todoItems = ArrayList<TodoItem>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.TodoItemEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var title: String
        var id: String
        var note: String
        var date: String
        var time: String

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_ID))
                title = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_TITLE))
                note = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_NOTE))
                date = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_DATE))
                time  = cursor.getString(cursor.getColumnIndex(DBContract.TodoItemEntry.COLUMN_TIME))

                todoItems.add(TodoItem(id,title,note,date,time))
                cursor.moveToNext()
            }
        }
        return todoItems
    }


}