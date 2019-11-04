package com.legion1900.mvpplayer.platform.utils.db

import android.provider.BaseColumns

object TracksContract {
    val COLUMN_ID = "_id"

    class Genres : BaseColumns {
        companion object {
            val TABLE_NAME = "Genres"
            val COLUMN_NAME_NAME = "name"
        }
    }

    class Musicians : BaseColumns {
        companion object {
            val TABLE_NAME = "Musicians"
            val COLUMN_NAME_NAME = "name"
        }
    }

    class Songs : BaseColumns {
        companion object {
            val TABLE_NAME = "Song"
            val COLUMN_NAME_NAME = "name"
            val COLUMN_NAME_PATH = "path"
            val COLUMN_NAME_MUSICIAN = "musicianID"
            val COLUMN_NAME_GENRE = "genreID"
        }
    }
}