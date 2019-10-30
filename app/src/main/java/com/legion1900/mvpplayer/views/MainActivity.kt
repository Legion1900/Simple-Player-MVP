package com.legion1900.mvpplayer.views

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.presenters.PlayerPresenter

class MainActivity : AppCompatActivity(), PlayerContract.View {
    override var song: CharSequence
        get() = tvSong.text
        set(value) { tvSong.text = value }
    override var musician: CharSequence
        get() = tvMusician.text
        set(value) { tvMusician.text = value }
    override var genre: CharSequence
        get() = tvGenre.text
        set(value) { tvGenre.text = value }

    private lateinit var tvSong: TextView
    private lateinit var tvMusician: TextView
    private lateinit var tvGenre: TextView

    private lateinit var presenter: PlayerContract.Presenter

//    TODO: implement BroadcastReceiver here (anonymously)
//    TODO: create Service object here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSong = findViewById(R.id.tv_song)
        tvMusician = findViewById(R.id.tv_musician)
        tvGenre = findViewById(R.id.tv_genre)

        presenter = PlayerPresenter(this)
    }

    override fun initPlayer(): PlayerContract.ModelPlayer {
        TODO("Start player service here.")
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
