package com.legion1900.mvpplayer.platform

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
    /*
    * As ContentProvider created within the hosting process it`s DB connections will be closed by
    * system cleanup when needed. For correct usage of Cursor it returns DB connection MUST stay
    * open.
    * */
    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        helper = DbHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        initDb()
        val columns = buildProjection(projection)
        return when (matcher.match(uri)) {
            GENRES -> getAllGenres(columns, selection, selectionArgs)
            MUSICIANS -> getAllMusicians(columns, selection, selectionArgs)
            SONGS -> getAllSongs(columns, selection, selectionArgs)
            /*
            * In this case projection ('columns') content will be used to substitute Aliases in
            * QUERY_SELECTED_GENRE or QUERY_SELECTED_MUSICIAN;
            * selectionArgs are used as usual - to substitute '?' in existing queries.
            * */
            SONGS_FOR_GENRE -> {
                getSongsBy(QUERY_SELECTED_GENRE, columns, selectionArgs)
            }
            SONGS_FOR_MUSICIAN -> {
                getSongsBy(QUERY_SELECTED_MUSICIAN, columns, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unknown Uri")
        }
    }

    /*
    * Adds _id column to array of user-requested columns.
    * projection may be null.
    * */
    private fun buildProjection(projection: Array<String>?): Array<String> {
        return if (projection != null) {
            val tmp = Array(projection.size + 1) {
                if (it < projection.size) projection[it] else TracksContract.COLUMN_ID
            }
            tmp[tmp.lastIndex] = TracksContract.COLUMN_ID
            tmp
        } else arrayOf(TracksContract.COLUMN_ID)
    }

    private fun getAllGenres(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Cursor? = db?.query(
        TracksContract.Genres.TABLE_NAME,
        projection, // Columns to return
        selection, // WHERE clause without WHERE itself: column1>?
        selectionArgs, null, null, null // values to substitute ? in where
    )

    private fun getAllMusicians(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Cursor? = db?.query(
        TracksContract.Musicians.TABLE_NAME,
        projection, // Columns to return
        selection, // WHERE clause without WHERE itself: column1>?
        selectionArgs, null, null, null // values to substitute ? in where
    )

    private fun getAllSongs(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Cursor? = db?.query(
        TracksContract.Songs.TABLE_NAME,
        projection,
        selection,
        selectionArgs, null, null, null
    )

    private fun getSongsBy(
        rawQuery: String,
        projection: Array<String>,
        selectionArgs: Array<String>?
    ): Cursor? {
        val query = buildSortingQuery(rawQuery, projection)
        return db?.rawQuery(query, selectionArgs)
    }

    private fun initDb() {
        if (db == null)
            db = helper.readableDatabase
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
        var out = selection
        val keyword = "Alias"
        for (i in aliases.indices) {
            val regex = (keyword + i).toRegex()
            out = out.replace(regex, aliases[i])
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
        const val QUERY_SELECTED_GENRE =
            "SELECT Songs._id, " +
                    "Songs.name AS Alias0, " +
                    "Musicians.name AS Alias1, " +
                    "Songs.path as path, " +
                    "Genres.name as Alias2 " +
                    "FROM Songs " +
                    "JOIN Musicians ON Songs.musicianID=Musicians._id " +
                    "JOIN Genres ON Songs.genreID=Genres._id " +
                    "WHERE Genres.name=?"

        const val QUERY_SELECTED_MUSICIAN =
            "SELECT Songs._id, " +
                    "Songs.name AS Alias0, " +
                    "Musicians.name AS Alias1, " +
                    "Songs.path as path, " +
                    "Genres.name as Alias2 " +
                    "FROM Songs " +
                    "JOIN Musicians ON Songs.musicianID=Musicians._id " +
                    "JOIN Genres ON Songs.genreID=Genres._id " +
                    "WHERE Musicians.name=?"
    }
}
