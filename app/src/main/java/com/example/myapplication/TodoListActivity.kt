package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import com.example.myapplication.database.TodoItemDBHelper
import com.example.myapplication.model.TodoItem
import java.util.logging.Level.INFO

class TodoListActivity : AppCompatActivity() {
    private lateinit var main_toolbar: androidx.appcompat.widget.Toolbar
    lateinit var todoDBHelper: TodoItemDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* Log.i("XXXXXX","Helo brroooooooanh  fjsdnfjks nfkndsfjndf k")*/
        todoDBHelper = TodoItemDBHelper(this)


        setContentView(R.layout.activity_todo_list)
        main_toolbar = findViewById(R.id.main_toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(main_toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView){
            R.id.add_menu ->{
                val intent = Intent(this, TodoItemActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Add item", Toast.LENGTH_SHORT).show()
            }
        }
        return false

    }
}