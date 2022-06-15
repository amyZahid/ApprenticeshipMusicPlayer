package com.example.musicplayer

import android.media.Image
import java.io.Serializable

public class AudioModel(
    val songName: String,
    val songArtist: String,
    val songAlbum: String,
    val songPath: String
) : Serializable
