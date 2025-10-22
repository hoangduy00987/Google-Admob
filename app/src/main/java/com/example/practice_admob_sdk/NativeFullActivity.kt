package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class NativeFullActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_full)
        loadNativeAd()
        val adclose = findViewById<ImageView>(R.id.ad_close)

        adclose.setOnClickListener {
            val intent = Intent(this@NativeFullActivity, OnBoarding2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110") // ID test
            .forNativeAd { nativeAd ->
                val adView = findViewById<NativeAdView>(R.id.native_ad_view)
                populateNativeAdView(nativeAd, adView)
            }
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)

        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction

        nativeAd.icon?.let {
            (adView.iconView as ImageView).setImageDrawable(it.drawable)
        }

        adView.setNativeAd(nativeAd)
    }
}