package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class PlaylistListAdapter(private val playlistList: ArrayList<PlaylistModel>, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<PlaylistListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_playlist, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val playlistViewModel = playlistList[position]

        holder.playlistNameTextView.text = playlistViewModel.playlistName
        holder.playlistCountTextView.text = playlistViewModel.songList.size.toString()
        holder.playlistClickableArea.setOnClickListener {
            clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val playlistNameTextView : TextView = itemView.findViewById(R.id.playlistNameTextView)
        val playlistCountTextView : TextView = itemView.findViewById(R.id.playlistCountTextView)
        val playlistClickableArea : ConstraintLayout = itemView.findViewById(R.id.playlistClickableArea)
    }

}