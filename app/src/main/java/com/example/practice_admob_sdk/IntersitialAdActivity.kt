package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class IntersitialAdActivity : ComponentActivity() {

    private var interstitialAd: InterstitialAd? = null
    private val adUnitId = "ca-app-pub-3940256099942544/1033173712" // Test ad unit
    private val nextPageDelay = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo AdMob
        MobileAds.initialize(this)

        // Load Interstitial Ad
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    showInterstitialAd()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    goToNextPage()
                }
            }
        )
    }

    private fun showInterstitialAd() {
        interstitialAd?.apply {
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    goToNextPage()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    goToNextPage()
                }
            }
            show(this@IntersitialAdActivity)
        } ?: goToNextPage()
    }

    private fun goToNextPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LanguageActivity::class.java))
            finish()
        }, nextPageDelay)
    }
}
