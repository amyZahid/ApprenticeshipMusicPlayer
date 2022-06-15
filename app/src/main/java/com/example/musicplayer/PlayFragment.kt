package com.example.musicplayer

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible

class PlayFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_play_layout, container, false)

        var shuffleActivated : Boolean = false
        val shuffleIcon = view.findViewById<ImageView>(R.id.shuffleIcon)
        shuffleIcon.setOnClickListener {
            if (!shuffleActivated) {
                shuffleActivated = true
                shuffleIcon.setColorFilter(resources.getColor(R.color.highlight_purple))
            } else {
                shuffleActivated = false
                shuffleIcon.setColorFilter(resources.getColor(R.color.text_white))
            }
        }

        val playPauseClickable = view.findViewById<ConstraintLayout>(R.id.playPauseArea)
        val playIcon = view.findViewById<ImageView>(R.id.playIcon)
        val pauseIcon = view.findViewById<ImageView>(R.id.pauseIcon)

        playPauseClickable.setOnClickListener {
            if (playIcon.isVisible) {
                playIcon.isVisible = false
                pauseIcon.isVisible = true
            } else {
                playIcon.isVisible = true
                pauseIcon.isVisible = false
            }
        }



        return view

    }



}