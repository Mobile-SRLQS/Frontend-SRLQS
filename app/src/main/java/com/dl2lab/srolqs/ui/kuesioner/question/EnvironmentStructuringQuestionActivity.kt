package com.dl2lab.srolqs.ui.kuesioner.question

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.ActivityEnvironmentStructuringQuestionBinding
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class EnvironmentStructuringQuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnvironmentStructuringQuestionBinding
    private val viewModel: QuestionnaireViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnvironmentStructuringQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pre-select radio buttons if answers are already set
        setSelectedAnswer(binding.radioGroup5, viewModel.getAnswer(5))
        setSelectedAnswer(binding.radioGroup6, viewModel.getAnswer(6))
        setSelectedAnswer(binding.radioGroup7, viewModel.getAnswer(7))
        setSelectedAnswer(binding.radioGroup8, viewModel.getAnswer(8))

        // Add listeners to save answer immediately when a radio button is selected
        binding.radioGroup5.setOnCheckedChangeListener { group, _ ->
            viewModel.setAnswer(5, getAnswerFromRadioGroup(group))
        }
        binding.radioGroup6.setOnCheckedChangeListener { group, _ ->
            viewModel.setAnswer(6, getAnswerFromRadioGroup(group))
        }
        binding.radioGroup7.setOnCheckedChangeListener { group, _ ->
            viewModel.setAnswer(7, getAnswerFromRadioGroup(group))
        }
        binding.radioGroup8.setOnCheckedChangeListener { group, _ ->
            viewModel.setAnswer(8, getAnswerFromRadioGroup(group))
        }

        // Set up navigation buttons
        binding.nextButton.setOnClickListener {
            viewModel.getAnswers()  // This will trigger the logging in getAnswers()
            val intent = Intent(this, TaskStrategyQuestionActivity::class.java)
            startActivity(intent)
        }

        binding.prevButton.setOnClickListener {
            viewModel.getAnswers()  // This will trigger the logging in getAnswers()
            val intent = Intent(this, GoalSettingQuestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelectedAnswer(radioGroup: RadioGroup, answer: Int?) {
        when (answer) {
            1 -> radioGroup.check(R.id.radio5_1)
            2 -> radioGroup.check(R.id.radio5_2)
            3 -> radioGroup.check(R.id.radio5_3)
            4 -> radioGroup.check(R.id.radio5_4)
            5 -> radioGroup.check(R.id.radio5_5)
            6 -> radioGroup.check(R.id.radio5_6)
        }
        Log.d("EnvironmentStructuringActivity", "Pre-selected answer: $answer")
    }

    private fun getAnswerFromRadioGroup(radioGroup: RadioGroup): Int {
        val answer = when (radioGroup.id) {
            R.id.radioGroup5 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio5_1 -> 1
                R.id.radio5_2 -> 2
                R.id.radio5_3 -> 3
                R.id.radio5_4 -> 4
                R.id.radio5_5 -> 5
                R.id.radio5_6 -> 6
                else -> 0
            }
            R.id.radioGroup6 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio6_1 -> 1
                R.id.radio6_2 -> 2
                R.id.radio6_3 -> 3
                R.id.radio6_4 -> 4
                R.id.radio6_5 -> 5
                R.id.radio6_6 -> 6
                else -> 0
            }
            R.id.radioGroup7 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio7_1 -> 1
                R.id.radio7_2 -> 2
                R.id.radio7_3 -> 3
                R.id.radio7_4 -> 4
                R.id.radio7_5 -> 5
                R.id.radio7_6 -> 6
                else -> 0
            }
            R.id.radioGroup8 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio8_1 -> 1
                R.id.radio8_2 -> 2
                R.id.radio8_3 -> 3
                R.id.radio8_4 -> 4
                R.id.radio8_5 -> 5
                R.id.radio8_6 -> 6
                else -> 0
            }
            else -> 0
        }
        Log.d("EnvironmentStructuringQuestionActivity", "Retrieved answer from RadioGroup ${radioGroup.id}: $answer")
        return answer
    }
}
