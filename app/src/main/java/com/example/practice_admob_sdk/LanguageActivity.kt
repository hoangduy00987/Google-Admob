package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAdView
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

class LanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.language)
        val ivTick = findViewById<ImageView>(R.id.ivTick)
        ivTick.setOnClickListener {
            val intent = Intent(this@LanguageActivity, OnBoarding1Activity::class.java)
            startActivity(intent)
        }

        val languageLayouts = listOf(
            findViewById<LinearLayout>(R.id.india),
            findViewById<LinearLayout>(R.id.spain),
            findViewById<LinearLayout>(R.id.france),
            findViewById<LinearLayout>(R.id.america),
            findViewById<LinearLayout>(R.id.portugal),
            findViewById<LinearLayout>(R.id.korea),
            findViewById<LinearLayout>(R.id.japan)
        )

        languageLayouts.forEach { layout ->
            layout.setOnClickListener {
                languageLayouts.forEach { it.isSelected = false }
                layout.isSelected = true
                ivTick.visibility = View.VISIBLE
            }
        }


        MobileAds.initialize(this) {}

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                Log.d("AdLoad", "Native Ad loaded successfully")
                val adView = layoutInflater.inflate(R.layout.native_ad_layout, null) as NativeAdView

                // Ánh xạ view
                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                adView.mediaView = adView.findViewById(R.id.ad_media)
                adView.iconView = adView.findViewById(R.id.ad_icon)
                adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
                Log.d("AdViewCheck", "headlineView: ${adView.headlineView}")
                Log.d("AdViewCheck", "bodyView: ${adView.bodyView}")
                Log.d("AdViewCheck", "callToActionView: ${adView.callToActionView}")
                Log.d("AdViewCheck", "mediaView: ${adView.mediaView}")
                Log.d("AdViewCheck", "iconView: ${adView.iconView}")
                Log.d("AdViewCheck", "advertiserView: ${adView.advertiserView}")
                // Gán dữ liệu
                (adView.headlineView as TextView).text = nativeAd.headline
                (adView.bodyView as TextView).text = nativeAd.body
                (adView.callToActionView as Button).text = nativeAd.callToAction

                nativeAd.mediaContent?.let { mediaContent ->
                    adView.mediaView?.mediaContent = mediaContent
                }

                nativeAd.icon?.let {
                    (adView.iconView as ImageView).setImageDrawable(it.drawable)
                    adView.iconView?.visibility = View.VISIBLE
                } ?: run {
                    adView.iconView?.visibility = View.GONE
                }

                nativeAd.advertiser?.let {
                    (adView.advertiserView as TextView).text = it
                    adView.advertiserView?.visibility = View.VISIBLE
                } ?: run {
                    adView.advertiserView?.visibility = View.GONE
                }

                adView.setNativeAd(nativeAd)

                // Hiển thị trong container
                findViewById<LinearLayout>(R.id.nativeAdContainer).apply {
                    visibility = View.VISIBLE
                    removeAllViews()
                    addView(adView)

                }
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("AdLoad", "❌ Failed to load native ad: ${p0.message}")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())


    }

}