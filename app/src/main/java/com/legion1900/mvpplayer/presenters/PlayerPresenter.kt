package com.legion1900.mvpplayer.presenters

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


//    TODO: set PlayerActivity view`s text

    private val repo = view.getRepository()

    init {
        view.initPlatform(repo.loadLastSong() ?: DEFAULT_SONG)
    }

    override lateinit var player: PlayerContract.ModelPlayer

    override fun onPlayBtnClick() {
        player.play()
    }

    override fun onPauseBtnClick() {
        player.pause()
    }

    override fun onStopBtnClick() {
        player.stop()
    }

    override fun onSongChanged(song: PlayerContract.ModelSong) {
        player.song = song
    }

}