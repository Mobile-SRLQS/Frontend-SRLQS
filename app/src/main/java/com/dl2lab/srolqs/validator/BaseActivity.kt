package com.dl2lab.srolqs.validator

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.preference.user.dataStore
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private var snackbar: Snackbar? = null
    private var noInternetView: View? = null
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
                Log.d("NetworkStatus", "Current status: $status")
                NetworkStateManager.updateNetworkState(status)
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        Log.d("NetworkStatus", "Connection Available")
                        hideNoInternetLayout()
                    }

                    ConnectivityObserver.Status.Unavailable -> {
                        showNoInternetLayout()
                    }
                    ConnectivityObserver.Status.Lost -> {
                        showNoInternetLayout()
                    }
                    ConnectivityObserver.Status.Losing -> {
                        showNoInternetLayout()
                    }
                }
            }
        }
    }

    private fun showNoInternetLayout() {
        try {
            // Pastikan view utama masih ada
            val rootView = findViewById<ViewGroup>(android.R.id.content)
            if (rootView != null) {
                // Hide semua child views dari root
                for (i in 0 until rootView.childCount) {
                    rootView.getChildAt(i).visibility = View.GONE
                }

                // Tampilkan layout no internet
                if (noInternetView == null) {
                    noInternetView = layoutInflater.inflate(R.layout.layout_no_internet, rootView, false)
                    rootView.addView(noInternetView)

                    // Setup retry button
                    noInternetView?.findViewById<MaterialButton>(R.id.btnRetry)?.setOnClickListener {
                        if (isNetworkAvailable()) {
                            hideNoInternetLayout()
                        }
                    }
                }
                noInternetView?.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error showing no internet layout: ${e.message}")
        }
    }


    private fun hideNoInternetLayout() {
        try {
            val rootView = findViewById<ViewGroup>(android.R.id.content)
            if (rootView != null) {
                // Show semua child views dari root kecuali noInternetView
                for (i in 0 until rootView.childCount) {
                    val child = rootView.getChildAt(i)
                    if (child != noInternetView) {
                        child.visibility = View.VISIBLE
                    }
                }
            }
            noInternetView?.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error hiding no internet layout: ${e.message}")
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
        return currentActivity == LoginActivity::class.java || currentActivity == RegisterActivity::class.java || currentActivity == WelcomeActivity::class.java
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            NetworkStateManager.networkState.collect { status ->
                when (status) {
                    ConnectivityObserver.Status.Unavailable, ConnectivityObserver.Status.Lost -> {
                      showNoInternetLayout()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showInvalidTokenSnackbar() {
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(
            rootView, "Token tidak valid, silakan login kembali", Snackbar.LENGTH_LONG
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


    override fun onDestroy() {
        super.onDestroy()
        snackbar?.dismiss()
        snackbar = null
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}