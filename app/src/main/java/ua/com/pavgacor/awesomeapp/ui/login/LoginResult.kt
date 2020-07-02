package ua.com.pavgacor.awesomeapp.ui.login

import com.facebook.AccessToken

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: LoggedInUserView? = null,
        val error: Int? = null


)