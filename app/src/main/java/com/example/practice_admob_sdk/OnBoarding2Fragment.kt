package com.example.practice_admob_sdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnBoarding2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onboarding_2, container, false)
        val btnNext = view.findViewById<TextView>(R.id.btNext)

        btnNext.setOnClickListener {
            (activity as? OnboardingActivity)?.openFragment(OnBoarding3Fragment())
        }

        return view
    }
}
