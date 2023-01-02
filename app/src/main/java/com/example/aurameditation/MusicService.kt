package com.example.aurameditation

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.LoudnessEnhancer
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

class MusicService: Service(), AudioManager.OnAudioFocusChangeListener {
    internal lateinit var player: MediaPlayer
    private var myBinder = MyBinder()
    var mediaPlayer:MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat
    private lateinit var runnable: Runnable
    lateinit var audioManager: AudioManager

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder: Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(playPauseBtn: Int){
        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val contentIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, flag)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, flag)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, flag)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)

//        val imgArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
//        val image = if(imgArt != null){
//            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
//        }else{
//            BitmapFactory.decodeResource(resources, R.drawable.ic_music)
//        }

        val notification = androidx.core.app.NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayerActivity.musicName[PlayerActivity.songPosition].title)
//            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.ic_music)
//            .setLargeIcon(R.drawable.ic_music)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previous_icon, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.next_icon, "Next", nextPendingIntent)
            .addAction(R.drawable.exit_icon, "Exit", exitPendingIntent)
            .build()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val playbackSpeed = if(PlayerActivity.isPlaying) 1F else 0F
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
                .build())
            val playBackState = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession.setPlaybackState(playBackState)
            mediaSession.setCallback(object: MediaSessionCompat.Callback(){

                //called when headphones buttons are pressed
                //currently only pause or play music on button click
                override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                    if(PlayerActivity.isPlaying){
                        //pause music
                        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
                        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
                        PlayerActivity.isPlaying = false
                        mediaPlayer!!.pause()
                        showNotification(R.drawable.ic_play)
                    }else{
                        //play music
                        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
                        NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
                        PlayerActivity.isPlaying = true
                        mediaPlayer!!.start()
                        showNotification(R.drawable.pause_icon)
                    }
                    return super.onMediaButtonEvent(mediaButtonEvent)
                }
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    mediaPlayer!!.seekTo(pos.toInt())
                    val playBackStateNew = PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, mediaPlayer!!.currentPosition.toLong(), playbackSpeed)
                        .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                        .build()
                    mediaSession.setPlaybackState(playBackStateNew)
                }
            })
        }

        startForeground(13, notification)
    }
    fun createMediaPlayer(){
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            mediaPlayer!!.prepare()
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
            showNotification(R.drawable.pause_icon)
            PlayerActivity.binding.tvSeekBarStart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.tvSeekBarEnd.text = formatDuration(mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.seekBarPA.progress = 0
            PlayerActivity.binding.seekBarPA.max = mediaPlayer!!.duration
//            PlayerActivity.nowPlayingId = PlayerActivity.musicListPA[PlayerActivity.songPosition].id
            PlayerActivity.loudnessEnhancer = LoudnessEnhancer(mediaPlayer!!.audioSessionId)
            PlayerActivity.loudnessEnhancer.enabled = true
        }catch (e: Exception){
            Log.d("TAG", "createMediaPlayer: "+e.message)
            return
        }
    }

    fun seekBarSetup(){
        runnable = Runnable {
            PlayerActivity.binding.tvSeekBarStart.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange <= 0){
            //pause music
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
            PlayerActivity.isPlaying = false
            mediaPlayer!!.pause()
            showNotification(R.drawable.ic_play)

        }
//        else{
//            //play music
//            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.pause_icon)
//            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon)
//            PlayerActivity.isPlaying = true
//            mediaPlayer!!.start()
//            showNotification(R.drawable.pause_icon)
//        }
    }

    //for making persistent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }














































//    override fun onBind(arg0: Intent): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        player = MediaPlayer()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val string = intent!!.getStringExtra("song")
//        createNotificarionChannel()
//        val intentNotificatoin = Intent(this, SongListActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 101, intentNotificatoin, 0)
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//        notification.setContentTitle("Forground Service")
//        notification.setContentText(string)
//        notification.setSmallIcon(R.drawable.ic_launcher_foreground)
//        notification.setContentIntent(pendingIntent)
//        startForeground(1, notification.build())
//
//        val afd = applicationContext.assets.openFd("mp3/"+string!!) as AssetFileDescriptor
//        try {
//            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
//        }catch (e: Exception){
//            Log.d("TAG", "onStartCommand: "+e.message)
//        }
//
//        player.prepare()
//        player.setVolume(100f, 100f)
//        player.start()
//        return START_NOT_STICKY
//    }
//
//    override fun onStart(intent: Intent, startId: Int) {
//        // TO DO
//    }
//
//    fun onUnBind(arg0: Intent): IBinder? {
//        // TO DO Auto-generated method
//        return null
//    }
//
//    fun onStop() {
//    }
//
//    fun onPause() {
//
//    }
//
//    override fun onDestroy() {
//        player.stop()
//        player.release()
//    }
//
//    override fun onLowMemory() {
//
//    }
//
//    companion object {
//        private val TAG: String? = null
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun createNotificarionChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.app_name)
//            val descriptionText = getString(R.string.app_name)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
//            mChannel.description = descriptionText
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(mChannel)
//        }
//    }
}