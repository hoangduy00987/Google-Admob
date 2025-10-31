package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class OnBoarding1Fragment : Fragment() {

    private lateinit var btnNext: TextView
    private lateinit var nativeAdContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onboarding_1, container, false)
        btnNext = view.findViewById(R.id.btNext)
        nativeAdContainer = view.findViewById(R.id.nativeAdContainer)

        val preloadedAd = LanguageActivity.preloadedOnboarding1
        if (preloadedAd != null) {
            Log.d("AdDisplay", "✅ Showing preloaded ad in OnBoarding1Fragment")
            showNativeAd(preloadedAd)
        } else {
            nativeAdContainer.visibility = View.GONE
        }

        // 👉 Sửa đoạn này để mở Activity NativeFullActivity
        btnNext.setOnClickListener {
            val intent = Intent(requireContext(), NativeFullActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showNativeAd(nativeAd: NativeAd) {
        val adView = layoutInflater.inflate(R.layout.onboard_native_ad, null) as NativeAdView

        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as TextView).text = nativeAd.callToAction ?: "Learn more"

        nativeAd.icon?.let {
            (adView.iconView as? ImageView)?.setImageDrawable(it.drawable)
        }

        nativeAd.mediaContent?.let {
            adView.mediaView?.mediaContent = it
        }

        adView.setNativeAd(nativeAd)

        nativeAdContainer.removeAllViews()
        nativeAdContainer.addView(adView)
        nativeAdContainer.visibility = View.VISIBLE
    }
}
