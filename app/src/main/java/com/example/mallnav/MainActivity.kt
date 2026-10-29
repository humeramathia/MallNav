package com.example.mallnav

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.mallnav.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.bind(findViewById(R.id.main))
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.navHostFragment.setPadding(bars.left, bars.top, bars.right, 0)
            binding.bottomNav.setPadding(0, 0, 0, bars.bottom)
            insets
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id == item.itemId) return@setOnItemSelectedListener true
            navController.navigate(
                item.itemId,
                null,
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(R.id.homeFragment, false, true)
                    .build(),
            )
            true
        }

        val hiddenTabs = setOf(
            R.id.splashFragment,
            R.id.confirmStartFragment,
            R.id.routePreviewFragment,
            R.id.navigationFragment,
            R.id.storeListFragment,
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showTabs = destination.id !in hiddenTabs
            binding.bottomNav.isVisible = showTabs
            binding.bottomNavDivider.isVisible = showTabs
            val selected = when (destination.id) {
                R.id.homeFragment, R.id.mallHomeFragment -> R.id.homeFragment
                R.id.mapFragment -> R.id.mapFragment
                R.id.directoryFragment, R.id.storeDetailsFragment -> R.id.directoryFragment
                R.id.profileFragment, R.id.placeholderFragment -> R.id.profileFragment
                else -> null
            }
            if (selected != null) {
                binding.bottomNav.menu.findItem(selected).isChecked = true
            }
        }
    }
}
