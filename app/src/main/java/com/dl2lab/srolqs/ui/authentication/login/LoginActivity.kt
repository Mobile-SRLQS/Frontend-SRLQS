package com.dl2lab.srolqs.ui.authentication.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.IconAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.Type
import com.akndmr.library.TextAttribute
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityLoginBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.forgotPassword.ForgotPasswordActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var role: String

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
//        this.window.setFlags(
//            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        role = intent.getStringExtra("role") ?: "Student"

        setContentView(binding.root)
        LoadingManager.init(this)
        configureButton()
        observeViewModel()
        binding.registerButton.setOnClickListener { navToRegisterPage() }
        binding.loginButton.setOnClickListener { validateAndSubmitForm() }
        binding.tvForgotPassword.setOnClickListener { navToForgotPassword(this) }
    }


    private fun navToForgotPassword(context: Context) {
        val intent = Intent(context, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun navToMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navToRegisterPage() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    private fun configureButton() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                val email = binding.inputEmailLogin.text.toString()
                val password = binding.inputPasswordLogin.text.toString()
                binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
            }
        }

        binding.inputEmailLogin.addTextChangedListener(textWatcher)
        binding.inputPasswordLogin.addTextChangedListener(textWatcher)

        val email = binding.inputEmailLogin.text.toString()
        val password = binding.inputPasswordLogin.text.toString()
        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }


    private fun validateAndSubmitForm() {
        val email = binding.inputEmailLogin.text.toString()
        val password = binding.inputPasswordLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showCustomAlertDialog("Login Gagal",
                "Tolong masukkan email dan password Anda!",
                "OK",
                "",
                {},
                {})

            return
        }

        try {
            // Trigger the registration process
            loginViewModel.login(
                email = email,
                password = password,
            )


        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

        }
    }

    private fun observeViewModel() {
        loginViewModel.loginUser.observe(this) { response ->
            if (response.error == true) {
                showCustomAlertDialog("Login Gagal", subtitle = response.message, "OK", "", {}, {})
            } else {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this),
                    type = Type.Success,
                    attributes = listOf(
                        TextAttribute.Text(text = "Berhasil masuk sebagai ${response.loginResult.nama}!"),
                        TextAttribute.TextColor(textColor = R.color.white),
                        IconAttribute.Icon(iconRes = R.drawable.ic_delete),
                        IconAttribute.IconColor(iconTint = R.color.white),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Top,
                        AnimationAttribute.FadeInOut
                    )
                ).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    navToMainActivity(this)
                }, 1500)
            }

            loginViewModel.isLoading.observe(this) { isLoading ->
                // Show loading indicator if needed
                if (isLoading) {
                    LoadingManager.show()
                } else {
                    LoadingManager.hide()
                }
            }

            loginViewModel.errorMessageLogin.observe(this) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}