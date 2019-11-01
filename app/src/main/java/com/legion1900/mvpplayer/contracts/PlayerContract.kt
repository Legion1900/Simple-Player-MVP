package com.legion1900.mvpplayer.contracts

import android.content.SharedPreferences
import android.os.Parcelable

object PlayerContract {
    const val EXTRA_SONG = "ModelSong"
    const val REPO_KEY = "Previous ModelSong"

    interface View {
        var song: CharSequence
        var musician: CharSequence
        var genre: CharSequence
        fun getRepository(): Repository
        /*
        * Platform-specific init calls should be placed here.
        * Must set song property of Presenter.
        * */
        fun initPlayer(song: ModelSong)
        /*
        * Should start broadcast receiver if it`s  offline and call startActivity.
        * */
        fun chooseSong()
    }

    interface Presenter {
        val view: View
        var player: ModelPlayer
        fun onPlayBtnClick()
        fun onPauseBtnClick()
        fun onStopBtnClick()
        fun onSongChanged(song: ModelSong)
    }

    interface ModelSong : Parcelable {
        val name: String
        val musician: String
        val genre: String
        val path: String
        val time: Int
    }

    /*
    * Represents last song loader.
    * */
    interface Repository {
        fun loadLastSong(): ModelSong?
        fun saveLastSong(song: ModelSong)
    }

    interface ModelPlayer {
        var song: ModelSong?
        fun play()
        fun pause()
        fun stop()
    }
}