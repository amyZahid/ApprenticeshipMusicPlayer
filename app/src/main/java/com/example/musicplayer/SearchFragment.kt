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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val songListViewModel : SongListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_layout, container, false)
        val autocompleteSearchInput = view.findViewById<AutoCompleteTextView>(R.id.searchAutoCompleteTextView)

        val adapter = SongSearchAdapter(requireContext(), R.layout.list_item_search_result)
        autocompleteSearchInput.setAdapter(adapter)

        val songList = songListViewModel.songListLiveData.value!!

        adapter.setSongList(songList)

        autocompleteSearchInput.onItemClickListener = AdapterView.OnItemClickListener{
                _, _, i, _ ->
            songListViewModel.songListLiveData.value?.get(i)?.let {
                songListViewModel.currentSong.value = it
            }
        }

        return view
    }

    private fun convertSearch(stores: ArrayList<AudioModel>): ArrayList<String>  {
        return ArrayList(stores.map { "${it.songName}, ${it.songArtist}" })
    }

}