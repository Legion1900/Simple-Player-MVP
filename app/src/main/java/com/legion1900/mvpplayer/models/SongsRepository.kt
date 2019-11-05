package com.legion1900.mvpplayer.models

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.data.Song
import com.legion1900.mvpplayer.platform.utils.db.TracksContract
import java.util.*

class SongsRepository(private val resolver: ContentResolver) : PlayerContract.SongsRepository {

    override fun getMusicians(): List<String> =
        getAllRows(URI_MUSICIANS, TracksContract.Musicians.COLUMN_NAME_NAME)

    override fun getGenres(): List<String> =
        getAllRows(URI_GENRES, TracksContract.Genres.COLUMN_NAME_NAME)

    private fun getAllRows(uri: Uri, column: String): List<String> {
        val projection = arrayOf(column)
        val cursor = resolver.query(uri, projection, null, null, null)
        return cursor!!.use {
                val colIndex = it.getColumnIndex(column)
                val musicians = mutableListOf<String>()
                while (it.moveToNext()) {
                    musicians.add(it.getString(colIndex))
                }
                musicians
            }
    }

    override fun sortByMusician(musician: String): List<PlayerContract.ModelSong> =
        querySorted(URI_SORT_MUSICIAN, arrayOf(musician)).use { readCursor(it) }

    override fun sortByGenre(genre: String): List<PlayerContract.ModelSong> =
        querySorted(URI_SORT_GENRE, arrayOf(genre)).use { readCursor(it) }

    private fun querySorted(uri: Uri, whereArgs: Array<String>): Cursor {
        val aliases = arrayOf(SONG_COLUMN, MUSICIAN_COLUMN, GENRE_COLUMN)
        return resolver.query(uri, aliases, null, whereArgs, null)!!
    }

    private fun readCursor(cursor: Cursor): List<PlayerContract.ModelSong> {
        val songs = mutableListOf<PlayerContract.ModelSong>()
        val songInd = cursor.getColumnIndex(SONG_COLUMN)
        val musicianInd = cursor.getColumnIndex(MUSICIAN_COLUMN)
        val genreInd = cursor.getColumnIndex(GENRE_COLUMN)
        val pathInd = cursor.getColumnIndex(PATH_COLUMN)
        while (cursor.moveToNext()) {
            val song = cursor.getString(songInd)
            val musician = cursor.getString(musicianInd)
            val genre = cursor.getString(genreInd)
            val path = cursor.getString(pathInd)
            songs.add(Song(song, musician, genre, path))
        }
        return songs
    }

    private companion object {
        val URI_GENRES: Uri = Uri.parse("content://com.legion1900.mvpplayer.provider/Genres")
        val URI_MUSICIANS: Uri =
            Uri.parse("content://com.legion1900.mvpplayer.provider/Musicians")
        val URI_SORT_GENRE: Uri =
            Uri.parse("content://com.legion1900.mvpplayer.provider/Song/genre")
        val URI_SORT_MUSICIAN: Uri =
            Uri.parse("content://com.legion1900.mvpplayer.provider/Song/musician")

        const val SONG_COLUMN = "song"
        const val MUSICIAN_COLUMN = "musician"
        const val GENRE_COLUMN = "genre"
        const val PATH_COLUMN = TracksContract.Songs.COLUMN_NAME_PATH
    }
}