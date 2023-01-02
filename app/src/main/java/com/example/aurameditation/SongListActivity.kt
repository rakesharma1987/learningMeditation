package com.example.aurameditation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aurameditation.databinding.ActivitySongListBinding

class SongListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val assetManager = this.assets
        val stringAray: Array<String> = assetManager.list("mp3") as Array<String>
        Log.d("TAG", "onCreate: "+stringAray)

        val adapter = SongAdapter(stringAray, this)
        binding.recyclerView.adapter = adapter

    }

    private fun getListOfFilesFromAssetFolder(path: String, context: Context): ArrayList<String>{
        val listOfAudioFiles = ArrayList<String>()
        context.assets.list(path)?.forEach { file ->
            val innerFiles = getListOfFilesFromAssetFolder("$path/#file", context)
            if (innerFiles.isNotEmpty()){
                listOfAudioFiles.addAll(innerFiles)
            }else{
                Toast.makeText(this, "Empty folder", Toast.LENGTH_SHORT).show()
            }
        }
        return listOfAudioFiles
    }
}