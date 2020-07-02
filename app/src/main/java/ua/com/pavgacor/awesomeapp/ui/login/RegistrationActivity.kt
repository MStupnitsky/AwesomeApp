package ua.com.pavgacor.awesomeapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ua.com.pavgacor.awesomeapp.R
import ua.com.pavgacor.awesomeapp.helper.UsersData
import ua.com.pavgacor.awesomeapp.home.HomeActivity

class RegistrationActivity : AppCompatActivity() {


    lateinit var mETName: EditText
    lateinit var mETEmail: EditText
    lateinit var mETPass: EditText
    lateinit var mBRegister: Button
    lateinit var mIDUser: String
    lateinit var mUsersData: UsersData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val arguments = intent.extras
        mIDUser = arguments!!["ID_USER"].toString()
        initializationUI()
        setUserData()
        mBRegister.setOnClickListener {
            saveUserData()
            startActivityHome()
        }
    }

    private fun initializationUI() {
        mUsersData = UsersData(this.applicationContext)
        mBRegister = findViewById(R.id.bRegister)
        mETName = findViewById(R.id.eTName)
        mETEmail = findViewById(R.id.eTEmail)
    }

    private fun setUserData() {
        mETName.setText(mUsersData.getData(mIDUser, true)?.get(0).toString())
    }

    private fun saveUserData() {

        mUsersData.putData(mETName.text.toString())
    }

    private fun startActivityHome() {
        startActivity(Intent(this@RegistrationActivity, HomeActivity::class.java))
        finish()
    }


}