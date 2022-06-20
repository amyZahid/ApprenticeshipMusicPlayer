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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_layout, container, false)

        val musicItemsList: ArrayList<AudioModel>? = songListViewModel.songListLiveData.value
        val playlists : ArrayList<PlaylistModel>? = songListViewModel.playlists.value

        val sortSongsIcon = view.findViewById<ImageView>(R.id.sortSongsIcon)
        val noSongsTextView = view.findViewById<TextView>(R.id.noSongsTextView)
        val addPlaylistButton = view.findViewById<ImageView>(R.id.addPlaylistButton)
        val noPlaylistTextView = view.findViewById<TextView>(R.id.noPlaylistsTextView)

        if (musicItemsList != null) {
            displayMusic(view, musicItemsList)
            sortSongsIcon.setOnClickListener {
                sortMusicDialog(view, musicItemsList)
            }
        } else {
            noSongsTextView.isVisible = true
        }

        if (playlists != null) {
            displayPlaylists(view, playlists)
        } else {
            noPlaylistTextView.isVisible = true
        }

        addPlaylistButton.setOnClickListener {
            createPlaylistDialog(view)
        }

        return view
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
            val adapter = MusicListAdapter(musicList)
            adapter.onItemClick={
                songListViewModel.queuedSongsLiveData.value = musicList
                songListViewModel.currentSongCounter = it
                songListViewModel.currentSong.value = musicList[it]
            }
            adapter.onItemLongClick={
                addToPlaylistDiaglog(view, it)
                //Toast.makeText(requireContext(), it.songName, Toast.LENGTH_SHORT).show()
            }
            songListView.isVisible = true
            songListView.adapter = adapter
            noSongsTextView.isVisible = false
        }
    }

    private fun displayPlaylists(view: View, playlists : ArrayList<PlaylistModel>?) {
        val noPlaylistTextView = view.findViewById<TextView>(R.id.noPlaylistsTextView)
        val playlistListView = view.findViewById<RecyclerView>(R.id.playlistListView)

        playlistListView.layoutManager = LinearLayoutManager(context)

        if (playlists.isNullOrEmpty()) {
            noPlaylistTextView.isVisible = true
        } else {
            if (playlists.size > 0) {
                val adapter = PlaylistListAdapter(playlists)
                adapter.onItemClick={
                    if (playlists[it].songList.size > 0) {
                        songListViewModel.queuedSongsLiveData.value = playlists[it].songList
                        songListViewModel.currentSong.value = playlists[it].songList[0]
                        songListViewModel.currentSongCounter = 0
                    } else {
                        Toast.makeText(requireContext(), "No songs in playlist", Toast.LENGTH_SHORT).show()
                    }
                }
                adapter.onItemLongClick={
                    //Toast.makeText(requireContext(), songListViewModel.playlists.value?.get(it)?.playlistName, Toast.LENGTH_SHORT).show()
                    deletePlaylistAlertDialog(view, it)
                }
                playlistListView.isVisible = true
                playlistListView.adapter = adapter
                noPlaylistTextView.isVisible = false
            }
        }

    }

    private fun sortMusicDialog(view: View, musicList: ArrayList<AudioModel>) {
        val sortMusicOptionsArray = arrayOf("Song", "Artist")

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Sort songs by...")
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
        var input: String
        val existingPlaylists = songListViewModel.playlists.value

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setView(edittext)
                .setTitle("Create a playlist...")
                .setPositiveButton("Create") { dialog, which ->
                    input = edittext.text.toString()
                    val newPlaylist = PlaylistModel(input, arrayListOf())
                    existingPlaylists?.add(newPlaylist)
                    songListViewModel.playlists.value = existingPlaylists
                    displayPlaylists(view, songListViewModel.playlists.value)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun addToPlaylistDiaglog(view: View, selectedSong: AudioModel) {
        var playlistNames: ArrayList<String> = arrayListOf()
        val playlists: ArrayList<PlaylistModel>? = songListViewModel.playlists.value
        var chosenPlaylist = 0
        if (playlists != null) {
            if (playlists.size > 0) {
                for (playlist in songListViewModel.playlists.value!!) {
                    playlistNames.add(playlist.playlistName)
                }
                context?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle("Add ${selectedSong.songName} to...")
                        .setSingleChoiceItems(playlistNames.toTypedArray(), chosenPlaylist) {_, which ->
                            chosenPlaylist = which
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            songListViewModel.playlists.value!!.get(chosenPlaylist).songList.add(selectedSong)
                           displayPlaylists(view, songListViewModel.playlists.value!!)
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
        else {
            Toast.makeText(requireContext(), "No playlists found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePlaylistAlertDialog(view: View, playlistPosition: Int) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete song?")
                .setMessage(songListViewModel.playlists.value?.get(playlistPosition)?.playlistName)
                .setPositiveButton("Delete") { dialog, which ->
                    songListViewModel.playlists.value?.removeAt(playlistPosition)
                    displayPlaylists(view, songListViewModel.playlists.value)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }


    }

}
