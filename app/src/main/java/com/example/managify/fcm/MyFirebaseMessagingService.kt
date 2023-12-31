package com.example.managify.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.managify.R
import com.example.managify.activities.MainActivity
import com.example.managify.activities.SignInActivity
import com.example.managify.firebase.FirestoreHandler
import com.example.managify.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMeesage: RemoteMessage) {
        super.onMessageReceived(remoteMeesage)

        Log.d(TAG, "FROM: ${remoteMeesage.from}")

        remoteMeesage.data.isNotEmpty().let {
            Log.d(TAG, "Message dada Payload: ${remoteMeesage.data}")
            val title = remoteMeesage.data[Constants.FCM_KEY_TITLE]!!
            val message = remoteMeesage.data[Constants.FCM_KEY_MESSAGE]!!

            sendNotification(title,message)
        }

        remoteMeesage.notification?.let {
            Log.d(TAG, "Message notification body : ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "Refreshed token : $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?){
        // Here we have saved the token in the Shared Preferences
        val sharedPreferences =
            this.getSharedPreferences(Constants.POJECTMANAGER_PREFERENCES, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.FCM_TOKEN, token)
        editor.apply()
    }

    private fun sendNotification(title : String, message : String){

        val intent = if(FirestoreHandler().getCurrentUserId().isNotEmpty()){
            Intent(this, MainActivity::class.java) //if the user already logged in
        }else{
            Intent(this, SignInActivity::class.java) //the user logged out
        }
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "project_manager_notification_channel_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat
            .Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Channel ProjectManager", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "FCM"
    }
}