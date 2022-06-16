package com.example.musicplayer

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import java.util.*
import kotlin.collections.ArrayList

class SongSearchAdapter(context: Context, viewResourceId: Int) : ArrayAdapter<String?>(context, viewResourceId) {
    private var songList: ArrayList<AudioModel> = arrayListOf()
    private var suggestions : ArrayList<AudioModel> = arrayListOf()

    fun setSongList(songs : ArrayList<AudioModel>) {
        songList.clear()
        songList.addAll(songs)
    }

    override fun getFilter(): Filter {
        return songSearchFilter
    }

    override fun getItem(position: Int): String? {
        return songList[position].songName
    }

    override fun getCount(): Int {
        return songList.size
    }

    private var songSearchFilter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            val filterResults = FilterResults()
            if (constraint != null) {
                val input = constraint.toString().toLowerCase(Locale.ROOT).trimStart()
                suggestions.addAll(songList.filter { song -> song.songName.toLowerCase().contains(input) })
            }
            filterResults.values = suggestions
            filterResults.count = suggestions.size
            return filterResults
        }

        override fun publishResults(constaint: CharSequence?, results: FilterResults?) {
            clear()
            if (results != null && results.count > 0) {
                val suggestions = results.values as ArrayList<*>
                for (suggestion in suggestions) {
                    add(suggestion.toString())
                }
            }
        }
    }




}