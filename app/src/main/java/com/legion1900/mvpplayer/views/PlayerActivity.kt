package com.legion1900.mvpplayer.views

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.PlayerService
import com.legion1900.mvpplayer.models.StateRepository
import com.legion1900.mvpplayer.presenters.PlayerPresenter

class PlayerActivity : AppCompatActivity(), PlayerContract.View {
    override var song: CharSequence
        get() = tvSong.text
        set(value) {
            tvSong.text = value
        }

    override var musician: CharSequence
        get() = tvMusician.text
        set(value) {
            tvMusician.text = value
        }
    override var genre: CharSequence
        get() = tvGenre.text
        set(value) {
            tvGenre.text = value
        }

    private lateinit var tvSong: TextView
    private lateinit var tvMusician: TextView
    private lateinit var tvGenre: TextView


    private lateinit var presenter: PlayerContract.Presenter

    private lateinit var playerIntent: Intent
    private val connection = object : ServiceConnection {
        var bound = false
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bound = true
            presenter.player = (service as PlayerService.PlayerBinder).getPlayer()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

//    TODO: implement BroadcastReceiver here (anonymously)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        tvSong = findViewById(R.id.tv_song)
        tvMusician = findViewById(R.id.tv_musician)
        tvGenre = findViewById(R.id.tv_genre)

        playerIntent = Intent(this, PlayerService::class.java)

        presenter = PlayerPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        if (!connection.bound)
            bindService(playerIntent, connection, 0)
    }

    override fun onStop() {
        super.onStop()
        if (connection.bound)
            unbindService(connection)
    }

    override fun getRepository(): PlayerContract.Repository {
        return StateRepository(getSharedPreferences(PlayerContract.REPO_KEY, Context.MODE_PRIVATE))
    }

    override fun initPlayer(song: PlayerContract.ModelSong) {
        playerIntent.putExtra(PlayerContract.EXTRA_SONG, song)
        startService(playerIntent)
    }

    fun onPlayClick(view: View) {
        presenter.onPlayBtnClick()
    }

    fun onPauseClick(view: View) {
        presenter.onPauseBtnClick()
    }

    fun onStopClick(view: View) {
        presenter.onStopBtnClick()
    }

    fun onChooseSongClick(view: View) {
        chooseSong()
    }

    override fun chooseSong() {
        TODO("Start broadcast receiver and chooser activity.")
    }
}
