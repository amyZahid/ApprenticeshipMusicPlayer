package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MusicListAdapter(private val musicItemsList: ArrayList<AudioModel>) : RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((AudioModel) -> Unit)?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_song, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val songItemViewModel = musicItemsList[position]

        holder.songTitleTextView.text = songItemViewModel.songName
        holder.songArtistTextView.text = songItemViewModel.songArtist
        holder.playSongClickableArea.setOnClickListener {
            onItemClick?.invoke(position)
        }
        holder.playSongClickableArea.setOnLongClickListener {
            onItemLongClick?.invoke(songItemViewModel)
            true
        }
    }

    override fun getItemCount(): Int {
        return musicItemsList.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val playSongClickableArea : LinearLayout = itemView.findViewById(R.id.playSongClickableArea)
        val songTitleTextView : TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtistTextView : TextView = itemView.findViewById(R.id.songArtistTextView)

    }

}
