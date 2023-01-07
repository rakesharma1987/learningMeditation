package com.example.aurameditation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurameditation.databinding.ActivitySongListBinding

class SongListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongListBinding
    private lateinit var musicList: ArrayList<Music>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val assetManager = this.assets
        val stringAray: Array<String> = assetManager.list("mp3") as Array<String>
        var music = Music()
        musicList = ArrayList<Music>()
        for (title in stringAray){
            music.isPremium = GooglePlayBillingPreferences.isPremium()
            music.title = title
            musicList.add(music)
        }

        val adapter = MusicAdapter(this, musicList)
        binding.recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        if(PlayerActivity.musicService != null) binding.nowPlaying.visibility = View.VISIBLE
    }
}