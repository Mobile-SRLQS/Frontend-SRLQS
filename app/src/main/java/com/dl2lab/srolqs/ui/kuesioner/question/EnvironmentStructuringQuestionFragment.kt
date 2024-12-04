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
import com.dl2lab.srolqs.databinding.FragmentEnvironmentStructuringQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.customview.showCustomInformation
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class EnvironmentStructuringQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentEnvironmentStructuringQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnvironmentStructuringQuestionBinding.inflate(layoutInflater)
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

    private fun isAllQuestionsAnswered(): Boolean {
        return binding.radioGroup5.checkedRadioButtonId != -1 &&
                binding.radioGroup6.checkedRadioButtonId != -1 &&
                binding.radioGroup7.checkedRadioButtonId != -1 &&
                binding.radioGroup8.checkedRadioButtonId != -1
    }




    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            if (isAllQuestionsAnswered()) {
                viewModel.logAnswers() // Log answers before navigating
                parentFragmentManager.commit {
                    addToBackStack(null)
                    replace(R.id.questionnaire_container, TaskStrategyQuestionFragment(viewModel), TaskStrategyQuestionFragment::class.java.simpleName)
                }

            } else {
                requireContext().showCustomAlertDialog(
                    "Peringatan",
                    "Mohon jawab semua pertanyaan sebelum melanjutkan",
                    "OK",
                    "",
                    {},
                    {}
                )
            }
        }
        binding.prevButton.setOnClickListener {
            viewModel.logAnswers()
            parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.questionnaire_container, GoalSettingQuestionFragment(viewModel), GoalSettingQuestionFragment::class.java.simpleName)
        } }

        binding.infoIcon.setOnClickListener {
            requireContext().showCustomInformation("Environment Structuring", "Mengatur lingkungan belajar untuk meminimalkan gangguan dan menciptakan kondisi yang mendukung konsentrasi. Ini termasuk pemilihan tempat belajar, penataan alat bantu, dan pengurangan hal-hal yang bisa mengganggu.")

        }

    }

    private fun setupRadioGroups() {
        binding.radioGroup5.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio5_1 -> 1
                R.id.radio5_2 -> 2
                R.id.radio5_3 -> 3
                R.id.radio5_4 -> 4
                R.id.radio5_5 -> 5
                R.id.radio5_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("5", answer)
        }

        binding.radioGroup6.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio6_1 -> 1
                R.id.radio6_2 -> 2
                R.id.radio6_3 -> 3
                R.id.radio6_4 -> 4
                R.id.radio6_5 -> 5
                R.id.radio6_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("6", answer)
        }

        binding.radioGroup7.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio7_1 -> 1
                R.id.radio7_2 -> 2
                R.id.radio7_3 -> 3
                R.id.radio7_4 -> 4
                R.id.radio7_5 -> 5
                R.id.radio7_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("7", answer)
        }

        binding.radioGroup8.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio8_1 -> 1
                R.id.radio8_2 -> 2
                R.id.radio8_3 -> 3
                R.id.radio8_4 -> 4
                R.id.radio8_5 -> 5
                R.id.radio8_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("8", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["5"]?.let { setRadioButtonChecked(binding.radioGroup5, it) }
            answers["6"]?.let { setRadioButtonChecked(binding.radioGroup6, it) }
            answers["7"]?.let { setRadioButtonChecked(binding.radioGroup7, it) }
            answers["8"]?.let { setRadioButtonChecked(binding.radioGroup8, it) }
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
        fun newInstance(viewModel: QuestionnaireViewModel) = EnvironmentStructuringQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}