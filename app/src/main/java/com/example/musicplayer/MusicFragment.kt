package com.example.musicplayer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MusicFragment : Fragment() {

    private var sortedByOption : Int = 0
    private val songListViewModel : SongListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_layout, container, false)

        val musicItemsList: ArrayList<AudioModel>? = songListViewModel.songListLiveData.value
        val playlists = songListViewModel.playlists

        val sortSongsIcon = view.findViewById<ImageView>(R.id.sortSongsIcon)
        val noSongsTextView = view.findViewById<TextView>(R.id.noSongsTextView)
        val addPlaylistButton = view.findViewById<ImageView>(R.id.addPlaylistButton)

        if (musicItemsList != null) {
            displayMusic(view, musicItemsList)
            sortSongsIcon.setOnClickListener {
                sortMusicDialog(view, musicItemsList)
            }
        } else {
            noSongsTextView.isVisible = true
        }

        addPlaylistButton.setOnClickListener {
            createPlaylistDialog(view)
            songListViewModel.playlists.value?.let { it1 -> displayPlaylists(view, it1) }
        }

        return view
    }

    private fun displayPlaylists(view: View, playlistList: ArrayList<PlaylistModel>?) {
        val noPlaylistTextView = view.findViewById<TextView>(R.id.noPlaylistsTextView)
        val playlistListView = view.findViewById<RecyclerView>(R.id.playlistListView)

        if (playlistList.isNullOrEmpty()) {
            noPlaylistTextView.isVisible = true
            playlistListView.isVisible = false
        } else if (playlistList.size > 0) {
            noPlaylistTextView.isVisible = false
            playlistListView.isVisible = true
            val adapter = PlaylistListAdapter(playlistList) {
                Toast.makeText(requireContext(), playlistList[it].playlistName, Toast.LENGTH_SHORT).show()
            }
            playlistListView.adapter = adapter

        }


    }

    private fun displayMusic(view: View, musicList: ArrayList<AudioModel>) {
        val noSongsTextView = view.findViewById<TextView>(R.id.noSongsTextView)
        val songListView =  view.findViewById<RecyclerView>(R.id.songListView)

        songListView.layoutManager = LinearLayoutManager(context)

        if (sortedByOption == 0) {
            musicList.sortWith(compareBy { it.songName })
            songListViewModel.updateSongList(musicList)
        } else {
            musicList.sortWith(compareBy { it.songArtist })
            songListViewModel.updateSongList(musicList)
        }

        if (musicList.size > 0) {
            val adapter = MusicListAdapter(musicList) {
                songListViewModel.queuedSongsLiveData.value = musicList
                songListViewModel.currentSongCounter = it
                songListViewModel.currentSong.value = musicList[it]
            }
            songListView.isVisible = true
            songListView.adapter = adapter
            noSongsTextView.isVisible = false
        }


    }

    private fun sortMusicDialog(view: View, musicList: ArrayList<AudioModel>) {
        val sortMusicOptionsArray = arrayOf("Song", "Artist")

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("List of Fruits")
                .setSingleChoiceItems(sortMusicOptionsArray, sortedByOption) { dialog_, which ->
                    sortedByOption = which
                }

                .setPositiveButton("Ok") { dialog, which ->
                    displayMusic(view, musicList)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun createPlaylistDialog(view : View) {
        val edittext = EditText(requireContext());
        var playlistName = ""
        var playlist : PlaylistModel
        var playlistList = songListViewModel.playlists.value
        var playlistSongs : ArrayList<AudioModel> = ArrayList()
        songListViewModel.songListLiveData.value?.get(0)?.let { playlistSongs.add(it) }
        songListViewModel.songListLiveData.value?.get(1)?.let { playlistSongs.add(it) }


        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(edittext)
                .setTitle("Create a playlist...")
                .setPositiveButton("Create") { dialog, which ->
                    playlistName = edittext.text.toString()
                    playlist = PlaylistModel(playlistName, playlistSongs)
                    playlistList?.add(playlist)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        if (playlistList != null) {
            songListViewModel.updatePlaylists(playlistList)
            displayPlaylists(view, playlistList)
        }
    }
}
