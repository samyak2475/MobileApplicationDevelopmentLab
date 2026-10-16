package com.example.experiment03

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "StudentDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "students"

        private const val COLUMN_ROLL =
            "roll_no"

        private const val COLUMN_NAME =
            "name"

        private const val COLUMN_BRANCH =
            "branch"

        private const val COLUMN_MARKS =
            "marks"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ROLL INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_BRANCH TEXT NOT NULL,
                $COLUMN_MARKS REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    // ADD STUDENT
    fun addStudent(
        roll: Int,
        name: String,
        branch: String,
        marks: Double
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COLUMN_ROLL, roll)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_BRANCH, branch)
        values.put(COLUMN_MARKS, marks)

        val result = db.insert(
            TABLE_NAME,
            null,
            values
        )

        return result != -1L
    }

    // UPDATE STUDENT
    fun updateStudent(
        roll: Int,
        name: String,
        branch: String,
        marks: Double
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COLUMN_NAME, name)
        values.put(COLUMN_BRANCH, branch)
        values.put(COLUMN_MARKS, marks)

        val result = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ROLL = ?",
            arrayOf(roll.toString())
        )

        return result > 0
    }

    // DELETE STUDENT
    fun deleteStudent(roll: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "$COLUMN_ROLL = ?",
            arrayOf(roll.toString())
        )

        return result > 0
    }

    // VIEW STUDENTS
    fun getAllStudents(): String {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ROLL",
            null
        )

        val result = StringBuilder()

        if (cursor.count == 0) {

            result.append("No student records found.")

        } else {

            while (cursor.moveToNext()) {

                val roll =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_ROLL
                        )
                    )

                val name =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_NAME
                        )
                    )

                val branch =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_BRANCH
                        )
                    )

                val marks =
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_MARKS
                        )
                    )

                result.append(
                    "Roll No: $roll\n" +
                            "Name: $name\n" +
                            "Branch: $branch\n" +
                            "Marks: $marks\n\n"
                )
            }
        }

        cursor.close()

        return result.toString()
    }
}