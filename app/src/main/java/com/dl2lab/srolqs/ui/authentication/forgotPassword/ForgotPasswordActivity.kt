package com.dl2lab.srolqs.ui.authentication.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.ActivityCreateNewPasswordBinding
import com.dl2lab.srolqs.databinding.ActivityForgotPasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.createNewPassword.CreateNewPasswordActivity
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import org.json.JSONObject
import retrofit2.Response


class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var role: String

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
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        role = intent.getStringExtra("email") ?: ""

        setContentView(binding.root)
        binding.buttonSubmit.setOnClickListener { changePassword() }
        observeViewModel()
        setupTextWatcher()
        updateButtonState()
        LoadingManager.init(this)

    }


    private fun navToCreateNewPassword(email: String) {
        val intent = Intent(this, CreateNewPasswordActivity::class.java)
        intent.putExtra("email", email )
        startActivity(intent)
    }

    private fun changePassword(){
        val email = binding.inputText.text.toString()



        loginViewModel.requestResetCode(email).observe(this) { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    this.showCustomAlertDialog(
                        "Verifikasi Email Sukses",
                        "Kode verifikasi telah dikirim ke email Anda. Silakan cek email Anda untuk melanjutkan proses reset password.",
                        "OK",
                        "",
                        {
                            navToCreateNewPassword(email)
                        },
                        {
                        },
                        error = false
                    )

                }
            } else {
                this.showCustomAlertDialog(
                    "Verifikasi Email Gagal",
                    extractErrorMessage(response),
                    "OK",
                    "",
                    {},
                    {
                    },
                )
            }
        }



    }

    private fun setupTextWatcher() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                updateButtonState()
            }
        }

        binding.inputText.addTextChangedListener(textWatcher)
    }

    private fun updateButtonState() {
        val isEmailFilled = binding.inputText.text.toString().isNotEmpty()
        binding.buttonSubmit.isEnabled = isEmailFilled
        binding.buttonSubmit.alpha = if (isEmailFilled) 1.0f else 0.5f
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
                LoadingManager.show()
            } else {
                LoadingManager.hide()
            }
        }

        loginViewModel.errorMessageLogin.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                this.showCustomAlertDialog(
                    "Verifikasi Email Gagal",
                    errorMessage ?: "Terjadi kesalahan saat mengirim kode verifikasi",
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