package vn.example.firebaseapp.login

import android.content.Context
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import vn.example.firebaseapp.R

class FacebookLogin(val context: Context, val listener: ILoginState) {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val loginListener = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult) {
            listener.onSuccess(result.accessToken.token)
        }

        override fun onError(error: FacebookException) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
        }
    }
    private var loginContract: LoginManager.FacebookLoginActivityResultContract
    private var loginLauncher: ActivityResultLauncher<Collection<String>>? = null

    init {
        registerCallbackLoginFacebook()
        loginContract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    }

    fun register(caller: ActivityResultCaller) {
        loginLauncher = caller.registerForActivityResult(loginContract) { }
    }

    fun login() {
        loginLauncher?.launch(listOf("public_profile"))
    }

    private fun registerCallbackLoginFacebook() {
        val facebookAppId = context.getString(R.string.facebook_app_id)
        val facebookClientId = context.getString(R.string.facebook_client_token)
        if (facebookAppId.isNotEmpty() && facebookClientId.isNotEmpty()) {
            FacebookSdk.setApplicationId(facebookAppId)
            FacebookSdk.setClientToken(facebookClientId)
        }
        FacebookSdk.sdkInitialize(context)
        LoginManager.getInstance().registerCallback(callbackManager, loginListener)
    }
}
