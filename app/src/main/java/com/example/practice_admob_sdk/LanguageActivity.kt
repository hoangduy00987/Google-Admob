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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_admob_sdk.Adapter.LanguageAdapter
import com.example.practice_admob_sdk.Model.LanguageItem
import com.google.android.gms.ads.*

import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class LanguageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ivTick: ImageView
    private var selectedLanguage: LanguageItem? = null
    private var currentNativeAd: NativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.language)

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerLanguages)
        ivTick = findViewById(R.id.ivTick)

        ivTick.setOnClickListener {
            selectedLanguage?.let {
                val intent = Intent(this, OnBoarding1Activity::class.java)
                startActivity(intent)
            }
        }

        // Danh sách ngôn ngữ
        val languages = mutableListOf(
            LanguageItem("Hindi", R.drawable.india),
            LanguageItem("Spanish", R.drawable.spain),
            LanguageItem("French", R.drawable.french),
            LanguageItem("English", R.drawable.america),
            LanguageItem("Portuguese", R.drawable.portuguese),
            LanguageItem("Korean", R.drawable.korean),
            LanguageItem("Japanese", R.drawable.japanese)
        )

        // Thiết lập RecyclerView
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            adapter = LanguageAdapter(languages) { selected ->
                selectedLanguage = selected
                ivTick.visibility = View.VISIBLE
            }
        }

        // Khởi tạo Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Tải quảng cáo Native
        loadNativeAd()
    }


    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                // Hủy quảng cáo cũ để tránh rò rỉ bộ nhớ
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd

                val adView = layoutInflater.inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(nativeAd, adView)


                findViewById<LinearLayout>(R.id.nativeAdContainer).apply {
                    visibility = View.VISIBLE
                    removeAllViews()
                    addView(adView)
                }

                Log.d("AdLoad", "✅ Native Ad loaded successfully")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdLoad", "❌ Failed to load native ad: ${error.message}")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

   // gán nội dung quảng cao
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // Gán nội dung quảng cáo
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction

        nativeAd.mediaContent?.let { adView.mediaView?.mediaContent = it }

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
    }

    // giải phòng quảng cáo khi activity bị hủy
    override fun onDestroy() {
        currentNativeAd?.destroy()
        currentNativeAd = null
        super.onDestroy()
    }
}
