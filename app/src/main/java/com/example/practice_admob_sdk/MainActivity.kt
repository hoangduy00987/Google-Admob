package com.example.practice_admob_sdk

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // chạy từ 1 - 100
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = 3000
        animation.start()

        animation.addUpdateListener { animator ->
            val progress = animator.animatedValue as Int
            if(progress == 100){
                val  intent = Intent(this, IntersitialAdActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}