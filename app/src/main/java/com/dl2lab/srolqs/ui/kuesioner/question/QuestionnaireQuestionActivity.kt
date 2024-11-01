package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityQuestionnaireQuestionBinding
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class QuestionnaireQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionnaireQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireQuestionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(QuestionnaireViewModel::class.java)
        if (savedInstanceState == null) {
            fragmentSetup()
        }

        setContentView(binding.root)

    }

    private fun fragmentSetup() {
        supportFragmentManager.beginTransaction().add(R.id.questionnaire_container, GoalSettingQuestionFragment.newInstance(viewModel)).commit()
    }

}