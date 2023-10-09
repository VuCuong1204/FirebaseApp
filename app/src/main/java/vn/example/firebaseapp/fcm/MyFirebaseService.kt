package vn.example.firebaseapp.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import vn.example.firebaseapp.MainActivity
import vn.example.firebaseapp.R

class MyFirebaseService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMsgService"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: " + message.from);

        // Kiểm tra xem thông báo có dữ liệu không
        if (message.data.isNotEmpty()) {
            val title = message.data["title"]
            val content = message.data["content"]
            val icon = message.data["icon"]
            Log.d(TAG, "Message data payload: + $title $content $icon ");
        }

        // Kiểm tra xem thông báo có thông điệp không
        Log.d(TAG, "Message Notification Body: " + message.notification!!.body);
        // Hiển thị thông báo đẩy trên màn hình khi nhận được
        if (message.notification!!.title != null && message.notification!!.body != null) {
            sendNotification(message.notification?.title!!, message.notification?.body!!)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token");

        sendRegistrationToServer(token);
    }

    private fun sendRegistrationToServer(token: String) {}

    private fun sendNotification(messageTitle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)
        val channelId = "Firebase App"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .addAction(NotificationCompat.Action(
                R.drawable.ic_launcher_background,
                "Cancel",
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)))
            .addAction(NotificationCompat.Action(
                R.drawable.ic_launcher_background,
                "OK",
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
