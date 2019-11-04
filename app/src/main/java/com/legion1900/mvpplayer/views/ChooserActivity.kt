package com.legion1900.mvpplayer.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.SongsRepository
import com.legion1900.mvpplayer.presenters.ChooserPresenter
import com.legion1900.mvpplayer.views.adapters.SongsAdapter

class ChooserActivity : AppCompatActivity(), PlayerContract.ChooserView {
    override lateinit var musicians: List<String>
    override lateinit var genres: List<String>

    private lateinit var presenter: PlayerContract.ChooserPresenter

    private lateinit var spinnerMusicians: Spinner
    private lateinit var spinnerGenres: Spinner

    private lateinit var rv: RecyclerView

    private val adapter =
        SongsAdapter(View.OnClickListener { presenter.onSongClick(rv.getChildAdapterPosition(it)) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        presenter = ChooserPresenter.presenterFor(this)
        findViews()
        initRecyclerView()
        initSpinner(spinnerMusicians, PlayerContract.ChooserView::musicians)
        initSpinner(spinnerGenres, PlayerContract.ChooserView::genres)
    }

    private fun findViews() {
        spinnerGenres = findViewById(R.id.genres)
        spinnerMusicians = findViewById(R.id.musicians)
        rv = findViewById(R.id.rv_songs)
    }

    private fun initRecyclerView() {
        rv.setHasFixedSize(true)
        rv.adapter = adapter
    }

    /*
    * prop - property that provide data.
    * */
    private fun initSpinner(spinner: Spinner, prop: PlayerContract.ChooserView.() -> List<String>) {
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, prop())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun displaySongs(songs: List<PlayerContract.ModelSongData>) {
        adapter.updateDataSet(songs)
    }

    override fun getRepository(): PlayerContract.SongsRepository = SongsRepository(contentResolver)

    override fun sendSong(song: PlayerContract.ModelSong) {
        val response = Intent()
            .setAction(PlayerActivity.ACTION_SONG_SENT)
            .putExtra(PlayerActivity.SONG_PATH_KEY, song)
        sendBroadcast(response)
    }

    override fun close() {
        finish()
    }
}
