package com.example.jkfb_dbproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.jkfb_dbproject1.database.DatabaseHelper
import com.example.jkfb_dbproject1.model.ClassListModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddTask : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSave: Button
    private lateinit var etName: TextInputEditText
    private lateinit var etDetails: TextInputEditText
    private lateinit var tilName: TextInputLayout
    private lateinit var tilDetails: TextInputLayout

    private var dbHandler: DatabaseHelper? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        btnBack = findViewById(R.id.btn_back)
        btnSave = findViewById(R.id.btn_save)
        etName = findViewById(R.id.et_name)
        etDetails = findViewById(R.id.et_details)
        tilName = findViewById(R.id.til_name)
        tilDetails = findViewById(R.id.til_details)

        dbHandler = DatabaseHelper(this)

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            btnSave.text = "Update Contact"
            val taskId = intent.getIntExtra("TaskId", 0)
            val task = dbHandler?.getTaskById(taskId)
            if (task != null) {
                etName.setText(task.name)
                etDetails.setText(task.details)
            }
        } else {
            isEditMode = false
            btnSave.text = "Save Contact"
        }

        btnSave.setOnClickListener {
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
            task.name = name
            task.details = details

            var success: Boolean
            if (isEditMode) {
                task.id = intent.getIntExtra("TaskId", 0)
                success = dbHandler?.updateTask(task) as Boolean
            } else {
                success = dbHandler?.addTask(task) as Boolean
            }

            if (success) {
                Toast.makeText(applicationContext, if (isEditMode) "Contact Updated Successfully!" else "Contact Added Successfully!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }

            btnSave.isEnabled = name.isNotEmpty() && details.isNotEmpty()
        }
    }
}