package com.example.musicplayer

import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.io.File

class PlayFragment : Fragment() {
    private val songListViewModel : SongListViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_play_layout, container, false)

        val musicPlaying = songListViewModel.isPlaying

        var shuffleActivated : Boolean = false
        val shuffleIcon = view.findViewById<ImageView>(R.id.shuffleIcon)
        shuffleIcon.setOnClickListener {
            if (!shuffleActivated) {
                shuffleActivated = true
                shuffleIcon.setColorFilter(resources.getColor(R.color.highlight_purple))
                //songListViewModel.queuedSongsLiveData.
            } else {
                shuffleActivated = false
                shuffleIcon.setColorFilter(resources.getColor(R.color.text_white))
            }
        }

        val songNameTextView = view.findViewById<TextView>(R.id.songNameTextView)
        val songArtistTextView = view.findViewById<TextView>(R.id.songArtistTextView)
        val songAlbumTextView = view.findViewById<TextView>(R.id.songAlbumTextView)

        songListViewModel.currentSong.observe(viewLifecycleOwner
        ) {
            songNameTextView.text = it.songName
            songAlbumTextView.text = it.songAlbum
            songArtistTextView.text = it.songArtist
        }

        val playPauseClickable = view.findViewById<ConstraintLayout>(R.id.playPauseArea)
        val playIcon = view.findViewById<ImageView>(R.id.playIcon)
        val pauseIcon = view.findViewById<ImageView>(R.id.pauseIcon)
        val nextIcon = view.findViewById<ImageView>(R.id.nextIcon)
        val previousIcon = view.findViewById<ImageView>(R.id.previousIcon)

        musicPlaying.observe(viewLifecycleOwner
        ) {
            if (it) {
                pauseIcon.isVisible = true
                playIcon.isVisible = false
            } else {
                playIcon.isVisible = true
                pauseIcon.isVisible = false
            }
        }

        playPauseClickable.setOnClickListener {
            musicPlaying.value = !(musicPlaying.value)!!
        }

        nextIcon.setOnClickListener {
            val nextSongCounter = songListViewModel.currentSongCounter + 1
            songListViewModel.currentSongCounter = nextSongCounter
            songListViewModel.currentSong.value = songListViewModel.queuedSongsLiveData.value!![nextSongCounter]
        }

        previousIcon.setOnClickListener {
            val previousSongCounter = songListViewModel.currentSongCounter - 1
            if (previousSongCounter >= 0 ) {
                songListViewModel.currentSong.value =
                    songListViewModel.queuedSongsLiveData.value!![previousSongCounter]
                songListViewModel.currentSongCounter = previousSongCounter
            }
            else {
                Toast.makeText(context, "No previous songs in queue", Toast.LENGTH_SHORT).show()
            }
        }


        return view

    }



}