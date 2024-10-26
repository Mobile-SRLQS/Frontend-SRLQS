package com.dl2lab.srolqs.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityMainBinding
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.authentication.register.RegisterActivity
import com.dl2lab.srolqs.ui.kuesioner.question.GoalSettingQuestionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.buttonStudent.setOnClickListener{ navToLoginPage() }
        binding.buttonTeacher.setOnClickListener{ navToLoginPage() }
        binding.buttonKuesioner.setOnClickListener{navToKuesionerPage()}
        setContentView(binding.root)
    }
    private fun navToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun navToKuesionerPage() {
        val intent = Intent(this, GoalSettingQuestionActivity::class.java)
        startActivity(intent)
    }
}