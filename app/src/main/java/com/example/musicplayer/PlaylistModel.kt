package com.example.musicplayer

import java.io.Serializable

public class PlaylistModel(
    val playlistName : String,
    val songList : ArrayList<AudioModel>
) : Serializable

