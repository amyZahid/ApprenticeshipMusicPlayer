package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultsListAdapter(private val musicItemsList: ArrayList<AudioModel>, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recent_searches, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val songItemViewModel = musicItemsList[position]

        holder.songTitleTextView.text = songItemViewModel.songName
        holder.songArtistTextView.text = songItemViewModel.songArtist
        holder.songAlbumTextView.text = songItemViewModel.songAlbum
        holder.resultSongClickableArea.setOnClickListener {
            clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return musicItemsList.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val songTitleTextView : TextView = itemView.findViewById(R.id.songTitleTextView)
        val songArtistTextView : TextView = itemView.findViewById(R.id.songArtistTextView)
        val songAlbumTextView : TextView = itemView.findViewById(R.id.songAlbumTextView)
        val resultSongClickableArea : LinearLayout = itemView.findViewById(R.id.resultSongClickableArea)
    }

}