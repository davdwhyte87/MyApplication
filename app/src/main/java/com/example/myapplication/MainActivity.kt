package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private  lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        binding.setTime.setOnClickListener{
            showTimePicker()
        }

        binding.setAlarm.setOnClickListener{
            setAlarm()
        }
        binding.cancelAlarm.setOnClickListener{
           cancelAlarm()
        }
    }

    private fun cancelAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReciever::class.java )
        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show()
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
                binding.selectedTime.text= String.format("%02d", picker.hour-12)+" : " +String.format("%02d", picker.minute)+" PM"
            }else{
                binding.selectedTime.text= String.format("%02d", picker.hour-12)+" : " +String.format("%02d", picker.minute)+" AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY]= picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND]= 0
        }
    }

    private fun setAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReciever::class.java )
        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "alarmappchannel"
            val description = "Channel for Alarm app"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val chanel = NotificationChannel("AlarmNotify", name, importance)
            chanel.description = description

            val notificationManager =  getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanel)
        }
    }
}