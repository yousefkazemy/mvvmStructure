package com.example.mvvmstructure.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmstructure.R
import com.example.mvvmstructure.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupAppBarConfiguration()
        setupNavBottomView()
        setUpDrawerLayoutItemListener()
        setUpNavControllerChangeListener()
    }

    private fun setupAppBarConfiguration() {
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun setupNavBottomView() {
        binding.navBottomView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setUpDrawerLayoutItemListener() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    navController.navigate(R.id.loginFragment)
                }
            }
            binding.drawerLayout.close()
            false
        }
    }

    private fun setUpNavControllerChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.navBottomView.visibility = View.VISIBLE
                }
                R.id.searchFragment -> {
                    binding.navBottomView.visibility = View.VISIBLE
                }
                R.id.bookmarkFragment -> {
                    binding.navBottomView.visibility = View.VISIBLE
                }
                else -> {
                    binding.navBottomView.visibility = View.GONE
                }
            }
        }
    }
}