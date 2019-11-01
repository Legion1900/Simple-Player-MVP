package com.legion1900.mvpplayer.contracts

import android.os.Parcelable

object PlayerContract {
    const val EXTRA_SONG = "ModelSong"
    const val REPO_KEY = "Previous ModelSong"

    interface PlayerView {
        var song: CharSequence
        var musician: CharSequence
        var genre: CharSequence
        fun getRepository(): SongStateRepository
        /*
        * Platform-specific init calls should be placed here.
        * Must set song property of Presenter.
        * */
        fun initPlatform(song: ModelSong)
    }

    interface ChooserView {
        var musicians: List<String>
        var genres: List<String>
        var song: List<ModelSong>
        fun getRepository(): SongsRepository
    }

    interface Presenter {
        var view: PlayerView
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
        var time: Int
    }

    /*
    * Represents last song loader.
    * */
    interface SongStateRepository {
        fun loadLastSong(): ModelSong?
        fun saveLastSong(song: ModelSong)
    }

    interface SongsRepository {
//        TODO: think about signatures
    }

    interface ModelPlayer {
        var song: ModelSong?
        fun play()
        fun pause()
        fun stop()
    }
}