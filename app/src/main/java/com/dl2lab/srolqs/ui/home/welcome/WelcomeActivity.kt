package com.dl2lab.srolqs.ui.home.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityMainBinding
import com.dl2lab.srolqs.databinding.ActivityWelcomeBinding
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        binding.buttonStudent.setOnClickListener { navToLoginPage("Student") }
        binding.buttonTeacher.setOnClickListener { navToLoginPage("Instructor") }
        setContentView(binding.root)
    }
    private fun navToLoginPage(role: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    private fun navToRegisterPage(role: String) {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
    }
}