package com.example.practice_admob_sdk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class OnBoarding3Fragment : Fragment() {

    private lateinit var btnNext: TextView
    private lateinit var nativeAdContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onboarding_3, container, false)

        btnNext = view.findViewById(R.id.btNext)
        nativeAdContainer = view.findViewById(R.id.nativeAdContainer)

        // ✅ Gán quảng cáo đã preload từ LanguageActivity
        val cachedAd = LanguageActivity.preloadedOnboarding3
        if (cachedAd != null) {
            Log.d("AdShow", "✅ Showing preloaded onboarding_3 ad")
            showNativeAd(cachedAd)
        } else {
            Log.w("AdShow", "⚠️ No cached ad found for onboarding_3")
            nativeAdContainer.visibility = View.GONE
        }

        btnNext.setOnClickListener {
            // 👉 Chuyển sang fragment tiếp theo (ví dụ OnBoarding4Fragment)
            (activity as? OnboardingActivity)?.openFragment(OnBoarding1Fragment())
        }

        return view
    }

    // ✅ Hiển thị NativeAd vào layout
    private fun showNativeAd(nativeAd: NativeAd) {
        val adView = layoutInflater.inflate(R.layout.onboard_native_ad, null) as NativeAdView

        adView.apply {
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action)
            mediaView = findViewById(R.id.ad_media)
            iconView = findViewById(R.id.ad_icon)
            advertiserView = findViewById(R.id.ad_advertiser)
        }

        // Gán dữ liệu quảng cáo
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

        adView.setNativeAd(nativeAd)

        // Hiển thị trong container
        nativeAdContainer.apply {
            visibility = View.VISIBLE
            removeAllViews()
            addView(adView)
        }
    }
}
