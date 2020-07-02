package ua.com.pavgacor.awesomeapp.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import ua.com.pavgacor.awesomeapp.R
import ua.com.pavgacor.awesomeapp.helper.UsersData

class HomeActivity : AppCompatActivity() {

    private lateinit var mIVAvatars:    ImageView
    private lateinit var mTVName:       TextView
    private lateinit var mTVEmail:      TextView
    private lateinit var mTVPass:       TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initializationUI()
        setDataUI()
    }

    private fun initializationUI(){
        mIVAvatars = findViewById(R.id.iVAvatars)
        mTVName = findViewById(R.id.tVName)
        mTVEmail = findViewById(R.id.tVEmail)
        mTVPass = findViewById(R.id.tVPass)
    }

    private fun setDataUI() {
        val lDS =
            UsersData(this.applicationContext)
        mTVName.text = lDS.getData().toString()
    }

}