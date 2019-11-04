package com.legion1900.mvpplayer.platform

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.legion1900.mvpplayer.platform.utils.db.DbHelper
import com.legion1900.mvpplayer.platform.utils.db.TracksContract

class SongProvider : ContentProvider() {

    private val matcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        matcher.addURI(AUTHORITY, TracksContract.Genres.TABLE_NAME, GENRES);
        matcher.addURI(AUTHORITY, TracksContract.Musicians.TABLE_NAME, MUSICIANS);
        matcher.addURI(AUTHORITY, TracksContract.Songs.TABLE_NAME, SONGS);
        matcher.addURI(AUTHORITY, TracksContract.Songs.TABLE_NAME + "/genre", SONGS_FOR_GENRE);
        matcher.addURI(
            AUTHORITY,
            TracksContract.Songs.TABLE_NAME + "/musician",
            SONGS_FOR_MUSICIAN
        );
    }

    private lateinit var helper: DbHelper

    override fun onCreate(): Boolean {
        helper = DbHelper(context!!)
        return true
    }

//    TODO: DO NOT CLOSE DATABASE UNTIL CURSOR ALIVE
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        /*
        * We need cursor to have "_id" column to use in CursorLoader.
        * */
        val columns = if (projection != null) {
            val tmp = Array(projection.size + 1) {
                if (it < projection.size) projection[it] else TracksContract.COLUMN_ID
            }
            tmp[tmp.lastIndex] = TracksContract.COLUMN_ID
            tmp
        } else arrayOf(TracksContract.COLUMN_ID)

        helper.readableDatabase.use {
            when (matcher.match(uri)) {
                GENRES -> return it.query(
                    TracksContract.Genres.TABLE_NAME,
                    columns, // Columns to return
                    selection, // WHERE clause without WHERE itself: column1>?
                    selectionArgs, null, null, null // values to substitute ? in where
                )
                MUSICIANS -> return it.query(
                    TracksContract.Musicians.TABLE_NAME,
                    columns, // Columns to return
                    selection, // WHERE clause without WHERE itself: column1>?
                    selectionArgs, null, null, null
                )// values to substitute ? in where
                SONGS -> return it.query(
                    TracksContract.Songs.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs, null, null, null
                )
                SONGS_FOR_GENRE -> {
                    /*
                    * In this case projection content will be used to substitute Aliases in
                    * QUERY_SELECTED_GENRE or QUERY_SELECTED_MUSICIAN;
                    * selectionArgs are used as usual - to substitute '?' in existing queries.
                    * */
                    val queryGenre = buildSortingQuery(QUERY_SELECTED_GENRE, columns)
                    return it.rawQuery(queryGenre, selectionArgs)
                }
                SONGS_FOR_MUSICIAN -> {
                    val queryMusician = buildSortingQuery(QUERY_SELECTED_MUSICIAN, columns)
                    return it.rawQuery(queryMusician, selectionArgs)
                }
                else -> throw IllegalArgumentException("Unknown Uri")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not supported")
    }

    private fun buildSortingQuery(selection: String, aliases: Array<String>): String {
        var out = ""
        val keyword = "Alias"
        for (i in aliases.indices) {
            out = selection.replace((keyword + i).toRegex(), aliases[i])
        }
        return out
    }

    private companion object {
        const val AUTHORITY = "com.legion1900.mvpplayer.provider"

        const val GENRES = 1
        const val MUSICIANS = 2
        const val SONGS = 3
        const val SONGS_FOR_GENRE = 4
        const val SONGS_FOR_MUSICIAN = 5

        /*
        * Parts for building sorting queries.
        * */
//        TODO: use string interpolation with TracksContract
        const val QUERY_SELECTED_GENRE =
            "SELECT Songs._id, " +
                    "Songs.name AS Alias0, " +
                    "Musicians.name AS Alias1, " +
                    "Songs.path as path, " +
                    "Genres.name as Alias2" +
                    "FROM Songs" +
                    "JOIN Musicians ON Songs.musicianID=Musicians._id" +
                    "JOIN Genres ON Songs.genreID=Genres._id" +
                    "WHERE Genres.name=?"

        const val QUERY_SELECTED_MUSICIAN =
            "SELECT Songs._id, " +
                    "Songs.name AS Alias0, " +
                    "Musicians.name AS Alias1, " +
                    "Songs.path as path, " +
                    "Genres.name as Alias2" +
                    "FROM Songs" +
                    "JOIN Musicians ON Songs.musicianID=Musicians._id" +
                    "JOIN Genres ON Songs.genreID=Genres._id" +
                    "WHERE Musicians.name=?"
    }
}
