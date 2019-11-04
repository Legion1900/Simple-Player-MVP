package com.legion1900.mvpplayer.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract

class SongsAdapter(private val itemListener: View.OnClickListener) :
    RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    private val dataSet: MutableList<PlayerContract.ModelSongData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.rv_item, parent, false)
        itemView.setOnClickListener(itemListener)
        return SongViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val songData = dataSet[position]
        holder.tvSong.text = songData.songName
        holder.tvData.text = songData.metadata
    }

    fun updateDataSet(songs: List<PlayerContract.ModelSongData>) {
        dataSet.clear()
        dataSet.addAll(songs)
        notifyDataSetChanged()
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSong: TextView = itemView.findViewById(R.id.song_name)
        val tvData: TextView = itemView.findViewById(R.id.song_data)
    }
}