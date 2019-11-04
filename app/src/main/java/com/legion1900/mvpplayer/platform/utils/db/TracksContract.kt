package com.legion1900.mvpplayer.platform.utils.db

import android.provider.BaseColumns

object TracksContract {
    const val COLUMN_ID = "_id"

    object Genres : BaseColumns {
        const val TABLE_NAME = "Genres"
        const val COLUMN_NAME_NAME = "name"
    }

    object Musicians : BaseColumns {
        const val TABLE_NAME = "Musicians"
        const val COLUMN_NAME_NAME = "name"
    }

    object Songs : BaseColumns {
        const val TABLE_NAME = "Song"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_PATH = "path"
        const val COLUMN_NAME_MUSICIAN = "musicianID"
        const val COLUMN_NAME_GENRE = "genreID"
    }
}