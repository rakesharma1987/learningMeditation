package com.example.aurameditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.aurameditation.databinding.ActivityPremiumBinding

class PremiumActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Buy Premium"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        binding.btnBuyPremium.setOnClickListener(this)
        binding.btnRemoveAd.setOnClickListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_buy_premium ->{
                Toast.makeText(this, "Will implement soon", Toast.LENGTH_SHORT).show()
            }

            R.id.btn_remove_ad ->{
                Toast.makeText(this, "Will implement soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
}