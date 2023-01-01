package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.myapplication.database.TodoItemDBHelper
import com.example.myapplication.model.TodoItem
import com.example.myapplication.ui.TodoListAdapter
import java.util.logging.Level.INFO

class TodoListActivity : AppCompatActivity() {
    private lateinit var main_toolbar: androidx.appcompat.widget.Toolbar
    lateinit var todoDBHelper: TodoItemDBHelper
    lateinit var myListView: ListView
    lateinit var  arrayAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* Log.i("XXXXXX","Helo brroooooooanh  fjsdnfjks nfkndsfjndf k")*/
        todoDBHelper = TodoItemDBHelper(this)


        setContentView(R.layout.activity_todo_list)
        main_toolbar = findViewById(R.id.main_toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(main_toolbar)

        //get data from db
        todoDBHelper = TodoItemDBHelper(this)
//        todoDBHelper.dropDB()
        val items = todoDBHelper.readAllItems()
        // display list o todo items
        Log.i("XXXXXXXXXXX", items.size.toString())
        myListView = findViewById<ListView>(R.id.todo_list) as ListView
        arrayAdapter = TodoListAdapter(this,items)
        myListView.adapter = arrayAdapter

        //set click listener
        myListView.setOnItemClickListener{ parent, view, position, id ->
            val data = arrayAdapter.getItem(position)
            val intent = Intent(this, TodoItemActivity::class.java)
            intent.putExtra("item_id", data!!.Id.toString())

            startActivity(intent)
        }


    }

    override fun onResume() {
        todoDBHelper = TodoItemDBHelper(this)
//        todoDBHelper.dropDB()
        val items = todoDBHelper.readAllItems()
        // display list of todo items
        myListView = findViewById<ListView>(R.id.todo_list) as ListView
        arrayAdapter = TodoListAdapter(this,items)
        myListView.adapter = arrayAdapter
        super.onResume()
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