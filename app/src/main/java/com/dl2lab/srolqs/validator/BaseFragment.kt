package com.dl2lab.srolqs.validator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dl2lab.srolqs.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.preference.user.dataStore
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import android.util.Log
import android.view.View
import kotlinx.coroutines.flow.firstOrNull

abstract class BaseFragment : Fragment() {
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectivityObserver = NetworkConnectivityObserver(requireContext())

        if (!shouldSkipTokenValidation()) {
            validateToken()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                Log.d("NetworkStatus", "Current status: $status")
                NetworkStateManager.updateNetworkState(status)
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        Log.d("NetworkStatus", "Connection Available")
                        hideNoInternetSnackbar()
                    }
                    ConnectivityObserver.Status.Unavailable,
                    ConnectivityObserver.Status.Lost,
                    ConnectivityObserver.Status.Losing -> {
                        Log.d("NetworkStatus", "Connection Lost/Unavailable")
                        showNoInternetSnackbar()
                    }
                }
            }
        }
    }

    private fun validateToken() {
        viewLifecycleOwner.lifecycleScope.launch {
            val token = getTokenFromPreferences()
            if (!JWTValidator.isTokenValid(token)) {
                showInvalidTokenSnackbar()
                navigateToWelcomeActivity()
            }
        }
    }

    private suspend fun getTokenFromPreferences(): String? {
        return UserPreference.getInstance(requireContext().dataStore).getToken().firstOrNull()
    }

    private fun shouldSkipTokenValidation(): Boolean {
        val currentActivity = requireActivity()::class.java
        return currentActivity == LoginActivity::class.java ||
                currentActivity == RegisterActivity::class.java ||
                currentActivity == WelcomeActivity::class.java
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
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
        view?.let { rootView ->
            Snackbar.make(
                rootView,
                "Token tidak valid, silakan login kembali",
                Snackbar.LENGTH_LONG
            ).apply {
                setBackgroundTint(requireContext().getColor(R.color.red))
                setTextColor(requireContext().getColor(R.color.black))
                setAction("OK") {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showNoInternetSnackbar() {
        snackbar?.dismiss()

        view?.let { rootView ->
            Log.d("Snackbar", "Showing no internet snackbar")
            snackbar = Snackbar.make(
                rootView,
                "Tidak ada koneksi internet",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setBackgroundTint(requireContext().getColor(R.color.red))
                setTextColor(requireContext().getColor(R.color.black))
                show()
            }
        }
    }

    private fun hideNoInternetSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        snackbar = null
    }
}
