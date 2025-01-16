package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityQuestionnaireQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class QuestionnaireQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionnaireQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireQuestionBinding.inflate(layoutInflater)
        setupViewModel()

        if (savedInstanceState == null) {
            fragmentSetup()
        }
        val classId = intent.getStringExtra("classId")
        classId?.let { viewModel.setClassId(it) }
        val period = intent.getStringExtra("period")
        period?.let { viewModel.setPeriod(it) }
        Toast.makeText(this, "classId: $classId, period: $period", Toast.LENGTH_SHORT).show()

        setContentView(binding.root)

    }

    private fun fragmentSetup() {
        supportFragmentManager.beginTransaction()
            .add(R.id.questionnaire_container, GoalSettingQuestionFragment.newInstance(viewModel))
            .commit()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(QuestionnaireViewModel::class.java)
    }

}