package com.example.practice_admob_sdk

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class MainActivity : AppCompatActivity() {

    companion object {
        var interstitialAd: InterstitialAd? = null
        var sharedNativeAd: NativeAd? = null
    }

    private val nativeAdUnitId = "ca-app-pub-3940256099942544/2247696110" // Native Ad test ID
    private val interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712" // Interstitial test ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        MobileAds.initialize(this)

        // ✅ Load Native Ad trước (để hiển thị ngay trong Onboarding)
        loadNativeAd()
        // Khởi tạo Mobile Ads SDK

        // ✅ Load Interstitial song song
        loadInterstitialAd()

        // Giả lập animation loading
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = 3000
        animation.start()

        animation.addUpdateListener {
            if ((it.animatedValue as Int) == 100) {
                val intent = Intent(this, IntersitialAdActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun loadNativeAd() {
        // Nếu có ad cũ, hủy trước khi load mới
        sharedNativeAd?.destroy()
        sharedNativeAd = null

        val adLoader = AdLoader.Builder(this, nativeAdUnitId)
            .forNativeAd { ad: NativeAd ->
                sharedNativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    sharedNativeAd = null
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            interstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

}
