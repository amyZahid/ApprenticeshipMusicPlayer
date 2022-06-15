package com.example.musicplayer

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicListAdapter(private val musicItemsList: ArrayList<AudioModel>?)
    : RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_song, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val songItemViewModel = musicItemsList?.get(position)

        if (songItemViewModel != null) {
            holder.songTitleTextView.text = songItemViewModel.songName
            holder.songArtistTextView.text = songItemViewModel.songArtist
        }
    }

    override fun getItemCount(): Int {
        return musicItemsList?.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val playSongClickableArea : LinearLayout = itemView.findViewById(R.id.playSongClickableArea)
        val addToPlaylistIcon : ImageView = itemView.findViewById(R.id.addToPlaylistIcon)
        val songTitleTextView : TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtistTextView : TextView = itemView.findViewById(R.id.songArtistTextView)
    }

}