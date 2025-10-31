package com.example.practice_admob_sdk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        // ✅ Nhận thông tin từ NativeFullActivity (fragment cần mở)
        val target = intent.getStringExtra("target_fragment")

        if (savedInstanceState == null) {
            when (target) {
                "onboarding_2" -> openFragment(OnBoarding2Fragment())
//                "onboarding_3" -> openFragment(OnBoarding3Fragment())
                else -> openFragment(OnBoarding1Fragment())
            }
        }
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showAdThenNext(nextFragment: Fragment) {
        val adView = IntersitialAdActivity.splashNativeAdView
        if (adView != null) {
            adView.postDelayed({
                openFragment(nextFragment)
            }, 2000)
        } else {
            openFragment(nextFragment)
        }
    }
}
