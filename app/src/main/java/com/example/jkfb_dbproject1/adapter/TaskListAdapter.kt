package com.example.jkfb_dbproject1.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jkfb_dbproject1.AddTask
import com.example.jkfb_dbproject1.EditTask
import com.example.jkfb_dbproject1.MainActivity
import com.example.jkfb_dbproject1.R
import com.example.jkfb_dbproject1.database.DatabaseHelper
import com.example.jkfb_dbproject1.model.ClassListModel

class TaskListAdapter(var taskList: List<ClassListModel>, internal var context: Context, private val activity: Activity) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.txtName)
        var details: TextView = view.findViewById(R.id.txtDetails)
        var btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        var btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        var userIcon: ImageView = view.findViewById(R.id.imageViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.details.text = tasks.details

        holder.btnEdit.setOnClickListener {
            val intent = Intent(activity, EditTask::class.java)
            intent.putExtra("TaskId", tasks.id)
            activity.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirm Deletion")
            builder.setMessage("Are you sure you want to delete this contact?")
            builder.setPositiveButton("Yes") { _, _ ->
                val dbHandler = DatabaseHelper(activity)
                val success = dbHandler.deleteTask(tasks.id)
                if (success) {
                    taskList = taskList.filterNot { it.id == tasks.id }
                    notifyDataSetChanged()
                    Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No", null)
            builder.show()
        }

        holder.userIcon.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Name: ${tasks.name}\nContact Number: ${tasks.details}")
                type = "text/plain"
            }

            activity.startActivity(Intent.createChooser(intent, "Share to:"))
        }

        holder.name.setOnClickListener {
            showContactToast(tasks.name, tasks.details)
        }

        holder.details.setOnClickListener {
            showContactToast(tasks.name, tasks.details)
        }
    }

    fun updateTaskList(newTaskList: List<ClassListModel>) {
        taskList = newTaskList
        notifyDataSetChanged()
    }

    private fun showContactToast(name: String, details: String) {
        val message = "Name: $name\nContact Number: $details"
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}