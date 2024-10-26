package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.R
import android.content.Intent
import android.util.Log
import android.widget.RadioGroup
import androidx.activity.viewModels
import com.dl2lab.srolqs.databinding.ActivityGoalSettingQuestionBinding
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class GoalSettingQuestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalSettingQuestionBinding
    private val viewModel: QuestionnaireViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalSettingQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pre-select radio buttons if answers are already set
        setSelectedAnswer(binding.radioGroup1, viewModel.getAnswer(1))
        setSelectedAnswer(binding.radioGroup2, viewModel.getAnswer(2))
        setSelectedAnswer(binding.radioGroup3, viewModel.getAnswer(3))
        setSelectedAnswer(binding.radioGroup4, viewModel.getAnswer(4))

        // Set up "Next" button click to save answers and go to the next activity
        binding.nextButton.setOnClickListener {
            viewModel.setAnswer(1, getAnswerFromRadioGroup(binding.radioGroup1))
            viewModel.setAnswer(2, getAnswerFromRadioGroup(binding.radioGroup2))
            viewModel.setAnswer(3, getAnswerFromRadioGroup(binding.radioGroup3))
            viewModel.setAnswer(4, getAnswerFromRadioGroup(binding.radioGroup4))
            // Log all answers at this point
            viewModel.getAnswers()  // This will trigger the logging in getAnswers()


            // Navigate to the next dimension activity
            val intent = Intent(this, EnvironmentStructuringQuestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelectedAnswer(radioGroup: RadioGroup, answer: Int?) {
        when (answer) {
            1 -> radioGroup.check(R.id.radio1_1)
            2 -> radioGroup.check(R.id.radio1_2)
            3 -> radioGroup.check(R.id.radio1_3)
            4 -> radioGroup.check(R.id.radio1_4)
            5 -> radioGroup.check(R.id.radio1_5)
            6 -> radioGroup.check(R.id.radio1_6)
        }
        Log.d("GoalSettingActivity", "Pre-selected answer for question with answer $answer")
    }

    private fun getAnswerFromRadioGroup(radioGroup: RadioGroup): Int {
        val answer = when (radioGroup.id) {
            R.id.radioGroup1 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio1_1 -> 1
                R.id.radio1_2 -> 2
                R.id.radio1_3 -> 3
                R.id.radio1_4 -> 4
                R.id.radio1_5 -> 5
                R.id.radio1_6 -> 6
                else -> 0
            }
            R.id.radioGroup2 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio2_1 -> 1
                R.id.radio2_2 -> 2
                R.id.radio2_3 -> 3
                R.id.radio2_4 -> 4
                R.id.radio2_5 -> 5
                R.id.radio2_6 -> 6
                else -> 0
            }
            R.id.radioGroup3 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio3_1 -> 1
                R.id.radio3_2 -> 2
                R.id.radio3_3 -> 3
                R.id.radio3_4 -> 4
                R.id.radio3_5 -> 5
                R.id.radio3_6 -> 6
                else -> 0
            }
            R.id.radioGroup4 -> when (radioGroup.checkedRadioButtonId) {
                R.id.radio4_1 -> 1
                R.id.radio4_2 -> 2
                R.id.radio4_3 -> 3
                R.id.radio4_4 -> 4
                R.id.radio4_5 -> 5
                R.id.radio4_6 -> 6
                else -> 0
            }
            else -> 0
        }
        Log.d("GoalSettingQuestionActivity", "Retrieved answer from RadioGroup ${radioGroup.id}: $answer")
        return answer
    }
}