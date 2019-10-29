package com.legion1900.mvpplayer.contracts

import android.content.Context

object PlayerContract {
    interface View {
        var song: String
        var musician: String
        var genre: String
        fun initPlayer(): ModelPlayer
        /*
        * Start song chooser Activity.
        * */
        fun startChooser()
        fun getContext(): Context
    }

    interface Presenter {
        fun onPlayBtnClick()
        fun onPauseBtnClick()
        fun onStopBtnClick()
        fun onSongChanged()

        fun onStart()
        fun onStop()
        fun onDestroy()
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