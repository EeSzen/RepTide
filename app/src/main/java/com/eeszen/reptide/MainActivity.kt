package com.eeszen.reptide

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eeszen.reptide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.findNavController()
        binding.bottomNav.setupWithNavController(navController)

        // Show bottom nav
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.workoutFragment,
                R.id.exerciseFragment,
                R.id.historyFragment
                    -> showBottomNav(true)
                else
                    -> showBottomNav(false)
            }
        }

    }

    fun selectBottomNavItem(itemId: Int) {
        // Pop back to root before switching bottom tab
        navController.popBackStack(R.id.homeFragment, false)
        binding.bottomNav.selectedItemId = itemId
    }

    private fun showBottomNav(show: Boolean){
        binding.bottomNav.visibility = if (show) View.VISIBLE else View.GONE
    }
}