package vn.example.firebaseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.example.firebaseapp.R
import vn.example.firebaseapp.login.instagram.InstagramLogin

class LoginActivity : AppCompatActivity() {

    private lateinit var btnEmail: Button
    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button
    private lateinit var btnTwitter: Button
    private lateinit var btnInstagram: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var googleLogin: GoogleLogin
    private lateinit var facebookLogin: FacebookLogin
    private lateinit var instagramLogin: InstagramLogin

    private val urlTwitter = "twitter.com"

    private val TAG = LoginActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        auth = Firebase.auth

        btnEmail = findViewById(R.id.btnLoginEmail)
        btnGoogle = findViewById(R.id.btnLoginGoogle)
        btnFacebook = findViewById(R.id.btnLoginFacebook)
        btnTwitter = findViewById(R.id.btnLoginTwitter)
        btnInstagram = findViewById(R.id.btnLoginInstagram)

        initView()

        btnEmail.setOnClickListener {
            startActivity(Intent(this, EmailLoginActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            loginGoogle()
        }

        btnFacebook.setOnClickListener {
            loginFacebook()
        }

        btnTwitter.setOnClickListener {
            loginTwitter()
        }

        btnInstagram.setOnClickListener {
            loginInstagram()
        }

        intent.dataString?.let {
            parseCode(it)
        }
    }

    private fun initView() {
        googleLogin = GoogleLogin(this, object : ILoginState {
            override fun onSuccess(token: String) {
                val credential = GoogleAuthProvider.getCredential(token, null)
                signInFirebase(credential)
            }
        }).also {
            it.register(this)
        }

        facebookLogin = FacebookLogin(this, object : ILoginState {
            override fun onSuccess(token: String) {
                val credential = FacebookAuthProvider.getCredential(token)
                signInFirebase(credential)
            }
        }).also {
            it.register(this)
        }

        instagramLogin = InstagramLogin(this, object : ILoginState {
            override fun onSuccessInfo(id: String?, username: String?) {
                Toast.makeText(this@LoginActivity, "success: $id $username", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onResponse:  $id $username}")
            }
        })
    }

    private fun loginGoogle() {
        googleLogin.login()
    }

    private fun loginFacebook() {
        facebookLogin.login()
    }

    private fun loginTwitter() {
        val provider = OAuthProvider.newBuilder(urlTwitter)
        provider.addCustomParameter("lang", "fr")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask.addOnSuccessListener {
                Toast.makeText(this, "login success ${it.user?.getIdToken(false)?.result?.token}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "login success: ${it.user?.getIdToken(false)?.result?.token}")
            }.addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    Toast.makeText(this, "login success ${it.user?.getIdToken(false)?.result?.token}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loginInstagram() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramLogin.authorizationUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            this.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signInFirebase(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener { _ ->
                Firebase.auth.currentUser?.getIdToken(false)?.result?.token?.let {
                    Log.d(TAG, "onSuccess: $it")
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun parseCode(dataUri: String): String? {
        val uri = Uri.parse(dataUri)
        return uri?.getQueryParameter("code")?.also {
            instagramLogin.exchangeAuthorizationCodeForToken(it)
        }
    }
}
