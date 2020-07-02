package ua.com.pavgacor.awesomeapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class UsersData (
    context: Context
) {
    init {
        mContext = context



    }

    fun getData(): String {
        val dbHelper: UserDataBaseHelper = UserDataBaseHelper(mContext)
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Users", arrayOf("NAME"),

            null, null, null, null, null
        )
         if (cursor.moveToFirst()) {
             mDataUser?.set(0, cursor.getString(0))
             cursor.moveToNext()
             cursor.close()
         }
        dbHelper.close()
        return mDataUser?.get(0).toString()
    }
    fun getData(field: String): String {
        val dbHelper: UserDataBaseHelper = UserDataBaseHelper(mContext)
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Users", arrayOf(field),

            null, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            mDataUser?.set(0, cursor.getString(0))
            cursor.moveToNext()
            cursor.close()
        }
        dbHelper.close()
        return mDataUser?.get(0).toString()
    }

    fun getData(idUser: String, boolean: Boolean): Array<String?>? {
        val dbHelper: UserDataBaseHelper = UserDataBaseHelper(mContext)
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Users", arrayOf("NAME", "EMAIL", "PASS"),

            idUser, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            mDataUser?.set(0, cursor.getString(0))
            cursor.moveToNext()
            cursor.close()
        }
        dbHelper.close()
        return mDataUser
    }


    fun putData(name: String){
        val dbHelper: UserDataBaseHelper = UserDataBaseHelper(mContext)
        val db = dbHelper.writableDatabase
        var contentValues: ContentValues = ContentValues()
        contentValues.put("NAME", name)
        db.insert("Users", null, contentValues)
        dbHelper.close()
    }

    fun putData(name: String, email: String, id: String){
        val dbHelper: UserDataBaseHelper = UserDataBaseHelper(mContext)
        val db = dbHelper.writableDatabase
        var contentValues: ContentValues = ContentValues()
        contentValues.put("NAME", name)
        contentValues.put("EMAIL", email)
        contentValues.put("IDUser", id)
        db.insert("Users", null, contentValues)
        dbHelper.close()
    }


    companion object{
        lateinit var mContext:Context

        private var mDataUser: Array<String?>? = arrayOfNulls(5)
    }

}