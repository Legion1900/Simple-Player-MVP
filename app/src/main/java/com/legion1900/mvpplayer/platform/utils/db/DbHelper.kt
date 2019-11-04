package com.legion1900.mvpplayer.platform.utils.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException

class DbHelper(private val context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        /*
        * Nothing to do here.
        * */
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        /*
        * Nothing to do here.
        * */
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        if (context.databaseList().isEmpty()) installDb()
        return super.getReadableDatabase()
    }

    /*
     * Copies database from assets to private folder.
     * */
    private fun installDb() {
        try {
            val inputStream = context.assets.open(DATABASE_PATH)
            val targetFile = context.getDatabasePath(DATABASE_NAME)
            val outputStream = FileOutputStream(targetFile)
            val rawData = ByteArray(inputStream.available())
            inputStream.read(rawData)
            inputStream.close()
            outputStream.write(rawData)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            Log.e(TAG_ERROR, e.message, e)
        }

    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "tracks.sqlite3"

        private val DATABASE_PATH = "databases/tracks.sqlite3"

        private val TAG_ERROR = "DB Error"
    }
}