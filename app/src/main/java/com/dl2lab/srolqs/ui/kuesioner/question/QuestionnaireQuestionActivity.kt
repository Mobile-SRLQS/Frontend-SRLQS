package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityQuestionnaireQuestionBinding

class QuestionnaireQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionnaireQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireQuestionBinding.inflate(layoutInflater)


        if (savedInstanceState == null) {
            fragmentSetup()
        }

        setContentView(binding.root)

    }

    private fun fragmentSetup() {
        supportFragmentManager.beginTransaction().add(R.id.questionnaire_container, GoalSettingQuestionFragment()).commit()
    }
}