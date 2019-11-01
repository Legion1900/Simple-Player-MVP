package com.legion1900.mvpplayer.presenters

import android.util.Log
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.Song

class PlayerPresenter private constructor(override var view: PlayerContract.View) :
    PlayerContract.Presenter {

    companion object {
        private const val DEFAULT_NAME = "The Chain"
        private const val DEFAULT_MUSICIAN = "Fleetwood Mac"
        private const val DEFAULT_GENRE = "Soft rock"
        private const val DEFAULT_PATH =
            "android.resource://com.legion1900.mvpplayer/raw/fleetwood_mac_the_chain"
        val DEFAULT_SONG = Song(DEFAULT_NAME, DEFAULT_MUSICIAN, DEFAULT_GENRE, DEFAULT_PATH)

        private var presenter: PlayerContract.Presenter? = null

        fun getPresenter(view: PlayerContract.View): PlayerContract.Presenter {
            if (presenter == null)
                presenter = PlayerPresenter(view)
            else presenter?.view = view
            return presenter!!
        }
    }

    private val repo = view.getRepository()

    init {
        val song = repo.loadLastSong() ?: DEFAULT_SONG
        view.initPlatform(song)
        updateView(song)
    }

    private fun updateView(song: PlayerContract.ModelSong) {
        view.song = song.name
        view.musician = song.musician
        view.genre = song.genre
    }

    override lateinit var player: PlayerContract.ModelPlayer

    override fun onPlayBtnClick() {
        player.play()
    }

    override fun onPauseBtnClick() {
        player.pause()
        repo.saveLastSong(player.song!!)
    }

    override fun onStopBtnClick() {
        player.stop()
        repo.saveLastSong(player.song!!)
    }

    override fun onSongChanged(song: PlayerContract.ModelSong) {
        player.song = song
        updateView(song)
        repo.saveLastSong(song)
    }
}