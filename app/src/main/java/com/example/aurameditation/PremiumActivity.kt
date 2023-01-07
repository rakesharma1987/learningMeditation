package com.example.aurameditation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.billingclient.api.*
import com.example.aurameditation.databinding.ActivityPremiumBinding

class PremiumActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPremiumBinding
    private lateinit var billingClient: BillingClient
    lateinit var skulList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GooglePlayBillingPreferences.init(this)

        supportActionBar!!.title = "Buy Premium"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        binding.btnBuyPremium.setOnClickListener(this)
        binding.btnRemoveAd.setOnClickListener(this)

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    if (purchases != null) {
                        handlePurchases(purchases)
                    }
                    GooglePlayBillingPreferences.savePremiumToPref(true)
                }else if(billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
                    GooglePlayBillingPreferences.savePremiumToPref(false)
                }
            }

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        skulList = ArrayList<String>()
        skulList.add("android.test.purchased")

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

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
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            val params = SkuDetailsParams.newBuilder()
                            params.setSkusList(skulList)
                                .setType(BillingClient.SkuType.INAPP)
                            billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailList ->

                                for (skuDetail in skuDetailList!!) {
                                    val flowPurchase = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetail)
                                        .build()

                                    val responseCode = billingClient.launchBillingFlow(this@PremiumActivity, flowPurchase).responseCode
                                    if (responseCode == 0) {
                                        GooglePlayBillingPreferences.savePremiumToPref(true)
                                    }
                                }
                            }
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        GooglePlayBillingPreferences.savePremiumToPref(false)
                    }
                })
            }

            R.id.btn_remove_ad ->{
                Toast.makeText(this, "Will implement soon", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient!!.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
            }
        }
    }
    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            GooglePlayBillingPreferences.savePremiumToPref(true)
            Toast.makeText(applicationContext, "Item Purchased", Toast.LENGTH_SHORT).show()
            recreate()
        }
    }
}