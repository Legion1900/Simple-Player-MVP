package com.legion1900.mvpplayer.presenters

import com.legion1900.mvpplayer.contracts.PlayerContract
import com.legion1900.mvpplayer.models.data.SongData

class ChooserPresenter private constructor(override var view: PlayerContract.ChooserView) :
    PlayerContract.ChooserPresenter {

    private val repo = view.getRepository()

    private lateinit var songs: List<PlayerContract.ModelSong>

    init {
        view.genres = repo.getGenres()
        view.musicians = repo.getMusicians()
    }

    override fun onGenreClick(genre: String) {
        songs = repo.sortByGenre(genre)
        val data = SongData.readSongData(songs, PlayerContract.ModelSong::musician)
        view.displaySongs(data)
    }

    override fun onMusicianClick(musician: String) {
        songs = repo.sortByMusician(musician)
        val data = SongData.readSongData(songs, PlayerContract.ModelSong::genre)
        view.displaySongs(data)
    }

    override fun onSongClick(index: Int) {
        val song = songs[index]
        view.sendSong(song)
        view.close()
    }

    companion object {
        private var presenter: PlayerContract.ChooserPresenter? = null
        fun presenterFor(view: PlayerContract.ChooserView): PlayerContract.ChooserPresenter {
            if (presenter == null)
                presenter = ChooserPresenter(view)
            else presenter!!.view = view
            return presenter!!
        }
    }
}
