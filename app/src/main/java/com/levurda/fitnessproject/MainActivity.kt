package com.levurda.fitnessproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.levurda.fitnessproject.fragments.DaysFragment
import com.levurda.fitnessproject.utils.FragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Spustíme úvodní fragment přes FragmentManager
        FragmentManager.setFragment(DaysFragment.newInstance(), this)
    }

    override fun onBackPressed() {
        if (FragmentManager.currentFragment is DaysFragment)super.onBackPressed()
        else FragmentManager.setFragment(DaysFragment.newInstance(), this)
    }
}
