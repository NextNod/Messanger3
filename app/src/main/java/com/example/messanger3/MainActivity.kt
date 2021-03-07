package com.example.messanger3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import java.net.*
import java.sql.SQLData

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }

        val db = DataBase(this, null)
        if(db.isEmpty()) {
            setContentView(R.layout.activity_main)
            db.close()
        }
        else {
            Data.key = db.getKey()
            db.close()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    public fun onRegister(view: View)
    {
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    public fun onLogin(view :View)
    {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
