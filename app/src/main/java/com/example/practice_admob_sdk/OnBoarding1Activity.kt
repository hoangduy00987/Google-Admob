package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class OnBoarding1Activity : AppCompatActivity() {

    private lateinit var btnNext: TextView
    private lateinit var nativeAdContainer: LinearLayout
    private val adUnitId = "ca-app-pub-3940256099942544/2247696110" // Test ID của Google

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.onboarding_1)

        btnNext = findViewById(R.id.btNext)
        nativeAdContainer = findViewById(R.id.nativeAdContainer)

        btnNext.setOnClickListener {
            startActivity(Intent(this, NativeFullActivity::class.java))
        }

        MobileAds.initialize(this) {}

        loadNativeAd()
    }

    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(this, adUnitId)
            .forNativeAd { nativeAd ->
                Log.d("AdLoad", "✅ Native Ad loaded successfully")
                showNativeAd(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdLoad", "❌ Failed to load native ad: ${error.message}")
                    nativeAdContainer.visibility = View.GONE
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun showNativeAd(nativeAd: NativeAd) {
        // Inflate layout quảng cáo gốc
        val adView = layoutInflater.inflate(R.layout.onboard_native_ad, null) as NativeAdView

        // Ánh xạ các view thành phần
        adView.apply {
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action)
            mediaView = findViewById(R.id.ad_media)
            iconView = findViewById(R.id.ad_icon)
            advertiserView = findViewById(R.id.ad_advertiser)
        }

        // Gán dữ liệu từ quảng cáo
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body ?: ""
        (adView.callToActionView as Button).text = nativeAd.callToAction ?: "Learn More"

        nativeAd.mediaContent?.let { adView.mediaView?.mediaContent = it }

        nativeAd.icon?.let {
            (adView.iconView as ImageView).setImageDrawable(it.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } ?: run { adView.iconView?.visibility = View.GONE }

        nativeAd.advertiser?.let {
            (adView.advertiserView as TextView).text = it
            adView.advertiserView?.visibility = View.VISIBLE
        } ?: run { adView.advertiserView?.visibility = View.GONE }

        // Gán NativeAd vào view
        adView.setNativeAd(nativeAd)

        // Hiển thị trong container
        nativeAdContainer.apply {
            visibility = View.VISIBLE
            removeAllViews()
            addView(adView)
        }
    }
}
