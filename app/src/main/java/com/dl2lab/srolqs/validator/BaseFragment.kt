package com.dl2lab.srolqs.validator

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dl2lab.srolqs.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.TextAttribute
import com.akndmr.library.Type
import com.dl2lab.srolqs.data.preference.user.UserPreference
import com.dl2lab.srolqs.data.preference.user.dataStore
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity

abstract class BaseFragment : Fragment() {
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectivityObserver = NetworkConnectivityObserver(requireContext())
        observeNetworkConnectivity()
        validateToken()
    }

    private fun observeNetworkConnectivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
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
        viewLifecycleOwner.lifecycleScope.launch {
            UserPreference.getInstance(requireContext().dataStore).getToken().collect {
                token = it
            }
        }
        return token
    }

    private fun showInvalidTokenSnackbar() {
        view?.let { view ->
            AirySnackbar.make(
                source = AirySnackbarSource.ViewSource(view = view),
                type = Type.Error,
                attributes = listOf(
                    TextAttribute.Text(text = "Token tidak valid, silakan login kembali"),
                    TextAttribute.TextColor(textColor = R.color.black),
                    SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                    SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                    RadiusAttribute.Radius(radius = 8f),
                    GravityAttribute.Top,
                    AnimationAttribute.FadeInOut
                )
            ).show()
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showNoInternetSnackbar() {
        view?.let { view ->
            AirySnackbar.make(
                source = AirySnackbarSource.ViewSource(view = view),
                type = Type.Error,
                attributes = listOf(
                    TextAttribute.Text(text = "Tidak ada koneksi internet"),
                    TextAttribute.TextColor(textColor = R.color.black),
                    SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                    SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                    RadiusAttribute.Radius(radius = 8f),
                    GravityAttribute.Top,
                    AnimationAttribute.FadeInOut
                )
            ).show()
        }
    }

    private fun hideNoInternetSnackbar() {
        snackbar?.dismiss()
    }
}