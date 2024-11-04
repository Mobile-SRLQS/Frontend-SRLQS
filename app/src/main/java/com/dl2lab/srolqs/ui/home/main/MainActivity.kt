package com.dl2lab.srolqs.ui.home.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityMainBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.utils.JwtUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        checkUserSession()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_scan_qr, R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)
        val fragmentToOpen = intent.getStringExtra("fragmentToOpen")
        if (fragmentToOpen == "scanQR") {
            navController.navigate(R.id.navigation_scan_qr)
            navView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (navController.currentDestination?.id == R.id.navigation_scan_qr) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(R.id.navigation_home)
                        }
                        true
                    }

                    else -> {
                        NavigationUI.onNavDestinationSelected(item, navController)
                    }
                }
            }
        } else if (fragmentToOpen == "profile") {
            navController.navigate(R.id.navigation_profile)
            navView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        if (navController.currentDestination?.id == R.id.navigation_profile) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(R.id.navigation_home)
                        }
                        true
                    }

                    else -> {
                        NavigationUI.onNavDestinationSelected(item, navController)
                    }
                }
            }
        }

        setupViewModel()
        checkUserSession()
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        ).get(MainViewModel::class.java)
    }

    private fun checkUserSession() {
        viewModel.getSession().observe(this, Observer  { userModel ->
            if(userModel.token != null) {
                if (JwtUtils.isTokenExpired(userModel.token)) {
                    viewModel.logout()
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    this.finish()
                    this.showCustomAlertDialog(
                        "Session Expired. Please login again!",
                        "Login",
                        "",
                        {},
                        {},
                    )
                }
            } else{
                startActivity(Intent(this, WelcomeActivity::class.java))
                this.finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        checkUserSession()
    }

    companion object{
        private const val TAG = "MainActivity"
    }

}