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
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel


class GoalSettingQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentGoalSettingQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalSettingQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        showInformation()
        setupAction()
        setupRadioGroups()
        restoreAnswers()
    }

    fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(QuestionnaireViewModel::class.java)
    }

    private fun showInformation(){
        requireContext().showCustomAlertDialog("Informasi Pengisian Kuesioner",
            "Kuesioner SRL ini terdiri dari 6 bagian yang dikelompokkan berdasarkan 6 dimensi SRL. Masing-masing bagian terdiri empat soal dengan  jawaban dalam bentuk skala 1 sampai 6 yang menunjukkan pilihan sangat tidak setuju dan sangat setuju.",
            "Mulai",
            "",
            {},
            {})
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            if (isAllQuestionsAnswered()) {
                viewModel.logAnswers() // Log answers before navigating
                parentFragmentManager.commit {
                    addToBackStack(null)
                    replace(R.id.questionnaire_container, EnvironmentStructuringQuestionFragment(
                        viewModel
                    ), EnvironmentStructuringQuestionFragment::class.java.simpleName)
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
    }


    private fun isAllQuestionsAnswered(): Boolean {
        return binding.radioGroup1.checkedRadioButtonId != -1 &&
                binding.radioGroup2.checkedRadioButtonId != -1 &&
                binding.radioGroup3.checkedRadioButtonId != -1 &&
                binding.radioGroup4.checkedRadioButtonId != -1
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