package com.legion1900.mvpplayer.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.mvpplayer.R
import com.legion1900.mvpplayer.contracts.PlayerContract

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    var dataset: MutableList<PlayerContract.ModelSongData> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.rv_item, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val songData = dataset[position]
        holder.tvSong.text = songData.songName
        holder.tvData.text = songData.metadata
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSong: TextView = itemView.findViewById(R.id.song_name)
        val tvData: TextView = itemView.findViewById(R.id.song_data)
    }
}