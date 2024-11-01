package com.dl2lab.srolqs.ui.kuesioner.question

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentSelfEvaluationQuestionBinding
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class SelfEvaluationQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentSelfEvaluationQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfEvaluationQuestionBinding.inflate(layoutInflater)
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
        binding.submitButton.setOnClickListener {
            viewModel.logAnswers() // Log answers before navigating
            val sortedAnswersArray = viewModel.getAnswersArray()
            Log.d("FinalQuestionnaireFragment", "Sorted Answers Array: ${sortedAnswersArray.contentToString()}")
            // Close the current activity and start WelcomeActivity
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // This will close the current activity
        }
        binding.prevButton.setOnClickListener {
            viewModel.logAnswers() // Log answers before navigating
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.questionnaire_container, HelpSeekingQuestionFragment(viewModel), HelpSeekingQuestionFragment::class.java.simpleName)
            } }
    }

    private fun setupRadioGroups() {
        binding.radioGroup21.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio21_1 -> 1
                R.id.radio21_2 -> 2
                R.id.radio21_3 -> 3
                R.id.radio21_4 -> 4
                R.id.radio21_5 -> 5
                R.id.radio21_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("21", answer)
        }

        binding.radioGroup22.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio22_1 -> 1
                R.id.radio22_2 -> 2
                R.id.radio22_3 -> 3
                R.id.radio22_4 -> 4
                R.id.radio22_5 -> 5
                R.id.radio22_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("22", answer)
        }

        binding.radioGroup23.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio23_1 -> 1
                R.id.radio23_2 -> 2
                R.id.radio23_3 -> 3
                R.id.radio23_4 -> 4
                R.id.radio23_5 -> 5
                R.id.radio23_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("23", answer)
        }

        binding.radioGroup24.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio24_1 -> 1
                R.id.radio24_2 -> 2
                R.id.radio24_3 -> 3
                R.id.radio24_4 -> 4
                R.id.radio24_5 -> 5
                R.id.radio24_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("24", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["21"]?.let { setRadioButtonChecked(binding.radioGroup21, it) }
            answers["22"]?.let { setRadioButtonChecked(binding.radioGroup22, it) }
            answers["23"]?.let { setRadioButtonChecked(binding.radioGroup23, it) }
            answers["24"]?.let { setRadioButtonChecked(binding.radioGroup24, it) }
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
        fun newInstance(viewModel: QuestionnaireViewModel) = SelfEvaluationQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}