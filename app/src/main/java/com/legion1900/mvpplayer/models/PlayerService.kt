package com.legion1900.mvpplayer.models

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.platform.utils.ServiceNotificationHelper
import com.legion1900.mvpplayer.views.PlayerActivity
import java.util.concurrent.Executors

class PlayerService : Service(), PlayerContract.ModelPlayer {

    override var song: PlayerContract.ModelSong? = null
        set(value) {
            isPlaying = false
            field = value
            player.reset()
            field?.run {
                player.setDataSource(this@PlayerService, Uri.parse(path))
                notificationHelper.updateNotification(name, musician)
            }
            preparePlayer()
        }

    private var isPlaying: Boolean = false

    private val player = MediaPlayer()
    private val executor = Executors.newSingleThreadExecutor()
    private val preparePlayer = Runnable {
        player.prepare()
        player.seekTo(song!!.time)
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
            player.pause()
            isPlaying = false
            song?.time = player.currentPosition
        }
    }

    override fun stop() {
        player.stop()
        isPlaying = false
        song?.time = 0
        preparePlayer()
    }

    private fun preparePlayer() {
        executor.submit(preparePlayer)
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class PlayerBinder : Binder() {
        fun getPlayer(): PlayerContract.ModelPlayer = this@PlayerService
    }

    private companion object {
        const val CHANNEL_NAME = "Player"
        const val CHANNEL_ID = "com.legion1900.mvpplayer"
        const val SERVICE_ID = 42
    }
}
