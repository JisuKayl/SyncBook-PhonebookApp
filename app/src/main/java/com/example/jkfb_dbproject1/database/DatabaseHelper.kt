package com.example.jkfb_dbproject1.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.jkfb_dbproject1.model.ClassListModel

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private val DB_NAME = "task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "taskList"
        private val ID = "id"
        private val TASK_NAME = "taskname"
        private val TASK_DETAILS = "taskdetails"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT);"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    fun getAllTask() : List<ClassListModel>{
        val tasklist = ArrayList<ClassListModel>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    val tasks = ClassListModel()

                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndexOrThrow(TASK_NAME))
                    tasks.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))

                    tasklist.add(tasks)

                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return tasklist
    }

    fun addTask (tasks : ClassListModel):Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)

        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun deleteTask(id: Int): Boolean {
        val db = writableDatabase
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString()))
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun updateTask(tasks: ClassListModel): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tasks.id.toString()))
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun getTaskById(id: Int): ClassListModel? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(query, null)

        var task: ClassListModel? = null
        if (cursor.moveToFirst()) {
            task = ClassListModel()
            task.id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
            task.name = cursor.getString(cursor.getColumnIndexOrThrow(TASK_NAME))
            task.details = cursor.getString(cursor.getColumnIndexOrThrow(TASK_DETAILS))
        }
        cursor.close()
        return task
    }
}

