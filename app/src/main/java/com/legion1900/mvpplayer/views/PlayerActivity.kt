package com.legion1900.mvpplayer.views

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.PlayerService
import com.legion1900.mvpplayer.models.data.Song
import com.legion1900.mvpplayer.models.SongStateRepository
import com.legion1900.mvpplayer.presenters.PlayerPresenter

class PlayerActivity : AppCompatActivity(), PlayerContract.PlayerView {

    companion object {
        const val KEY_SONG = "song"
        const val KEY_MUSICIAN = "musician"
        const val KEY_GENRE = "genre"

        const val ACTION_SONG_SENT = "Song sent"
        const val SONG_PATH_KEY = "path"
    }

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


    private lateinit var presenter: PlayerContract.PlayerPresenter

    private lateinit var playerIntent: Intent
    private val connection = object : ServiceConnection {
        var bound = false
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bound = true
            presenter.player = (service as PlayerService.PlayerBinder).getPlayer()
        }

        /*
        * Only gets called when process hosting service crashed or been killed!!
        * */
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val song = intent.getParcelableExtra<Song>(SONG_PATH_KEY)
            presenter.onSongChanged(song)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initViews(savedInstanceState)

        playerIntent = Intent(this, PlayerService::class.java)

        presenter = PlayerPresenter.getPresenter(this)
    }

    private fun initViews(state: Bundle?) {
        tvSong = findViewById(R.id.tv_song)
        tvMusician = findViewById(R.id.tv_musician)
        tvGenre = findViewById(R.id.tv_genre)
        state?.run {
            song = getCharSequence(KEY_SONG)!!
            musician = getCharSequence(KEY_MUSICIAN)!!
            genre = getCharSequence(KEY_GENRE)!!
        }
    }

    override fun onStart() {
        super.onStart()
        if (!connection.bound)
            bindService(playerIntent, connection, 0)
    }

    override fun onStop() {
        super.onStop()
        if (connection.bound) {
            unbindService(connection)
            connection.bound = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_SONG, song)
        outState.putCharSequence(KEY_MUSICIAN, musician)
        outState.putCharSequence(KEY_GENRE, genre)
    }

    override fun getRepository(): PlayerContract.SongStateRepository {
        val repo = getSharedPreferences(PlayerContract.REPO_KEY, Context.MODE_PRIVATE)
        return SongStateRepository(repo)
    }

    override fun initPlatform(song: PlayerContract.ModelSong) {
        initService(song)
        initReceiver()
    }

    private fun initService(song: PlayerContract.ModelSong) {
        playerIntent.putExtra(PlayerContract.EXTRA_SONG, song)
        startService(playerIntent)
    }

    private fun initReceiver() {
        val filter = IntentFilter(ACTION_SONG_SENT)
        registerReceiver(receiver, filter)
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
        val intent = Intent(this, ChooserActivity::class.java)
        startActivity(intent)
    }
}
