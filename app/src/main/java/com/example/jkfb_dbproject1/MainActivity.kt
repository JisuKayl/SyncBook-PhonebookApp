package com.example.jkfb_dbproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jkfb_dbproject1.adapter.TaskListAdapter
import com.example.jkfb_dbproject1.database.DatabaseHelper
import com.example.jkfb_dbproject1.model.ClassListModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

lateinit var recyclerTask: RecyclerView
lateinit var btnAdd: ImageButton
lateinit var taskListAdapter: TaskListAdapter
var dbHandler: DatabaseHelper? = null
var taskList: List<ClassListModel> = ArrayList<ClassListModel>()
var linearLayoutManager: LinearLayoutManager? = null
private lateinit var etSearch: TextInputEditText
private lateinit var tilSearch: TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerTask = findViewById(R.id.RecyclerView)
        btnAdd = findViewById(R.id.btn_add_items)
        etSearch = findViewById(R.id.etSearch)
        tilSearch = findViewById(R.id.til_search)

        dbHandler = DatabaseHelper(this)

        taskListAdapter = TaskListAdapter(emptyList(), applicationContext, this)
        recyclerTask.adapter = taskListAdapter

        fetchList()

        btnAdd.setOnClickListener {
            val i = Intent(applicationContext, AddTask::class.java)
            startActivity(i)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchContacts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchList() {
        dbHandler?.let { handler ->
            taskList = handler.getAllTask()
            taskListAdapter.updateTaskList(taskList)
            linearLayoutManager = LinearLayoutManager(applicationContext)
            recyclerTask.layoutManager = linearLayoutManager
        }
    }
    override fun onResume() {
        super.onResume()
        fetchList()
        taskList = dbHandler!!.getAllTask()
        taskListAdapter.updateTaskList(taskList)
    }

    private fun searchContacts(query: String) {
        dbHandler?.let { handler ->
            val allTasks = handler.getAllTask()
            val filteredList = if (query.isNotEmpty()) {
                allTasks.filter {
                    it.name.contains(query, true) || it.details.contains(query, true)
                }
            } else {
                allTasks
            }
            taskListAdapter.updateTaskList(filteredList)
        }
    }
}