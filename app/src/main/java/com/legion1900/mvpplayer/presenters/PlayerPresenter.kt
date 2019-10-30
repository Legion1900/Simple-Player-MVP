package com.legion1900.mvpplayer.presenters

import com.legion1900.mvpplayer.contracts.PlayerContract

class PlayerPresenter(override val view: PlayerContract.View) : PlayerContract.Presenter {

    private val player = view.initPlayer()

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