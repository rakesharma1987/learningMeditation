package com.example.aurameditation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.provider.Settings.Global.getInt
import android.provider.Settings.Secure.getInt
import android.provider.Settings.SettingNotFoundException
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.provider.Settings.System.putInt
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getInt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.aurameditation.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManagerFactory
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
//    private var brightness: Int = 0
    private var count: Int = 1
    private var liveDataVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar!!.hide()

        MobileAds.initialize(this)
        adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        var screenBrightnessValue = 0
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val context = applicationContext
                val canWriteSettings = Settings.System.canWrite(context)
                if (canWriteSettings) {
                    // Because max screen brightness value is 255
                    // But max seekbar value is 100, so need to convert.
                    screenBrightnessValue = i * 255 / 100

                    // Change the screen brightness change mode to manual.
                    putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
                    // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
                    // It will also change the screen brightness for the device.
                    putInt(context.contentResolver, SCREEN_BRIGHTNESS, screenBrightnessValue)
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    startActivity(intent)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekBar.progress = screenBrightnessValue
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                seekBar.progress = screenBrightnessValue
            }
        })

        val currBrightness = Settings.System.getInt(contentResolver, SCREEN_BRIGHTNESS, 0)
        binding.seekBar.progress = currBrightness

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
        binding.rootLayout.setOnClickListener(this)

        binding.ivImage1.setOnClickListener(this)
        binding.ivImage2.setOnClickListener(this)
        binding.ivImage3.setOnClickListener(this)
        binding.ivImage4.setOnClickListener(this)
        binding.ivImage5.setOnClickListener(this)

        binding.ivAi1.setOnClickListener(this)
        binding.ivAi2.setOnClickListener(this)
        binding.ivAi3.setOnClickListener(this)
        binding.ivAi4.setOnClickListener(this)
        binding.ivAi5.setOnClickListener(this)

