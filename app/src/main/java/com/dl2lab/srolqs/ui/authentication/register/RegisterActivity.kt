package com.dl2lab.srolqs.ui.authentication.register

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityLoginBinding
import com.dl2lab.srolqs.databinding.ActivityRegisterBinding
import com.dl2lab.srolqs.ui.authentication.viewmodel.RegisterViewModel
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var role: String

    private val registerViewModel: RegisterViewModel by viewModels {
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

        setContentView(R.layout.activity_register)

        role = intent.getStringExtra("role") ?: "Student"

        LoadingManager.init(this)

        val personalInfoFragment = RegisterPersonalFragment().apply {
            arguments = Bundle().apply {
                putString("role", role)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, personalInfoFragment)
            .commit()
    }

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
        finish()
    }
}
