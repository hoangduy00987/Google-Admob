package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class NativeFullActivity : ComponentActivity() {

    private var startX = 0f
    private var startY = 0f
    private var isSwiping = false
    private val SWIPE_THRESHOLD = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_full)

        val adView = findViewById<NativeAdView>(R.id.native_ad_view)
        val swipeOverlay = findViewById<View>(R.id.swipe_overlay)

        // Load sẵn quảng cáo
        val nativeAd = LanguageActivity.preloadedNativeFull
        if (nativeAd != null) {
            populateNativeAdView(nativeAd, adView)
        }

        // ✅ Overlay bắt gesture, chỉ chặn khi vuốt, không chặn click
        swipeOverlay.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    isSwiping = false
                }

                MotionEvent.ACTION_MOVE -> {
                    val diffX = event.x - startX
                    val diffY = event.y - startY
                    // Nếu người dùng di chuyển ngang > SWIPE_THRESHOLD => vuốt
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(diffX) > Math.abs(diffY)) {
                        isSwiping = true
                        return@setOnTouchListener true  // ✅ chặn event vuốt
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (isSwiping) {
                        // ✅ Người dùng vừa vuốt —> KHÔNG gửi ACTION_UP xuống quảng cáo
                        handleSwipe(event.x - startX)
                        return@setOnTouchListener true
                    }
                }
            }
            // ✅ Không vuốt => cho event đi xuống quảng cáo (SDK xử lý click)
            false
        }
    }

    // ✅ Nếu vuốt đủ xa, chuyển trang
    private fun handleSwipe(diffX: Float) {
        if (Math.abs(diffX) < 20) return // không đủ xa thì bỏ qua
        if (diffX > 0) goToNextActivity() // Vuốt sang phải
        else goToPreviousActivity()       // Vuốt sang trái
    }

    private fun goToNextActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.putExtra("target_fragment", "onboarding_2")
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun goToPreviousActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.putExtra("target_fragment", "onboarding_1")
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    // ✅ Gán dữ liệu vào ad view
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_icon)

        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction ?: "Install"

        nativeAd.icon?.let {
            (adView.iconView as ImageView).setImageDrawable(it.drawable)
            adView.iconView?.visibility = View.VISIBLE
        } ?: run {
            adView.iconView?.visibility = View.GONE
        }

        adView.setNativeAd(nativeAd)
    }
}
