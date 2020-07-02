package ua.com.pavgacor.awesomeapp.helper

import android.content.Context
import android.util.Log

class UserVerification (
    _context: Context
){
    init {
        context = _context
    }

    fun checkUserID(id: String): Boolean{
        var usersData: UsersData = UsersData(context)
        var idHas =usersData.getData("IDUser")
        Log.d("checkUserID", "has:$idHas log:$id")
        if(idHas== id)
            return true
        return false
    }

    companion object{
        lateinit var context: Context
    }

}