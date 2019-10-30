package com.legion1900.mvpplayer.contracts

object PlayerContract {
    interface View {
        var song: CharSequence
        var musician: CharSequence
        var genre: CharSequence
        fun initPlayer(): ModelPlayer
        /*
        * Should start broadcast receiver if it`s  offline and call startActivity.
        * */
        fun chooseSong()
    }

    interface Presenter {
        val view: View
        fun onPlayBtnClick()
        fun onPauseBtnClick()
        fun onStopBtnClick()
        fun onSongChanged(song: ModelSong)
    }

    interface ModelSong {
        val musician: String
        val genre: String
        val path: String
    }

    interface ModelPlayer {
        var song: ModelSong
        fun play()
        fun pause()
        fun stop()
    }
}