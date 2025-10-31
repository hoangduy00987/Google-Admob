package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class IntersitialAdActivity : ComponentActivity() {

    companion object {
        var splashNativeAd: NativeAd? = null
        var splashNativeAdView: NativeAdView? = null
    }

    private val nextPageDelay = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        loadLanguageNativeAd()
        showInterstitialAdOrSkip()
    }

    private fun showInterstitialAdOrSkip() {
        val ad = MainActivity.interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    MainActivity.interstitialAd = null
                    goToNextPage()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    MainActivity.interstitialAd = null
                    goToNextPage()
                }
            }
            ad.show(this@IntersitialAdActivity)
        } else {
            goToNextPage()
        }
    }

    private fun loadLanguageNativeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                splashNativeAd?.destroy()
                splashNativeAd = nativeAd

                // ✅ Inflate view một lần và gán toàn cục
                val adView = layoutInflater.inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                splashNativeAdView = adView

                Log.d("AdLoad", "✅ Native Ad loaded and view cached globally")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdLoad", "❌ Failed to load native ad: ${error.message}")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction

        nativeAd.mediaContent?.let { adView.mediaView?.mediaContent = it }

        nativeAd.icon?.let {
            (adView.iconView as ImageView).setImageDrawable(it.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } ?: run {
            adView.iconView?.visibility = View.GONE
            Log.w("AdCheck", "⚠️ This ad has no icon asset.")
        }

        adView.setNativeAd(nativeAd)
    }

    private fun goToNextPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LanguageActivity::class.java))
            finish()
        }, nextPageDelay)
    }
}
