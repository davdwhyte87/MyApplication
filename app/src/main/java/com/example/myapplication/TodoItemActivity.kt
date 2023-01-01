package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
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
    private lateinit var datePicker:MaterialDatePicker<Long>
    private  lateinit var calendar: Calendar
    lateinit var dateView: TextView
    lateinit var timeView:TextView
    lateinit var toolbar: Toolbar
    lateinit var selectTimeBtn: Button
    lateinit var selectDateBtn:Button
    lateinit var deleteBtn:ImageButton
    lateinit var alarmManager:AlarmManager
    lateinit var selectedDate : Date
    lateinit var item: TodoItem
    var hasPickedTime = false
    var hasPickedDate = false



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
        deleteBtn = findViewById(R.id.delete_btn) as ImageButton

        //initialize date pickers
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Due Date")
            .build()
        datePicker = MaterialDatePicker.Builder.datePicker().build()


        //listener
        save_btn.setOnClickListener{

            saveItem()
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
        deleteBtn.setOnClickListener {
            deleteItem()
        }

    }

    private fun deleteItem(){
        val intent = intent
        val id = intent.getStringExtra("item_id")

        if (id !="0" && id!=null){
            val todoDBHelper = TodoItemDBHelper(this)
            if(todoDBHelper.deleteItem(id) ){
                Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // take in date data and set an alarm on the android OS schedule manager
    private fun setAlarm(){
        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY]= picker.hour
        calendar[Calendar.MINUTE] = picker.minute
        calendar[Calendar.DAY_OF_MONTH] = selectedDate.day
        calendar[Calendar.MONTH]= selectedDate.month
        calendar[Calendar.YEAR]= selectedDate.year

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReciever::class.java )
        val pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
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
            item = todoDBHelper.readItem(id!!)[0]
            title_text.text.append(item.Title)
            note_text.text.append(item.Note)
            timeView.text = item.Time
            dateView.text = item.Date

        }

    }

    private fun showDatePicker(){

        datePicker.show(supportFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            val dateFOrmatter = SimpleDateFormat("dd-MM-yy")
            selectedDate = Date(it)
            val date = dateFOrmatter.format(Date(it))
            dateView.text = date
            hasPickedDate = true
        }
    }
    private fun showTimePicker(){

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

            hasPickedTime = true
        }
    }



    public fun saveItem(){
        if (item.Id == null){
            // create new item on db if there is non existing in this activity
            item = TodoItem(null, title_text.text.toString(),
                note_text.text.toString(), dateView.text.toString(),timeView.text.toString() )
            todoDBHelper = TodoItemDBHelper(this)
            todoDBHelper.insertTododItem(item)
            Toast.makeText(applicationContext, "Item Created", Toast.LENGTH_SHORT).show()

        }

        //updated item data
        var newItem = TodoItem(item.Id, title_text.text.toString(),
            note_text.text.toString(), dateView.text.toString(),timeView.text.toString() )

        todoDBHelper = TodoItemDBHelper(this)
        todoDBHelper.insertTododItem(newItem)
        Toast.makeText(applicationContext, "Item Created", Toast.LENGTH_SHORT).show()



        // set alarm after saving data. set alarm if date is selected
        if(hasPickedDate && hasPickedTime){
            setAlarm()
        }

        finish()
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