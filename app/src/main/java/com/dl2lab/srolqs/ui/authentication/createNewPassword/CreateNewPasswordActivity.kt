package com.dl2lab.srolqs.ui.authentication.createNewPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.TextAttribute
import com.akndmr.library.Type
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.ActivityCreateNewPasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import org.json.JSONObject
import retrofit2.Response




class CreateNewPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewPasswordBinding
    private lateinit var email: String

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        LoadingManager.init(this)
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        email = intent.getStringExtra("email") ?: ""
        setupTextWatchers()
        updateButtonState()
        setContentView(binding.root)
        setupPasswordInputListener()
        binding.btnSaveChanges.setOnClickListener { changePassword() }
        observeViewModel()

    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupPasswordInputListener() {
        val passwordLayout = binding.inputNewPasswordLayout
        val passwordInput = binding.inputNewPassword
        val passwordRecommendation = binding.tvPasswordReccomendation

        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showPasswordRecommendation(passwordRecommendation)
            } else {
                hidePasswordRecommendation(passwordRecommendation)
            }
        }

        passwordInput.setOnClickListener {
            showPasswordRecommendation(passwordRecommendation)
        }
    }

    private fun showPasswordRecommendation(view: View) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            translationY = -20f // Mulai sedikit ke atas
            animate().alpha(1f).translationY(0f).setDuration(300)
                .setInterpolator(DecelerateInterpolator()).start()
        }
    }

    private fun hidePasswordRecommendation(view: View) {
        view.animate().alpha(0f).translationY(-20f).setDuration(200)
            .setInterpolator(AccelerateInterpolator()).withEndAction {
                view.visibility = View.GONE
            }.start()
    }


    private fun navToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun setupTextWatchers() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                updateButtonState()
            }
        }

        binding.inputNewPassword.addTextChangedListener(textWatcher)
        binding.inputConfirmNewPassword.addTextChangedListener(textWatcher)
        binding.inputVerificationCode.addTextChangedListener(textWatcher)
    }

    private fun updateButtonState() {
        val isFormFilled = binding.inputNewPassword.text.toString().isNotEmpty() &&
                binding.inputConfirmNewPassword.text.toString().isNotEmpty() &&
                binding.inputVerificationCode.text.toString().isNotEmpty()

        binding.btnSaveChanges.isEnabled = isFormFilled
    }

    private fun changePassword(){
        val newPassword = binding.inputNewPassword.text.toString()
        val confirmNewPassword = binding.inputConfirmNewPassword.text.toString()
        val verificationCode = binding.inputVerificationCode.text.toString()

        if(newPassword.isEmpty() || confirmNewPassword.isEmpty() || verificationCode.isEmpty() ){
            this.showCustomAlertDialog(
                "",
                "Please input all fields",
                "OK",
                "Cancel",
                {},
                {
                },
            )
            return
        }

        if (newPassword != confirmNewPassword) {
            AirySnackbar.make(
                source = AirySnackbarSource.ViewSource(binding.root),
                type = Type.Error,
                attributes = listOf(
                    TextAttribute.Text(text = "Kata sandi yang Anda masukkan tidak cocok!"),
                    TextAttribute.TextColor(textColor = R.color.white),
                    SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                    SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                    RadiusAttribute.Radius(radius = 8f),
                    GravityAttribute.Bottom,
                    AnimationAttribute.FadeInOut
                )
            ).show()
            return
        }


        showLoading(true)
        loginViewModel.createNewPassword(email, confirmNewPassword, verificationCode).observe(this) { response ->
            if (response.isSuccessful) {
                showLoading(false)
                val body = response.body()
                if (body != null) {
                    this.showCustomAlertDialog(
                        "Kata Sandi Berhasil Diubah",
                        "Kata sandi Anda berhasil diubah. Silakan login kembali.",
                        "OK",
                        "",
                        {
                            navToWelcomeActivity()
                        },
                        {
                        },
                        error = false
                    )

                }
            } else {
                showLoading(false)

            }
        }



    }

    private fun extractErrorMessage(response: Response<BasicResponse>): String {
        return try {
            val json = response.errorBody()?.string()
            val jsonObject = JSONObject(json)
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Unknown Error"
        }
    }



    private fun observeViewModel() {


        loginViewModel.isLoading.observe(this) { isLoading ->
            // Show loading indicator if needed
            if (isLoading) {
                binding.loadingView.visibility = View.VISIBLE
                binding.svChangePassword.visibility = View.GONE
            } else {
                binding.loadingView.visibility = View.GONE
                binding.svChangePassword.visibility = View.VISIBLE
                // Hide loading spinner
            }
        }

        loginViewModel.errorMessageLogin.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                this.showCustomAlertDialog(
                    "Kata Sandi Gagal Diubah",
                    errorMessage ?: "Terjadi kesalahan saat mengubah kata sandi",
                    "OK",
                    "",
                    {},
                    {
                    },
                )
            }
        }

    }
}