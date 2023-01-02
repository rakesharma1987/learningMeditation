package com.example.aurameditation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aurameditation.databinding.LayoutItemSongBinding

class SongAdapter(private val list: Array<String>, private val context: Context): RecyclerView.Adapter<SongAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val layoutItemSongBinding: LayoutItemSongBinding): RecyclerView.ViewHolder(layoutItemSongBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding: LayoutItemSongBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_item_song, parent, false)
        return CustomViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.layoutItemSongBinding.tvSongTitle.text = list[position]

        holder.layoutItemSongBinding.ivPlayMusic.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, BackgroundSoundService::class.java)
            intent.putExtra("song", holder.layoutItemSongBinding.tvSongTitle.text)
            context.startForegroundService(intent)
            holder.layoutItemSongBinding.ivPlayMusic.setImageDrawable(context.getDrawable(R.drawable.ic_pause))
            holder.layoutItemSongBinding.ivPlayMusic.visibility = View.GONE
            holder.layoutItemSongBinding.ivStop.visibility = View.VISIBLE
        })

        holder.layoutItemSongBinding.ivStop.setOnClickListener(View.OnClickListener {
            context.stopService(Intent(context, BackgroundSoundService::class.java))
            holder.layoutItemSongBinding.ivPlayMusic.setImageDrawable(context.getDrawable(R.drawable.ic_play))
            holder.layoutItemSongBinding.ivStop.visibility = View.GONE
            holder.layoutItemSongBinding.ivPlayMusic.visibility = View.VISIBLE
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}