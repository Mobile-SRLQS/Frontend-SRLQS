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
import com.dl2lab.srolqs.databinding.FragmentTimeManagementQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class TimeManagementQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentTimeManagementQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeManagementQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()
        setupRadioGroups()
        restoreAnswers()
    }

    fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(QuestionnaireViewModel::class.java)
    }



    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            viewModel.logAnswers()
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.questionnaire_container, HelpSeekingQuestionFragment(viewModel), HelpSeekingQuestionFragment::class.java.simpleName)
            }
        }
        binding.prevButton.setOnClickListener {
            viewModel.logAnswers()
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.questionnaire_container, TaskStrategyQuestionFragment(viewModel), TaskStrategyQuestionFragment::class.java.simpleName)
            } }
    }

    private fun setupRadioGroups() {
        binding.radioGroup13.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio13_1 -> 1
                R.id.radio13_2 -> 2
                R.id.radio13_3 -> 3
                R.id.radio13_4 -> 4
                R.id.radio13_5 -> 5
                R.id.radio13_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("13", answer)
        }

        binding.radioGroup14.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio14_1 -> 1
                R.id.radio14_2 -> 2
                R.id.radio14_3 -> 3
                R.id.radio14_4 -> 4
                R.id.radio14_5 -> 5
                R.id.radio14_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("14", answer)
        }

        binding.radioGroup15.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio15_1 -> 1
                R.id.radio15_2 -> 2
                R.id.radio15_3 -> 3
                R.id.radio15_4 -> 4
                R.id.radio15_5 -> 5
                R.id.radio15_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("15", answer)
        }

        binding.radioGroup16.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio16_1 -> 1
                R.id.radio16_2 -> 2
                R.id.radio16_3 -> 3
                R.id.radio16_4 -> 4
                R.id.radio16_5 -> 5
                R.id.radio16_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("16", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["13"]?.let { setRadioButtonChecked(binding.radioGroup13, it) }
            answers["14"]?.let { setRadioButtonChecked(binding.radioGroup14, it) }
            answers["15"]?.let { setRadioButtonChecked(binding.radioGroup15, it) }
            answers["16"]?.let { setRadioButtonChecked(binding.radioGroup16, it) }
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
        fun newInstance(viewModel: QuestionnaireViewModel) =TimeManagementQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}