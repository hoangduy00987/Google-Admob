package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAdView

class OnBoarding1Activity: AppCompatActivity() {
    private lateinit var indicators: List<View>
    private var  currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_1)


        val btnNext = findViewById<TextView>(R.id.btNext)
        btnNext.setOnClickListener {
            val intent = Intent(this@OnBoarding1Activity, NativeFullActivity::class.java)
            startActivity(intent)

        }
        MobileAds.initialize(this) {}

        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                Log.d("AdLoad", "Native Ad loaded successfully")
                val adView = layoutInflater.inflate(R.layout.onboard_native_ad, null) as NativeAdView

                // Ánh xạ view
                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                adView.mediaView = adView.findViewById(R.id.ad_media)
                adView.iconView = adView.findViewById(R.id.ad_icon)
                adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
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
                    Log.e("OnBoard1", "❌ Failed to load native ad: ${p0.message}")
                }
            }).build()

        adLoader.loadAd(AdRequest.Builder().build())

    }


}