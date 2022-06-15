package com.example.musicplayer


import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MusicFragment : Fragment() {

    private var sortedByOption : Int = 0
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_layout, container, false)

        val musicItemsList: ArrayList<AudioModel>? =
            requireArguments().getSerializable("bundle_key") as ArrayList<AudioModel>?

        val sortSongsIcon = view.findViewById<ImageView>(R.id.sortSongsIcon)
        val noSongsTextView = view.findViewById<TextView>(R.id.noSongsTextView)
        val songListView = view.findViewById<RecyclerView>(R.id.songListView)

        if (musicItemsList != null) {
            displayMusic(view, musicItemsList)
            sortSongsIcon.setOnClickListener {
                sortMusicDialog(view, musicItemsList)
            }
        } else {
            noSongsTextView.isVisible = true
        }


        return view
    }

    private fun displayMusic(view: View, musicList: ArrayList<AudioModel>) {
        val noSongsTextView = view.findViewById<TextView>(R.id.noSongsTextView)
        val songListView = view.findViewById<RecyclerView>(R.id.songListView)

        songListView.layoutManager = LinearLayoutManager(context)
        if (sortedByOption == 0) {
            musicList.sortWith(compareBy { it.songName })
        } else {
            musicList.sortWith(compareBy { it.songArtist })
        }

        if (musicList.size > 0) {
            val adapter = MusicListAdapter(musicList)
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
}
