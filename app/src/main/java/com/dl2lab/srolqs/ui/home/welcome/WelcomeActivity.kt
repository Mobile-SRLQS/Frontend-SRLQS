package com.dl2lab.srolqs.ui.home.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.databinding.ActivityWelcomeBinding
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.home.dosen.WebViewActivity
import com.dl2lab.srolqs.ui.home.main.MainActivity
import com.dl2lab.srolqs.validator.BaseActivity

class WelcomeActivity : BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonStudent.setOnClickListener { navToLoginPage("Student") }
        binding.buttonTeacher.setOnClickListener { navToLoginPage("Instructor") }
    }

    private fun navToLoginPage(role: String) {
        if (role == "Instructor") {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("role", role)
            startActivity(intent)
        }
    }
}