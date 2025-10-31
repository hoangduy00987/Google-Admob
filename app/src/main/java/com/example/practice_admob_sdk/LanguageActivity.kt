package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_admob_sdk.Adapter.LanguageAdapter
import com.example.practice_admob_sdk.Model.LanguageItem
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd

class LanguageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivTick: ImageView
    private var selectedLanguage: LanguageItem? = null
    private lateinit var nativeAdContainer: LinearLayout

    companion object {
        var preloadedNativeFull: NativeAd? = null
        var preloadedOnboarding1: NativeAd? = null
        var preloadedOnboarding3: NativeAd? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.language)

        recyclerView = findViewById(R.id.recyclerLanguages)
        ivTick = findViewById(R.id.ivTick)
        nativeAdContainer = findViewById(R.id.nativeAdContainer)

        MobileAds.initialize(this)

        loadAd()
        preloadNativeFullAd {
            preloadOnboardingAds()
        }

        ivTick.setOnClickListener {
            selectedLanguage?.let {
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val languages = mutableListOf(
            LanguageItem("Hindi", R.drawable.india),
            LanguageItem("Spanish", R.drawable.spain),
            LanguageItem("French", R.drawable.french),
            LanguageItem("English", R.drawable.america),
            LanguageItem("Portuguese", R.drawable.portuguese),
            LanguageItem("Korean", R.drawable.korean),
            LanguageItem("Japanese", R.drawable.japanese)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LanguageAdapter(languages.toMutableList()) { selected ->
            selectedLanguage = selected
            ivTick.visibility = View.VISIBLE
        }
    }

    private fun loadAd() {
        val adView = IntersitialAdActivity.splashNativeAdView
        if (adView != null) {
            (adView.parent as? ViewGroup)?.removeView(adView)
            nativeAdContainer.apply {
                visibility = View.VISIBLE
                removeAllViews()
                addView(adView)
            }
            Log.d("AdShow", "Language Ad displayed successfully")
        } else {
            Log.w("AdShow", "No cached ad found for Language screen")
        }
    }

    private fun preloadNativeFullAd(onLoaded: () -> Unit) {
        val adUnitId = "ca-app-pub-3940256099942544/2247696110"
        val adLoader = AdLoader.Builder(this, adUnitId)
            .forNativeAd { ad ->
                preloadedNativeFull = ad
                Log.d("AdPreload", "Native Full Ad loaded successfully")
                onLoaded()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdPreload", "Failed to load Native Full: ${error.message}")
                    onLoaded()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun preloadOnboardingAds() {
        preloadNativeAd("ca-app-pub-3940256099942544/2247696110", "onboarding_1")
        preloadNativeAd("ca-app-pub-3940256099942544/2247696110", "onboarding_3")
    }

    private fun preloadNativeAd(adUnitId: String, tag: String) {
        val adLoader = AdLoader.Builder(this, adUnitId)
            .forNativeAd { ad ->
                when (tag) {
                    "onboarding_1" -> preloadedOnboarding1 = ad
                    "onboarding_3" -> preloadedOnboarding3 = ad
                }
                Log.d("AdPreload", "Preloaded Ad: $tag successfully")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdPreload", "Failed to preload $tag: ${error.message}")
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
