package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.database.TodoItemDBHelper
import com.example.myapplication.model.TodoItem
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class TodoItemActivity : AppCompatActivity() {
    private lateinit var save_btn : Button
    private lateinit var title_text: EditText
    private lateinit var note_text: EditText
    lateinit var todoDBHelper: TodoItemDBHelper
    private lateinit var picker: MaterialTimePicker
    private  lateinit var calendar: Calendar
    lateinit var dateView: TextView
    lateinit var timeView:TextView
    lateinit var toolbar: Toolbar
    lateinit var selectTimeBtn: Button
    lateinit var selectDateBtn:Button


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
        dateView = findViewById(R.id.date_view) as TextView
        timeView = findViewById(R.id.time_view) as TextView
        selectDateBtn = findViewById(R.id.selectDate) as Button
        selectTimeBtn = findViewById(R.id.selectTime) as Button

        //listener
        save_btn.setOnClickListener{
            var item = TodoItem(null, title_text.text.toString(),
                note_text.text.toString(), dateView.text.toString(),timeView.text.toString() )
            saveItem(item)
        }

        //fill items with data from previous activity if there is any
        fillItems()

        // date time button clkick listeners
        selectDateBtn.setOnClickListener {
            showDatePicker()
        }
        selectTimeBtn.setOnClickListener {
           showTimePicker()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun fillItems(){
        val intent = intent
        val id = intent.getStringExtra("item_id")

        if (id !="0" && id!=null){
            val todoDBHelper = TodoItemDBHelper(this)
            val item = todoDBHelper.readItem(id!!)[0]
            title_text.text.append(item.Title)
            note_text.text.append(item.Note)
            timeView.text = item.Time
            dateView.text = item.Date
        }

    }

    private fun showDatePicker(){
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(supportFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            val dateFOrmatter = SimpleDateFormat("dd-MM-yy")
            val date = dateFOrmatter.format(Date(it))
            dateView.text = date
        }
    }
    private fun showTimePicker(){
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Due Date")
            .build()
        picker.show(supportFragmentManager, "AlarmNotify")

        picker.addOnPositiveButtonClickListener{
            if(picker.hour > 12){
                timeView.text= String.format("%02d", picker.hour-12)+" : " +String.format("%02d", picker.minute)+" PM"
            }else{
                timeView.text= String.format("%02d", picker.hour-12)+" : " +String.format("%02d", picker.minute)+" AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY]= picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND]= 0
        }
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