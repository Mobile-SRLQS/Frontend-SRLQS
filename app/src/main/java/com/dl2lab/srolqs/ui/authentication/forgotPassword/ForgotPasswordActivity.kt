package com.dl2lab.srolqs.ui.authentication.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.ActivityForgotPasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.createNewPassword.CreateNewPasswordActivity
import com.dl2lab.srolqs.ui.authentication.viewmodel.LoginViewModel
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
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        role = intent.getStringExtra("email") ?: ""

        setContentView(binding.root)
        binding.buttonSubmit.setOnClickListener { changePassword() }
        observeViewModel()

    }


    private fun navToCreateNewPassword(email: String) {
        val intent = Intent(this, CreateNewPasswordActivity::class.java)
        intent.putExtra("email", email )
        startActivity(intent)
    }

    private fun changePassword(){
        val email = binding.inputText.text.toString()

        if(email.isEmpty() ){
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



        loginViewModel.requestResetCode(email).observe(this) { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    this.showCustomAlertDialog(
                        body.message ?: "Code sent successfully",
                        "OK",
                        "",
                        {
                            navToCreateNewPassword(email)
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