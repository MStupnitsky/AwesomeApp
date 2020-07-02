package ua.com.pavgacor.awesomeapp.ui.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import ua.com.pavgacor.awesomeapp.R
import ua.com.pavgacor.awesomeapp.helper.UserVerification
import ua.com.pavgacor.awesomeapp.helper.UsersData
import ua.com.pavgacor.awesomeapp.home.HomeActivity


class LoginActivity : AppCompatActivity() {


    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mCallbackManager : CallbackManager //Facebook

    private var userName = ""
    private var email = ""
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // FacebookSdk.sdkInitialize(applicationContext);
        setContentView(R.layout.activity_login)



        var username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            startHomeActivity()
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        /**Facebook*/
        mCallbackManager = CallbackManager.Factory.create()
        val loginFacebookButton = findViewById<LoginButton>(R.id.connectWithFbButton)
        loginFacebookButton.setReadPermissions("email")
        // Callback registration
        loginFacebookButton.registerCallback(
            mCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    Log.i("Facebook success", "complete")
                    val accessToken = loginResult.accessToken
                    val request =
                        GraphRequest.newMeRequest(
                            accessToken
                        ) { user, _ ->
                            LoginManager.getInstance().logOut()
                            userName = user.optString("name")
                            email = user.optString("email")
                            id = user.optString("id")
                            LoggedInUserView(userName)
                            Log.i("FB", "get name: $username")
                            Log.i("FB", "get email: $email")
                            Log.i("FB", "get id: $id")
                            //putData(username, email)

                            checkLogin()
                        }.executeAsync()
                    // App code
                    Toast.makeText(
                        applicationContext,
                        "Login Success with Facebook",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                override fun onCancel() {
                    // App code
                }
                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    private fun checkLogin() {
        val verification: UserVerification = UserVerification(this@LoginActivity.applicationContext)
        if(verification.checkUserID(id)) {

            startHomeActivity()
        } else{
            val usersData: UsersData = UsersData(this@LoginActivity.applicationContext)
            usersData.putData(userName, email, id)
            startRegistrationActivity(id)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        Log.i("onActivityResult", "complete")

    }

    /*fun putData(name: String, email: String){
        val userData: UsersData =
            UsersData(this.applicationContext)
        userData.putData(name)
    }*/

    private fun startHomeActivity(){
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }

    private fun startRegistrationActivity(idUser: String){
        var intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
        intent.putExtra("ID_USER", idUser)
        startActivity(intent)
        finish()
    }

}



/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}