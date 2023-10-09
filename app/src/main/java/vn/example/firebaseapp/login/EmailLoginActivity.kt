package vn.example.firebaseapp.login

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.example.firebaseapp.R
import vn.example.firebaseapp.login.instagram.InstagramLogin
import vn.example.firebaseapp.realtime.AppConfig

class EmailLoginActivity : AppCompatActivity() {

    private lateinit var edtAccount: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    private lateinit var auth: FirebaseAuth
    private val TAG = EmailLoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_login_activity)

        edtAccount = findViewById(R.id.edtEmailLoginAccount)
        edtPassword = findViewById(R.id.edtEmailLoginPassword)
        btnRegister = findViewById(R.id.btnEmailLoginRegister)
        btnLogin = findViewById(R.id.btnEmailLogin)

        auth = Firebase.auth

        btnRegister.setOnClickListener {
            val email = edtAccount.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            register(email, password)
        }

        btnLogin.setOnClickListener {
            val email = edtAccount.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            login(email, password)
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success ${user?.getIdToken(false)?.result?.token}")
                    Toast.makeText(
                        baseContext,
                        "Authentication success.${user?.getIdToken(false)?.result?.token}",
                        Toast.LENGTH_SHORT,
                    ).show()
                    AppConfig.userId = user?.uid
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        Log.d(TAG, "signInWithEmail:success ${user.getIdToken(false).result?.token}")
                        Toast.makeText(
                            baseContext,
                            "Authentication failed. ${user.getIdToken(false).result?.token}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Vui lòng xác nhận trong email",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}
