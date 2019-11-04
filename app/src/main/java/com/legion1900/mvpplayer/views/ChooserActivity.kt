package com.legion1900.mvpplayer.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract

class ChooserActivity : AppCompatActivity(), PlayerContract.ChooserView {
    override var musicians: List<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var genres: List<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var songs: List<PlayerContract.ModelSong>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun getRepository(): PlayerContract.SongsRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendSong(song: PlayerContract.ModelSong) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)
    }
}
