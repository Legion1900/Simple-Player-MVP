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
        * Must set song property of PlayerPresenter.
        * */
        fun initPlatform(song: ModelSong)
    }

    interface ChooserView {
        var musicians: List<String>
        var genres: List<String>
        fun displaySongs(songs: List<ModelSongData>)
        fun getRepository(): SongsRepository
        /*
        * Should contain platform-specific calls for broadcasting chosen song.
        * */
        fun sendSong(song: ModelSong)

        /*
        * Should perform platform-specific calls for closing this view.
        * */
        fun close()
    }

    interface PlayerPresenter {
        var view: PlayerView
        var player: ModelPlayer
        fun onPlayBtnClick()
        fun onPauseBtnClick()
        fun onStopBtnClick()
        fun onSongChanged(song: ModelSong)
    }

    interface ChooserPresenter {
        var view: ChooserView
        fun onGenreClick(genre: String)
        fun onMusicianClick(musician: String)
        fun onSongClick(index: Int)
    }

    interface ModelSong : Parcelable {
        val name: String
        val musician: String
        val genre: String
        val path: String
        var time: Int
    }

    interface ModelSongData {
        val songName: String
        val metadata: String
    }

    /*
    * Represents last song loader.
    * */
    interface SongStateRepository {
        fun loadLastSong(): ModelSong?
        fun saveLastSong(song: ModelSong)
    }

    interface SongsRepository {
        fun getMusicians(): List<String>
        fun getGenres(): List<String>
        fun sortByMusician(musician: String): List<ModelSong>
        fun sortByGenre(genre: String): List<ModelSong>
    }

    interface ModelPlayer {
        var song: ModelSong?
        fun play()
        fun pause()
        fun stop()
    }
}