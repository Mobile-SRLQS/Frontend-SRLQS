package com.dl2lab.srolqs.ui.authentication.createNewPassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.ActivityCreateNewPasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
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
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        email = intent.getStringExtra("email") ?: ""

        setContentView(binding.root)
        binding.btnSaveChanges.setOnClickListener { changePassword() }
        observeViewModel()

    }


    private fun navToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun changePassword(){
        val newPassword = binding.inputNewPassword.text.toString()
        val confirmNewPassword = binding.inputConfirmNewPassword.text.toString()
        val verificationCode = binding.inputVerificationCode.text.toString()

        if(newPassword.isEmpty() || confirmNewPassword.isEmpty() || verificationCode.isEmpty() ){
            this.showCustomAlertDialog(
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
            this.showCustomAlertDialog(
                "Password and Confirm Password must be the same",
                "OK",
                "",
                {},
                {
                },
            )
            return
        }



        loginViewModel.createNewPassword(email, confirmNewPassword, verificationCode).observe(this) { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    this.showCustomAlertDialog(
                        body.message ?: "Password changed successfully",
                        "OK",
                        "",
                        {
                            navToWelcomeActivity()
                        },
                        {
                        },
                    )

                }
            } else {
                this.showCustomAlertDialog(
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
                // Show a loading spinner or some UI feedback for loading
            } else {
                // Hide loading spinner
            }
        }

        loginViewModel.errorMessageLogin.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }
}