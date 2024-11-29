package com.dl2lab.srolqs.validator

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.preference.user.dataStore
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectivityObserver = NetworkConnectivityObserver(applicationContext)
        observeNetworkConnectivity()
        if (!shouldSkipTokenValidation()) {
            validateToken()
        }
    }

    private fun observeNetworkConnectivity() {
        lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                NetworkStateManager.updateNetworkState(status)
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        hideNoInternetSnackbar()
                    }
                    ConnectivityObserver.Status.Unavailable,
                    ConnectivityObserver.Status.Lost -> {
                        showNoInternetSnackbar()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun validateToken() {
        val token = getTokenFromPreferences()
        if (!JWTValidator.isTokenValid(token)) {
            showInvalidTokenSnackbar()
            navigateToWelcomeActivity()
        }
    }

    private fun getTokenFromPreferences(): String? {
        var token: String? = null
        lifecycleScope.launch {
            UserPreference.getInstance(applicationContext.dataStore).getToken().collect {
                token = it
            }
        }
        return token
    }

    private fun shouldSkipTokenValidation(): Boolean {
        val currentActivity = this::class.java
        return currentActivity == LoginActivity::class.java ||
                currentActivity == RegisterActivity::class.java ||
                currentActivity == WelcomeActivity::class.java
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            NetworkStateManager.networkState.collect { status ->
                when (status) {
                    ConnectivityObserver.Status.Unavailable,
                    ConnectivityObserver.Status.Lost -> {
                        showNoInternetSnackbar()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showInvalidTokenSnackbar() {
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(
            rootView,
            "Token tidak valid, silakan login kembali",
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(getColor(R.color.red)) // Adjust color as needed
            setTextColor(getColor(R.color.black))
            setAction("OK") {
                dismiss()
            }
            show()
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showNoInternetSnackbar() {
        // Dismiss existing Snackbar if any
        snackbar?.dismiss()

        val rootView = findViewById<View>(android.R.id.content)
        Log.d("Snackbar", "Showing no internet snackbar")
        snackbar = Snackbar.make(
            rootView,
            "Tidak ada koneksi internet",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setBackgroundTint(getColor(R.color.red))
            setTextColor(getColor(R.color.black))
            show()
        }
    }

    private fun hideNoInternetSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.dismiss()
        snackbar = null
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}