package com.example.aurameditation

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


//class ChangeScreenBrightnessUseSeekbarActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_change_screen_brightness_use_seekbar)
//        title = "dev2qa.com - Seekbar Change Screen Brightness Example."
//
//        // Get display screen brightness value text view object.
//        val screenBrightnessValueTextView =
//            findViewById<View>(R.id.change_screen_brightness_value_text_view) as TextView
//
//        // Get the seekbar instance.
//        val seekBar = findViewById<View>(R.id.change_screen_brightness_seekbar) as SeekBar
//        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val context = applicationContext
//                val canWriteSettings = Settings.System.canWrite(context)
//                if (canWriteSettings) {
//
//                    // Because max screen brightness value is 255
//                    // But max seekbar value is 100, so need to convert.
//                    val screenBrightnessValue = i * 255 / 100
//
//                    // Set seekbar adjust screen brightness value in the text view.
//                    screenBrightnessValueTextView.text =
//                        SCREEN_BRIGHTNESS_VALUE_PREFIX + screenBrightnessValue
//
//                    // Change the screen brightness change mode to manual.
//                    Settings.System.putInt(
//                        context.contentResolver,
//                        Settings.System.SCREEN_BRIGHTNESS_MODE,
//                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
//                    )
//                    // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
//                    // It will also change the screen brightness for the device.
//                    Settings.System.putInt(
//                        context.contentResolver,
//                        Settings.System.SCREEN_BRIGHTNESS,
//                        screenBrightnessValue
//                    )
//                } else {
//                    // Show Can modify system settings panel to let user add WRITE_SETTINGS permission for this app.
//                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//                    context.startActivity(intent)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {}
//        })
//
//        //Getting Current screen brightness.
//        val currBrightness =
//            Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0)
//        // Set current screen brightness value in the text view.
//        screenBrightnessValueTextView.text = SCREEN_BRIGHTNESS_VALUE_PREFIX + currBrightness
//        // Set current screen brightness value to seekbar progress.
//        seekBar.progress = currBrightness
//    }
//
//    companion object {
//        private const val SCREEN_BRIGHTNESS_VALUE_PREFIX =
//            "Current device screen brightness value is "
//    }
//}