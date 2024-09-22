package com.example.jkfb_dbproject1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jkfb_dbproject1.database.DatabaseHelper
import com.example.jkfb_dbproject1.model.ClassListModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditTask : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnUpdate: Button
    private lateinit var etName: TextInputEditText
    private lateinit var etDetails: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var tilDetails: TextInputLayout

    private var dbHandler: DatabaseHelper? = null
    private var taskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        btnBack = findViewById(R.id.btn_back)
        btnUpdate = findViewById(R.id.btn_update)
        etName = findViewById(R.id.et_name)
        etDetails = findViewById(R.id.et_details)
        tilName = findViewById(R.id.til_name)
        tilDetails = findViewById(R.id.til_details)

        dbHandler = DatabaseHelper(this)

        taskId = intent.getIntExtra("TaskId", 0)
        val task = dbHandler?.getTaskById(taskId)
        if (task != null) {
            etName.setText(task.name)
            etDetails.setText(task.details)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString().trim()
            val details = etDetails.text.toString().trim()

            if (name.isEmpty() || details.isEmpty()) {
                if (name.isEmpty()) {
                    tilName.error = "Please enter a name"
                } else {
                    tilName.isErrorEnabled = false
                }

                if (details.isEmpty()) {
                    tilDetails.error = "Please enter details"
                } else {
                    tilDetails.isErrorEnabled = false
                }
                Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val task = ClassListModel()
            task.id = taskId
            task.name = name
            task.details = details

            val success = dbHandler?.updateTask(task) as Boolean
            if (success) {
                Toast.makeText(applicationContext, "Contact updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }

            btnUpdate.isEnabled = name.isNotEmpty() && details.isNotEmpty()
        }
    }
}