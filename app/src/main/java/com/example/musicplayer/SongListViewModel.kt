package com.example.musicplayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongListViewModel : ViewModel() {
    var songListLiveData = MutableLiveData<ArrayList<AudioModel>>()
    var currentSong = MutableLiveData<AudioModel>()
    var currentSongCounter : Int = 0
    var isPlaying = MutableLiveData<Boolean>()
    var shuffledQueue = MutableLiveData<ArrayList<AudioModel>>()

    fun updateSongList(songList : ArrayList<AudioModel>) {
        songListLiveData.value = songList
    }

}