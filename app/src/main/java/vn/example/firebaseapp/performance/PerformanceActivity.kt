package vn.example.firebaseapp.performance

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.FirebaseApp
import com.google.firebase.perf.FirebasePerformance
import vn.example.firebaseapp.R

class PerformanceActivity : AppCompatActivity() {

    private lateinit var btnConfirm: Button
    private lateinit var ivAvatar: ImageView
    private val urlImage = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.performance_activity)

        btnConfirm = findViewById(R.id.btnPerformanceConfirm)
        ivAvatar = findViewById(R.id.ivPerformanceAvatar)

        FirebaseApp.initializeApp(this)
        val firebasePerformance = FirebasePerformance.getInstance()
        val trace = firebasePerformance.newTrace("my_trace")
        trace.start()

        btnConfirm.setOnClickListener {

            Glide.with(this)
                .load(urlImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        trace.stop()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        trace.stop()
                        return false
                    }
                })
                .into(ivAvatar)
        }
    }
}
