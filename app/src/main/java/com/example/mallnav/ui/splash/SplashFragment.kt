package com.example.mallnav.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mallnav.R
import com.example.mallnav.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val navigateToHome = Runnable {
        if (!isAdded) return@Runnable
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.splashFragment) {
            navController.navigate(R.id.action_splash_to_home)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val delayMs = resources.getInteger(R.integer.splash_delay_ms).toLong()
        view.postDelayed(navigateToHome, delayMs)
    }

    override fun onDestroyView() {
        binding.root.removeCallbacks(navigateToHome)
        _binding = null
        super.onDestroyView()
    }
}
