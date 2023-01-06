package com.example.aurameditation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getInt
import android.provider.Settings.Secure.getInt
import android.provider.Settings.SettingNotFoundException
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.provider.Settings.System.putInt
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getInt
import androidx.databinding.DataBindingUtil
import com.example.aurameditation.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener




class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var brightness: Int = 0
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar!!.hide()

        MobileAds.initialize(this)
        val adrequest = AdRequest.Builder().build()
        binding.adView.loadAd(adrequest)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_SETTINGS), 101)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_SETTINGS), 101)
        }


        val contentResolver = contentResolver
        val window = window

        binding.seekBar.max = 255
        binding.seekBar.keyProgressIncrement = 1

        try {
            brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS)
        } catch (e: SettingNotFoundException) {
            Log.e("Error", "Cannot access system brightness")
            e.printStackTrace()
        }
        binding.seekBar.progress = brightness

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress <= 20){
                    brightness = 20
                }else{
                    brightness = progress
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        binding.ivColorPlate.setOnClickListener(this)
        binding.ivSongs.setOnClickListener(this)
        binding.ivPremium.setOnClickListener(this)
        binding.ivRate.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.ivExit.setOnClickListener(this)

        binding.ivColor1.setOnClickListener(this)
        binding.ivColor2.setOnClickListener(this)
        binding.ivColor3.setOnClickListener(this)
        binding.ivColor4.setOnClickListener(this)
        binding.ivColor5.setOnClickListener(this)

        binding.tvFullScreen.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.iv_color_plate ->{
                val dialog = AmbilWarnaDialog(this, 255, object:OnAmbilWarnaListener{
                    override fun onCancel(dialog: AmbilWarnaDialog?) {}

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        if (count % 2 == 0){
                            binding.ivColor1.setBackgroundColor(color)
                        }else{
                            binding.ivColor2.setBackgroundColor(color)
                        }
                        count++
                    }

                })
                dialog.show()
            }

            R.id.iv_songs ->{
                // TODO: code here for premimum
                startActivity(Intent(this@MainActivity, PremiumActivity::class.java))
            }

            R.id.iv_premium ->{
                // TODO: code here for songs
                startActivity(Intent(this@MainActivity, SongListActivity::class.java))
            }

            R.id.iv_rate ->{

            }

            R.id.iv_share ->{
                this.finish()
            }

            R.id.iv_exit ->{

            }

            R.id.iv_color1 ->{
                val colorDrawable = binding.ivColor1.background as ColorDrawable
                binding.rootLayout.setBackgroundColor(colorDrawable.color)
            }

            R.id.iv_color2 ->{
                val colorDrawable = binding.ivColor2.background as ColorDrawable
                binding.rootLayout.setBackgroundColor(colorDrawable.color)
            }

            R.id.iv_color3 ->{

            }

            R.id.iv_color4 ->{

            }

            R.id.iv_color5 ->{

            }
        }
    }
}