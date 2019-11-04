package com.legion1900.mvpplayer.models.data

import com.legion1900.mvpplayer.contracts.PlayerContract

class SongData(override val songName: String, override val metadata: String) :
    PlayerContract.ModelSongData {
    companion object {
        fun readSongData(
            songs: Collection<PlayerContract.ModelSong>,
            getData: PlayerContract.ModelSong.() -> String
        ): List<PlayerContract.ModelSongData> {
            val songsData = mutableListOf<PlayerContract.ModelSongData>()
            for (song in songs) {
                songsData.add(SongData(song.name, song.getData()))
            }
            return songsData
        }
    }
}