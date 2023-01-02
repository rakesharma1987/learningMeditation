package com.example.aurameditation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData

private const val CHANNEL_ID = "ForegroundServiceChannel"
class BackgroundSoundService: Service() {
    internal lateinit var player: MediaPlayer
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val string = intent!!.getStringExtra("song")
        createNotificarionChannel()
        val intentNotificatoin = Intent(this, SongListActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 101, intentNotificatoin, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
        notification.setContentTitle("Forground Service")
        notification.setContentText(string)
        notification.setSmallIcon(R.drawable.ic_launcher_foreground)
        notification.setContentIntent(pendingIntent)
        startForeground(1, notification.build())

        val afd = applicationContext.assets.openFd("mp3/"+string!!) as AssetFileDescriptor
        try {
            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        }catch (e: Exception){
            Log.d("TAG", "onStartCommand: "+e.message)
        }

        player.prepare()
        player.setVolume(100f, 100f)
        player.start()
        return START_NOT_STICKY
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {
    }

    fun onPause() {

    }

    override fun onDestroy() {
        player.stop()
        player.release()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificarionChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}