//        Glide.with(this).load("https://media.giphy.com/media/SKGo6OYe24EBG/giphy.gif").into(binding.ivAi1)
        Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivAi1)
        Glide.with(this).load(R.drawable.ic_ai2).into(binding.ivAi2)
        Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivAi3)
        Glide.with(this).load(R.drawable.ic_ai2).into(binding.ivAi4)
        Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivAi5)

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
                showInterstitialAds()

            }

            R.id.iv_songs ->{
                // TODO: code here for premimum
                startActivity(Intent(this@MainActivity, PremiumActivity::class.java))
                showInterstitialAds()
            }

            R.id.iv_premium ->{
                // TODO: code here for songs
                startActivity(Intent(this@MainActivity, SongListActivity::class.java))
                showInterstitialAds()
            }

            R.id.iv_rate ->{
                showInterstitialAds()
                openAppInPlayStore()
            }

            R.id.iv_share ->{
                this.finish()
            }

            R.id.iv_exit ->{
                val intent= Intent()
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,"Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,"Share To:"))
                showInterstitialAds()
            }

            R.id.iv_color1 ->{
                val colorDrawable = binding.ivColor1.background as ColorDrawable
                binding.rootLayout.setBackgroundColor(colorDrawable.color)

                showInterstitialAds()
            }

            R.id.iv_color2 ->{
                val colorDrawable = binding.ivColor2.background as ColorDrawable
                binding.rootLayout.setBackgroundColor(colorDrawable.color)

                showInterstitialAds()
            }

            R.id.iv_color3 ->{
                showInterstitialAds()

            }

            R.id.iv_color4 ->{
                showInterstitialAds()
            }

            R.id.iv_color5 ->{
                showInterstitialAds()
            }

            R.id.root_layout ->{
                liveDataVisible.value = binding.seekBar.visibility == View.VISIBLE
            }

            R.id.iv_image1 ->{
                binding.rootLayout.background = resources.getDrawable(R.drawable.image_1)
            }

            R.id.iv_image2 ->{
                binding.rootLayout.background = resources.getDrawable(R.drawable.image_2)
            }

            R.id.iv_image3 ->{
                binding.rootLayout.background = resources.getDrawable(R.drawable.image_3)
            }

            R.id.iv_image4 ->{
                binding.rootLayout.background = resources.getDrawable(R.drawable.image_4)
            }

            R.id.iv_image5 ->{
                binding.rootLayout.background = resources.getDrawable(R.drawable.image_5)
            }

            R.id.iv_ai1 ->{
                binding.ivRoot.visibility = View.VISIBLE
                Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivRoot)
            }

            R.id.iv_ai2 ->{
                Glide.with(this).load(R.drawable.ic_ai2).into(binding.ivRoot)
            }

            R.id.iv_ai3 ->{
                Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivRoot)
            }

            R.id.iv_ai4 ->{
                Glide.with(this).load(R.drawable.ic_ai2).into(binding.ivRoot)
            }

            R.id.iv_ai5 ->{
                Glide.with(this).load(R.drawable.ic_ai1).into(binding.ivRoot)
            }


        }

        liveDataVisible.observe(this, Observer {
            if (it){
                binding.ivColor1.visibility = View.GONE
                binding.ivColor2.visibility = View.GONE
                binding.ivColor3.visibility = View.GONE
                binding.ivColor4.visibility = View.GONE
                binding.ivColor5.visibility = View.GONE

                binding.ivColorPlate.visibility = View.GONE
                binding.tvColor.visibility = View.GONE
                binding.ivSongs.visibility = View.GONE
                binding.tvSongs.visibility = View.GONE
                binding.ivPremium.visibility = View.GONE
                binding.tvPremium.visibility = View.GONE
                binding.ivRate.visibility = View.GONE
                binding.tvRate.visibility = View.GONE
                binding.ivShare.visibility = View.GONE
                binding.tvShare.visibility = View.GONE
                binding.ivExit.visibility = View.GONE
                binding.tvExit.visibility = View.GONE

                binding.seekBar.visibility = View.GONE

                binding.ivImage1.visibility = View.GONE
                binding.ivImage2.visibility = View.GONE
                binding.ivImage3.visibility = View.GONE
                binding.ivImage4.visibility = View.GONE
                binding.ivImage5.visibility = View.GONE

                binding.ivAi1.visibility = View.GONE
                binding.ivAi2.visibility = View.GONE
                binding.ivAi3.visibility = View.GONE
                binding.ivAi4.visibility = View.GONE
                binding.ivAi5.visibility = View.GONE

                binding.ivRoot.visibility = View.GONE
            }else{
                binding.ivColor1.visibility = View.VISIBLE
                binding.ivColor2.visibility = View.VISIBLE
                binding.ivColor3.visibility = View.VISIBLE
                binding.ivColor4.visibility = View.VISIBLE
                binding.ivColor5.visibility = View.VISIBLE

                binding.ivColorPlate.visibility = View.VISIBLE
                binding.tvColor.visibility = View.VISIBLE
                binding.ivSongs.visibility = View.VISIBLE
                binding.tvSongs.visibility = View.VISIBLE
                binding.ivPremium.visibility = View.VISIBLE
                binding.tvPremium.visibility = View.VISIBLE
                binding.ivRate.visibility = View.VISIBLE
                binding.tvRate.visibility = View.VISIBLE
                binding.ivShare.visibility = View.VISIBLE
                binding.tvShare.visibility = View.VISIBLE
                binding.ivExit.visibility = View.VISIBLE
                binding.tvExit.visibility = View.VISIBLE

                binding.seekBar.visibility = View.VISIBLE

                binding.tvColor.setTextColor(getColor(R.color.white))
                binding.tvSongs.setTextColor(getColor(R.color.white))
                binding.tvPremium.setTextColor(getColor(R.color.white))
                binding.tvRate.setTextColor(getColor(R.color.white))
                binding.tvShare.setTextColor(getColor(R.color.white))
                binding.tvExit.setTextColor(getColor(R.color.white))

                binding.ivImage1.visibility = View.VISIBLE
                binding.ivImage2.visibility = View.VISIBLE
                binding.ivImage3.visibility = View.VISIBLE
                binding.ivImage4.visibility = View.VISIBLE
                binding.ivImage5.visibility = View.VISIBLE

                binding.ivAi1.visibility = View.VISIBLE
                binding.ivAi2.visibility = View.VISIBLE
                binding.ivAi3.visibility = View.VISIBLE
                binding.ivAi4.visibility = View.VISIBLE
                binding.ivAi5.visibility = View.VISIBLE
            }
        })
    }

    // Check whether this app has android write settings permission.
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteSettingsPermission(context: Context): Boolean {
        var ret = true
        // Get the result from below code.
        ret = Settings.System.canWrite(context)
        return ret
    }

    // Start can modify system settings panel to let user change the write
    // settings permission.
    private fun changeWriteSettingsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        context.startActivity(intent)
    }

    // This function only take effect in real physical android device,
    // it can not take effect in android emulator.
    private fun changeScreenBrightness(context: Context, screenBrightnessValue: Int) {
        // Change the screen brightness change mode to manual.
        putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        // Apply the screen brightness value to the system, this will change
        // the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        )
    }

    fun showInterstitialAds(){
        InterstitialAd.load(this,getString(R.string.interstitial_adunit_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("TAG", adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("TAG", "Ad was loaded.")
                mInterstitialAd = interstitialAd
                mInterstitialAd!!.show(this@MainActivity)
            }
        })
    }

    fun openAppInPlayStore() {
        val uri = Uri.parse("market://details?id=" + applicationContext.packageName)
        val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        goToMarketIntent.addFlags(flags)

        try {
            startActivity(goToMarketIntent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName))

            startActivity(intent)
        }
    }

}