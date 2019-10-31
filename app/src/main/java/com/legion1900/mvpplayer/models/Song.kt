package com.legion1900.mvpplayer.models

import android.os.Parcel
import android.os.Parcelable
import com.legion1900.mvpplayer.contracts.PlayerContract

class Song(
    override val name: String,
    override val musician: String,
    override val genre: String,
    override val path: String,
    override val time: Int = 0
) : PlayerContract.ModelSong {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(musician)
        parcel.writeString(genre)
        parcel.writeString(path)
        parcel.writeInt(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}