package com.levurda.fitnessproject.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.levurda.fitnessproject.R

object FragmentManager {
    var currentFragment : Fragment? = null

    fun setFragment(newFragment: Fragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        currentFragment = newFragment
    }
}
