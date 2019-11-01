package com.legion1900.mvpplayer.models

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.utils.ServiceNotificationHelper
import com.legion1900.mvpplayer.views.PlayerActivity
import java.util.concurrent.Executors

private const val CHANNEL_NAME = "Player"
private const val CHANNEL_ID = "com.legion1900.mvpplayer"
private const val SERVICE_ID = 42

class PlayerService : Service(), PlayerContract.ModelPlayer {


//    TODO: player stops on rotation because of this setter (it is called on activity creation)
    override var song: PlayerContract.ModelSong? = null
        set(value) {
            isPlaying = false
            field = value
            player.reset()
            player.setDataSource(this, Uri.parse(song?.path))
            song?.run { notificationHelper.updateNotification(name, musician) }
            preparePlayer()
        }

    private var isPlaying: Boolean = false

    private val player = MediaPlayer()
    private val executor = Executors.newSingleThreadExecutor()
    private val preparePlayer = Runnable {
        player.prepare()
    }

    private lateinit var notificationHelper: ServiceNotificationHelper
    private lateinit var pendingIntent: PendingIntent
    private val binder = PlayerBinder()

    override fun onCreate() {
        pendingIntent = buildPendingIntent()
        notificationHelper = ServiceNotificationHelper(
            this,
            SERVICE_ID,
            CHANNEL_ID,
            CHANNEL_NAME,
            R.drawable.ic_player_24dp,
            pendingIntent
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Test", "onStartCommand call")
        intent?.run {
            song = getParcelableExtra(PlayerContract.EXTRA_SONG)
        }
        song?.run {
            val notification = notificationHelper.buildNotification(name, musician)
            startForeground(SERVICE_ID, notification)
        }
        return START_STICKY
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(this, PlayerActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    override fun play() {
        isPlaying = true
        player.start()
    }

    override fun pause() {
        if (isPlaying) {
            isPlaying = false
            player.pause()
        }
    }

    override fun stop() {
        player.stop()
        preparePlayer()
    }

    private fun preparePlayer() {
        executor.submit(preparePlayer)
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class PlayerBinder : Binder() {
        fun getPlayer(): PlayerContract.ModelPlayer = this@PlayerService
    }
}
