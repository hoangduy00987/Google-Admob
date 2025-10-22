package com.example.practice_admob_sdk

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnBoarding2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_2)
        val btnNext = findViewById<TextView>(R.id.btNext)
        btnNext.setOnClickListener {
            val intent = Intent(this@OnBoarding2Activity, OnBoarding3Activity::class.java)
            startActivity(intent)
        }
    }
}