package vn.example.firebaseapp.remoteconfig

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import vn.example.firebaseapp.R

class RemoteConfigActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var btnConfirm: Button
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.remote_config_activity)

        tvName = findViewById(R.id.tvRemoteName)
        btnConfirm = findViewById(R.id.btnRemoteClick)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
            )
            setDefaultsAsync(R.xml.remote_config_defaults)
        }

        tvName.setTextColor(Color.parseColor(firebaseRemoteConfig.getString("text_color")))
        tvName.textSize = firebaseRemoteConfig.getValue("text_size").asDouble().toFloat()
        tvName.text = firebaseRemoteConfig.getString("text_str")

        btnConfirm.setOnClickListener {
            firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("TAG", "Config params updated: $updated")
                        Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()
                        tvName.setTextColor(Color.parseColor(firebaseRemoteConfig.getString("text_color")))
                        tvName.textSize = firebaseRemoteConfig.getValue("text_size").asDouble().toFloat()
                        tvName.text = firebaseRemoteConfig.getString("text_str")
                    } else {
                        Toast.makeText(this, "Fetch failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
