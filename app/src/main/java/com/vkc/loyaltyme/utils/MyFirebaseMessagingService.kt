package com.vkc.loyaltyme.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.manager.PreferenceManager
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val tag = "MyFirebaseMessagingService"
    var bitmap: Bitmap? = null
    private val channelID = getString(R.string.app_name) + "_01"
    val name: CharSequence = getString(R.string.app_name)


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()){
            val json = JSONObject(remoteMessage.data.toString().replace("=".toRegex(), ":"))
            handleDataMessage(json)
        }
        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification !!.body.toString())
            Log.d(tag, "Message Notification Body: " + remoteMessage.notification !!.body)

        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(message: String) {
        val random = Random()
        val randomNumber = random.nextInt(100)
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,channelID)
            .setSmallIcon(R.drawable.notifi_dubai)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.appicon_dubai)
        notificationBuilder.color = getColor(R.color.caramine_pink)
        notificationBuilder.setLargeIcon(largeIcon)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, name, importance)
            notificationBuilder.setChannelId(channel.id)
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = getColor(R.color.caramine_pink)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(randomNumber, notificationBuilder.build())
        } else {
            notificationManager.notify(randomNumber, notificationBuilder.build())
        }
    }
    private fun handleDataMessage(json: JSONObject) {
        val data = json.getJSONObject("body")
        val message = data.optString("message")
        val title = data.optString("title")
        val image = data.optString("image")
        if (image.isNotEmpty()) {
            bitmap = bitmapFromUrl(image)
        }

        sendNotification(message)
    }

    private fun bitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        PreferenceManager.setToken(applicationContext, p0)
    }

}