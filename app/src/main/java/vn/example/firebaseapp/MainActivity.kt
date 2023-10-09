package vn.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.example.firebaseapp.fcm.FcmActivity
import vn.example.firebaseapp.login.LoginActivity
import vn.example.firebaseapp.performance.PerformanceActivity
import vn.example.firebaseapp.realtime.AppConfig
import vn.example.firebaseapp.realtime.ChatActivity
import vn.example.firebaseapp.remoteconfig.RemoteConfigActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnSub1: Button
    private lateinit var btnSub2: Button
    private lateinit var btnSub3: Button
    private lateinit var btnSub4: Button
    private lateinit var btnSub5: Button
    private lateinit var btnSub6: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        btnSub1 = findViewById(R.id.btnMainSub1)
        btnSub2 = findViewById(R.id.btnMainSub2)
        btnSub3 = findViewById(R.id.btnMainSub3)
        btnSub4 = findViewById(R.id.btnMainSub4)
        btnSub5 = findViewById(R.id.btnMainSub5)
        btnSub6 = findViewById(R.id.btnMainSub6)

        btnSub1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnSub2.setOnClickListener {
            startActivity(Intent(this, RemoteConfigActivity::class.java))
        }

        btnSub3.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        btnSub4.setOnClickListener {
            if (AppConfig.userId == null) {
                Toast.makeText(this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }

        btnSub5.setOnClickListener {
            startActivity(Intent(this, PerformanceActivity::class.java))
        }

        btnSub6.setOnClickListener {
            startActivity(Intent(this, FcmActivity::class.java))
        }
    }
}
