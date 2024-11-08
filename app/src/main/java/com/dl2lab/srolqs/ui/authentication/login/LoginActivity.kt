package com.dl2lab.srolqs.ui.authentication.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        binding = ActivityLoginBinding.inflate(layoutInflater)
        role = intent.getStringExtra("role") ?: "Student"

        setContentView(binding.root)
        LoadingManager.init(this)
        observeViewModel()
        binding.registerButton.setOnClickListener { navToRegisterPage() }
        binding.loginButton.setOnClickListener { validateAndSubmitForm() }
        binding.tvForgotPassword.setOnClickListener { navToForgotPassword(this) }
    }




    private fun navToForgotPassword(context: Context){
        val intent = Intent(context, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun navToMainActivity(context: Context){
        val intent = Intent(context, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navToRegisterPage() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    private fun validateAndSubmitForm() {
        val email = binding.inputEmailLogin.text.toString()
        val password = binding.inputPasswordLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showCustomAlertDialog(
                "Login Gagal",
                "Tolong masukkan email dan password Anda!",
                "OK",
                "",
                {},
                {}
            )

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
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                navToMainActivity(this)
            }
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