package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.database.TodoItemDBHelper
import com.example.myapplication.model.TodoItem

class TodoItemActivity : AppCompatActivity() {
    private lateinit var save_btn : Button
    private lateinit var title_text: EditText
    private lateinit var note_text: EditText
    lateinit var todoDBHelper: TodoItemDBHelper
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set action bar

        setContentView(R.layout.activity_todo_item)

        toolbar = findViewById(R.id.back_toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)


        //set ui data
        save_btn = findViewById(R.id.save_btn) as Button
        title_text = findViewById(R.id.title)as EditText
        note_text = findViewById(R.id.note) as EditText

        //listener
        save_btn.setOnClickListener{
            var item = TodoItem(null, title_text.text.toString(),note_text.text.toString(), "","" )
            saveItem(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    public fun saveItem(item:TodoItem){
        todoDBHelper = TodoItemDBHelper(this)
        var created = todoDBHelper.insertTododItem(item)
        if (created){
            Toast.makeText(applicationContext, "Item Created", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(applicationContext, "An error occured", Toast.LENGTH_SHORT).show()
        }
    }


    //handle closing the page with back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView){
            R.id.back_menu ->{
              finish()
            }
        }
        return false
    }


}