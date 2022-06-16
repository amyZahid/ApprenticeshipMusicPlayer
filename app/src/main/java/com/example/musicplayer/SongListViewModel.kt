package com.example.musicplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongListViewModel : ViewModel() {
    var songListLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var currentSong = MutableLiveData<AudioModel>()
    var queuedSongsLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var previousSongsLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var isPlaying = MutableLiveData<Boolean>()

    fun updateSongList(songList : ArrayList<AudioModel>) {
        songListLiveData.value = songList
    }

}