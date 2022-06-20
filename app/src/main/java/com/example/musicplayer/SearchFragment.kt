package com.example.musicplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {

    private val songListViewModel : SongListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_layout, container, false)
        val autocompleteSearchInput = view.findViewById<AutoCompleteTextView>(R.id.searchAutoCompleteTextView)
        val searchResultsCardView = view.findViewById<CardView>(R.id.searchResultsCardView)
        val resultsListView = view.findViewById<RecyclerView>(R.id.resultsListView)
        val searchButton = view.findViewById<Button>(R.id.searchButton)
        val noResultsTextView = view.findViewById<TextView>(R.id.noResultsFoundTextView)

        val songList = songListViewModel.songListLiveData.value!!
        var searchResultString = convertSearchResults(songList)

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_search_result, searchResultString)
        autocompleteSearchInput.setAdapter(adapter)

        var songSearchResults : ArrayList<AudioModel> = ArrayList()


        searchButton.setOnClickListener {
            val input = autocompleteSearchInput.text.toString().lowercase()
            if (input != "") {
                searchResultsCardView.isVisible = true
                for (songItem in songList) {
                    val songName = songItem.songName.lowercase()
                    val songArtist = songItem.songArtist.lowercase()
                    val songAlbum = songItem.songAlbum.lowercase()
                    if (songName.contains(input)) {
                        songSearchResults.add(songItem)
                    } else if (songArtist.contains(input)) {
                        songSearchResults.add(songItem)
                    } else if (songAlbum.contains(input)) {
                        songSearchResults.add(songItem)
                    }
                }
                resultsListView.layoutManager = LinearLayoutManager(context)

                if (songSearchResults.size > 0) {

                    val resultAdapter = SearchResultsListAdapter(songSearchResults) {
                        songListViewModel.queuedSongsLiveData.value = songSearchResults
                        songListViewModel.currentSongCounter = it
                        songListViewModel.currentSong.value = songSearchResults[it]
                    }
                    resultsListView.isVisible = true
                    resultsListView.adapter = resultAdapter
                    noResultsTextView.isVisible = false
                }


            }
        }

        return view
    }

    private fun convertSearchResults(songs: ArrayList<AudioModel>): ArrayList<String>  {
        var stringSearchResults : ArrayList<String> = ArrayList()
        val songNames = ArrayList(songs.map { it.songName })
        for (results in songNames) {
            if (!stringSearchResults.contains(results)) {
                stringSearchResults.add(results)
            }
        }
        var songArtists = ArrayList(songs.map { it.songArtist })
        for (results in songArtists) {
            if (!stringSearchResults.contains(results)) {
                stringSearchResults.add(results)
            }
        }
        var songAlbums = ArrayList(songs.map { it.songAlbum })
        for (results in songAlbums) {
            if (!stringSearchResults.contains(results)) {
                stringSearchResults.add(results)
            }
        }

        return stringSearchResults
    }



}