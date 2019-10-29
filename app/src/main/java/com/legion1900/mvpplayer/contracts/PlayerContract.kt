package com.legion1900.mvpplayer.contracts

object PlayerContract {
    interface View {
        var song: String
        var musician: String
        var genre: String
        fun initPlayer(): ModelPlayer
        /*
        * Should start broadcast receiver if it`s not online and call startActivity.
        * */
        fun startChooser()
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