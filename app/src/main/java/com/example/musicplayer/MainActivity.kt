package com.example.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer : MediaPlayer
    lateinit var currentSong: AudioModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        if(!checkPermission()) {
            requestPermission()
            return
        }

        var songList : ArrayList<AudioModel> = ArrayList()

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

        val musicFragment = MusicFragment()
        val playFragment = PlayFragment()
        val searchFragment = SearchFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val upNextPlayNextIcon = findViewById<ImageView>(R.id.upNextPlayNextIcon)

        val bundle = Bundle()

        bundle.putSerializable("bundle_key", songList)
        musicFragment.arguments = bundle

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
            Toast.makeText(this, "Up Next Button Clicked", Toast.LENGTH_SHORT).show()
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
}