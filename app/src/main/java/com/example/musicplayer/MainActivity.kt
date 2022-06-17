package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer : MediaPlayer
    private val songListViewModel : SongListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        if(!checkPermission()) {
            requestPermission()
            return
        }

        mediaPlayer = MediaPlayer()

        songListViewModel.isPlaying.value = false

        val songList : ArrayList<AudioModel> = ArrayList()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection, selection, null, null )
        while (cursor?.moveToNext() == true) {
            val songData = AudioModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3))
            songList.add(songData)
        }

        songListViewModel.updateSongList(songList)

        val musicFragment = MusicFragment()
        val playFragment = PlayFragment()
        val searchFragment = SearchFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val upNextPlayNextIcon = findViewById<ImageView>(R.id.upNextPlayNextIcon)
        val upNextSongTitle = findViewById<TextView>(R.id.upNextSongTextView)

        setCurrentFragment(musicFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.musicFragment->{
                    setCurrentFragment(musicFragment)
                    upNextPlayNextIcon.isVisible = true
                }
                R.id.playFragment-> {
                    setCurrentFragment(playFragment)
                    upNextPlayNextIcon.isVisible = false
                }
                R.id.searchFragment->{
                    setCurrentFragment(searchFragment)
                    upNextPlayNextIcon.isVisible = true
                }

            }
            true
        }

        upNextPlayNextIcon.setOnClickListener {
            val nextSongCounter = songListViewModel.currentSongCounter + 1
            val queuedSongsSize = songListViewModel.queuedSongsLiveData.value!!.size
            if ((!songListViewModel.queuedSongsLiveData.value.isNullOrEmpty())&&( queuedSongsSize > nextSongCounter)){
                songListViewModel.currentSongCounter = nextSongCounter
                songListViewModel.currentSong.value = songListViewModel.queuedSongsLiveData.value!![songListViewModel.currentSongCounter]

            } else {
                Toast.makeText(this, "No songs queued", Toast.LENGTH_SHORT).show()
            }
        }

        songListViewModel.currentSong.observe(this
        ) {
            val queuedSongsSize = songListViewModel.queuedSongsLiveData.value!!.size
            val nextSongCounter = songListViewModel.currentSongCounter + 1
            if ((!songListViewModel.queuedSongsLiveData.value.isNullOrEmpty())&&( queuedSongsSize > nextSongCounter)){
                val nextSong = songListViewModel.queuedSongsLiveData.value?.get(nextSongCounter)?.songName
                upNextSongTitle.text = nextSong ?: "No song queued"
            } else {
                upNextSongTitle.text = "No songs queued"
                Toast.makeText(this, "No songs queued", Toast.LENGTH_SHORT).show()
            }
            newCurrentSong(it)

        }

        songListViewModel.isPlaying.observe(this
        ) {
            if (!it) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
            }
        }

    }

    private fun checkPermission(): Boolean {
        var result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
            Toast.makeText(this, "Read permission is required, please allow from settings", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
    }

    private fun setCurrentFragment(fragment:Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment,fragment)
            commit()
        }

    private fun newCurrentSong(currentSong: AudioModel) {
        if(songListViewModel.isPlaying.value == true) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        mediaPlayer = MediaPlayer.create(applicationContext, Uri.fromFile(File(currentSong.songPath)))
        mediaPlayer.start()
        songListViewModel.isPlaying.value = true
    }
}

