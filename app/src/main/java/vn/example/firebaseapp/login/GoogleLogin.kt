package vn.example.firebaseapp.login

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleLogin(context: Context, val listener: ILoginState) {

    private var mGoogleSignInClient: GoogleSignInClient
    private var logInContract: ActivityResultContract<Unit, GoogleSignInAccount?>
    private lateinit var logInLauncher: ActivityResultLauncher<Unit>

    init {
        val googleClientId = "574047223826-ltihgmou5toq0s5jkr5lm91lg6bg9ud9.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleClientId)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        logInContract = object : ActivityResultContract<Unit, GoogleSignInAccount?>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return mGoogleSignInClient.signInIntent
            }

            override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
                var account: GoogleSignInAccount? = null
                if (intent != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    account = task.getResult(ApiException::class.java)
                }
                return account
            }
        }
    }

    fun login() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener {
                logInLauncher.launch(Unit)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun register(caller: ActivityResultCaller) {
        logInLauncher = caller.registerForActivityResult(logInContract) {
            it?.let {
                listener.onSuccess(it.idToken!!)
            }
        }
    }
}
