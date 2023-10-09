package vn.example.firebaseapp.login

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TwitterLogin(private val context: Activity, private val listener: ILoginState) {

    private var auth: FirebaseAuth = Firebase.auth
    private val urlTwitter = "twitter.com"

    fun login() {
        val provider = OAuthProvider.newBuilder(urlTwitter)
        provider.addCustomParameter("lang", "fr")

        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask.addOnSuccessListener {
                listener.onSuccess(it.user?.getIdToken(false)?.result?.token ?: "")
            }.addOnFailureListener {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            auth.startActivityForSignInWithProvider(context, provider.build())
                .addOnSuccessListener {
                    listener.onSuccess(it.user?.getIdToken(false)?.result?.token ?: "")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
