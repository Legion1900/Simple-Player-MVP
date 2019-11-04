package com.legion1900.mvpplayer.presenters

import com.legion1900.mvpplayer.contracts.PlayerContract

class ChooserPresenter private constructor(override var view: PlayerContract.ChooserView) :
    PlayerContract.ChooserPresenter {

    private val repo = view.getRepository()

    init {
        view.genres = repo.getGenres()
        view.musicians = repo.getMusicians()
    }

    override fun onGenreClick(genre: String) {
        view.songs = repo.sortByGenre(genre)
    }

    override fun onMusicianClick(musician: String) {
        view.songs = repo.sortByMusician(musician)
    }

    override fun onSongClick(song: PlayerContract.ModelSong) {
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
