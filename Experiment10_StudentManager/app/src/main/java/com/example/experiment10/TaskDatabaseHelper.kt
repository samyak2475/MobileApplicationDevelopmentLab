
package com.example.experiment10

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class StudentTask(
    val id: Int,
    val title: String,
    val subject: String,
    val dueDate: String,
    val isCompleted: Boolean
)

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "student_tasks.db", null, 1) {

    companion object {
        private const val TABLE_TASKS = "tasks"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_SUBJECT = "subject"
        private const val COL_DUE_DATE = "due_date"
        private const val COL_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_TASKS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_SUBJECT TEXT NOT NULL,
                $COL_DUE_DATE TEXT NOT NULL,
                $COL_COMPLETED INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(title: String, subject: String, dueDate: String): Long {
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_SUBJECT, subject)
            put(COL_DUE_DATE, dueDate)
            put(COL_COMPLETED, 0)
        }
        return writableDatabase.insert(TABLE_TASKS, null, values)
    }

    fun getAllTasks(): List<StudentTask> {
        val tasks = mutableListOf<StudentTask>()
        val cursor = readableDatabase.query(
            TABLE_TASKS,
            null, null, null, null, null,
            "$COL_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                tasks.add(
                    StudentTask(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(COL_TITLE)),
                        subject = it.getString(it.getColumnIndexOrThrow(COL_SUBJECT)),
                        dueDate = it.getString(it.getColumnIndexOrThrow(COL_DUE_DATE)),
                        isCompleted = it.getInt(
                            it.getColumnIndexOrThrow(COL_COMPLETED)
                        ) == 1
                    )
                )
            }
        }
        return tasks
    }

    fun setTaskCompleted(id: Int, completed: Boolean): Int {
        val values = ContentValues().apply {
            put(COL_COMPLETED, if (completed) 1 else 0)
        }
        return writableDatabase.update(
            TABLE_TASKS,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteTask(id: Int): Int {
        return writableDatabase.delete(
            TABLE_TASKS,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getTotalCount(): Int {
        readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_TASKS",
            null
        ).use {
            return if (it.moveToFirst()) it.getInt(0) else 0
        }
    }

    fun getPendingCount(): Int {
        readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_TASKS WHERE $COL_COMPLETED = 0",
            null
        ).use {
            return if (it.moveToFirst()) it.getInt(0) else 0
        }
    }
}
