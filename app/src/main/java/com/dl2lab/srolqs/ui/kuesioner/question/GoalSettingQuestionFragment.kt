package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentGoalSettingQuestionBinding
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class GoalSettingQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentGoalSettingQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalSettingQuestionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(QuestionnaireViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupRadioGroups()
        restoreAnswers()
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            viewModel.logAnswers() // Log answers before navigating
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.questionnaire_container, EnvironmentStructuringQuestionFragment(
                    viewModel
                ), EnvironmentStructuringQuestionFragment::class.java.simpleName)
            }
        }
    }

    private fun setupRadioGroups() {
        binding.radioGroup1.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio1_1 -> 1
                R.id.radio1_2 -> 2
                R.id.radio1_3 -> 3
                R.id.radio1_4 -> 4
                R.id.radio1_5 -> 5
                R.id.radio1_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("1", answer)
        }

        binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio2_1 -> 1
                R.id.radio2_2 -> 2
                R.id.radio2_3 -> 3
                R.id.radio2_4 -> 4
                R.id.radio2_5 -> 5
                R.id.radio2_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("2", answer)
        }

        binding.radioGroup3.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio3_1 -> 1
                R.id.radio3_2 -> 2
                R.id.radio3_3 -> 3
                R.id.radio3_4 -> 4
                R.id.radio3_5 -> 5
                R.id.radio3_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("3", answer)
        }

        binding.radioGroup4.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio4_1 -> 1
                R.id.radio4_2 -> 2
                R.id.radio4_3 -> 3
                R.id.radio4_4 -> 4
                R.id.radio4_5 -> 5
                R.id.radio4_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("4", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["1"]?.let { setRadioButtonChecked(binding.radioGroup1, it) }
            answers["2"]?.let { setRadioButtonChecked(binding.radioGroup2, it) }
            answers["3"]?.let { setRadioButtonChecked(binding.radioGroup3, it) }
            answers["4"]?.let { setRadioButtonChecked(binding.radioGroup4, it) }
        }
    }

    private fun setRadioButtonChecked(radioGroup: RadioGroup, answer: Int) {
        val radioButtonId = when (answer) {
            1 -> radioGroup.getChildAt(0).id
            2 -> radioGroup.getChildAt(1).id
            3 -> radioGroup.getChildAt(2).id
            4 -> radioGroup.getChildAt(3).id
            5 -> radioGroup.getChildAt(4).id
            6 -> radioGroup.getChildAt(5).id
            else -> View.NO_ID
        }
        if (radioButtonId != View.NO_ID) {
            radioGroup.check(radioButtonId)
        }
    }

    companion object {
        fun newInstance(viewModel: QuestionnaireViewModel) = GoalSettingQuestionFragment(viewModel).apply {
            this.viewModel = viewModel
        }
    }
}