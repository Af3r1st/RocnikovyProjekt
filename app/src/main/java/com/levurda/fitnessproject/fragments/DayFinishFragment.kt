package com.levurda.fitnessproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.levurda.fitnessproject.MyApp
import com.levurda.fitnessproject.R
import com.levurda.fitnessproject.databinding.DayFinishBinding
import com.levurda.fitnessproject.utils.FragmentManager
import com.levurda.fitnessproject.utils.MainViewModel
import pl.droidsonroids.gif.GifDrawable

class DayFinishFragment : Fragment() {

    private lateinit var binding: DayFinishBinding
    private var ab: ActionBar? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity().application as MyApp).viewModel

        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.done)

        val gif = GifDrawable(resources.assets, "congratulation.gif")
        binding.imMain.setImageDrawable(gif)

        binding.bDone.setOnClickListener {
            viewModel.saveDayList() // ✅ klíčové: uloží denní seznam i po odhlášení
            FragmentManager.setFragment(DaysFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFinishFragment()
    }
}
