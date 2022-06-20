package com.example.musicplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongListViewModel : ViewModel() {
    var songListLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var currentSong = MutableLiveData<AudioModel>()
    var currentSongCounter : Int = 0
    var queuedSongsLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var isPlaying = MutableLiveData<Boolean>()
    var shuffleActivated : Boolean = false
    var playlists = MutableLiveData<ArrayList<PlaylistModel>>()
    var shuffledQueue = MutableLiveData<ArrayList<AudioModel>>()

    fun updateSongList(songList : ArrayList<AudioModel>) {
        songListLiveData.value = songList
    }

    fun updatePlaylists(playlistList : ArrayList<PlaylistModel>) {
        playlists.value = playlistList
    }

}