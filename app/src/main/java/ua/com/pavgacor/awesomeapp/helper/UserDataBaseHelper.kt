package ua.com.pavgacor.awesomeapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDataBaseHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(
        context,
        DB_NAME,
        null,
        DB_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.i("MyLog", "In onCreate Data Base")
        db.execSQL(
            "CREATE TABLE Users("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "PASS, "
                    + "NAME, "
                    + "EMAIL, "
                    + "IDUser);"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {}

    companion object {
        private const val DB_NAME = "userDataNEPI" // Имя базы данных
        private const val DB_VERSION = 1 // Версия базы данных

        private fun updateRecord() {}

        private fun insertRecord(
            db: SQLiteDatabase,
            name: String,
            email: String,
            pass: String
        ) {
            val userData = ContentValues()
            userData.put("PASS", pass)
            userData.put("NAME", name)
            userData.put("EMAIL", email)
            db.insert("RECORDS", null, userData)
        }
    }
}
