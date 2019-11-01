package com.legion1900.mvpplayer.models

import android.content.SharedPreferences
import android.util.Log
import com.legion1900.mvpplayer.contracts.PlayerContract

class SongStateRepository(private val pref: SharedPreferences) : PlayerContract.SongStateRepository {

    private companion object SharedPrefKeys {
        const val SONG = "song"
        const val MUSICIAN = "musician"
        const val GENRE = "genre"
        const val PATH = "path"
        const val TIME = "time"
    }

    override fun loadLastSong(): PlayerContract.ModelSong? {
        val song = pref.getString(SONG, null)
        val musician = pref.getString(MUSICIAN, null)
        val genre = pref.getString(GENRE, null)
        val path = pref.getString(PATH, null)
        val time = pref.getInt(TIME, 0)
        Log.d("test", "song=$song musician=$musician genre=$genre time=$time path=$path")
        return if (song == null || musician == null || genre == null || path == null) null
        else Song(song, musician, genre, path, time)
    }

    override fun saveLastSong(song: PlayerContract.ModelSong) {
        val editor = pref.edit()
        editor.putString(SONG, song.name)
        editor.putString(MUSICIAN, song.musician)
        editor.putString(GENRE, song.genre)
        editor.putString(PATH, song.path)
        editor.putInt(TIME, song.time)
        editor.apply()
        Log.d("test", "Saved")
    }

}