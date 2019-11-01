package com.legion1900.mvpplayer.presenters

import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.Song

class PlayerPresenter(override val view: PlayerContract.View) : PlayerContract.Presenter {

    private companion object DefaultData {
        const val DEFAULT_NAME = "The Chain"
        const val DEFAULT_MUSICIAN = "Fleetwood Mac"
        const val DEFAULT_GENRE = "Soft rock"
        const val DEFAULT_PATH =
            "android.resource://com.legion1900.simpleplayer/raw/fleetwood_mac_the_chain"
        val DEFAULT_SONG = Song(DEFAULT_NAME, DEFAULT_MUSICIAN, DEFAULT_GENRE, DEFAULT_PATH)
    }

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