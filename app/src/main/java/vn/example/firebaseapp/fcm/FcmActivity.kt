package vn.example.firebaseapp.fcm

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import vn.example.firebaseapp.R

class FcmActivity : AppCompatActivity() {

    private lateinit var btnConfirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fcm_activity)

        btnConfirm = findViewById(R.id.btnFcmConfirm)

        getFcmToken()
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "getFcmToken: ${task.result}")
                return@OnCompleteListener
            }
            Log.w("TAG", "Fetching FCM registration token failed", task.exception)
        })
    }
}
