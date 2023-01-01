package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.model.TodoItem

class TodoListAdapter(private val context: Activity, val todoItems: ArrayList<TodoItem>) : ArrayAdapter<TodoItem>(context, R.layout.todo_list, todoItems) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.todo_list, null, true)

        val titleText = rowView.findViewById<TextView>(R.id.view_title)
        titleText.text = todoItems[position].Title

        return rowView
    }
}