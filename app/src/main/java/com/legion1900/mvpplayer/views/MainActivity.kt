package com.legion1900.mvpplayer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.legion1900.mvpplayer.contracts.PlayerContract

class MainActivity : AppCompatActivity(), PlayerContract.View {
    override var song: CharSequence
        get() = tvSong.text
        set(value) { tvSong.text = value }
    override var musician: CharSequence
        get() = tvMusician.text
        set(value) { tvMusician.text = value }
    override var genre: CharSequence
        get() = tvGenre.text
        set(value) { tvGenre.text = value }

    private lateinit var tvSong: TextView
    private lateinit var tvMusician: TextView
    private lateinit var tvGenre: TextView

    private lateinit var presenter: PlayerContract.Presenter

//    TODO: implement BroadcastReceiver here (anonymously)
//    TODO: create Service object here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSong = findViewById(R.id.tv_song)
        tvMusician = findViewById(R.id.tv_musician)
        tvGenre = findViewById(R.id.tv_genre)

//        presenter = TODO: instantiate presenter here
    }

    override fun initPlayer(): PlayerContract.ModelPlayer {
        TODO("Start player service here.")
    }

    override fun chooseSong() {
        TODO("Start broadcast receiver and chooser activity.")
    }
}
