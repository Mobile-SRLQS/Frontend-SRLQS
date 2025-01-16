package com.dl2lab.srolqs.ui.kuesioner.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentTaskStrategyQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.customview.showCustomInformation
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class TaskStrategyQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentTaskStrategyQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskStrategyQuestionBinding.inflate(layoutInflater)


        viewModel = ViewModelProvider(requireActivity()).get(QuestionnaireViewModel::class.java)
        return binding.root
    }

    private fun isAllQuestionsAnswered(): Boolean {
        return binding.radioGroup9.checkedRadioButtonId != -1 && binding.radioGroup10.checkedRadioButtonId != -1 && binding.radioGroup11.checkedRadioButtonId != -1 && binding.radioGroup12.checkedRadioButtonId != -1
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
            if (isAllQuestionsAnswered()) {
                viewModel.logAnswers()
                parentFragmentManager.commit {
                    addToBackStack(null)
                    replace(
                        R.id.questionnaire_container,
                        TimeManagementQuestionFragment(viewModel),
                        TimeManagementQuestionFragment::class.java.simpleName
                    )
                }
            } else {
                requireContext().showCustomAlertDialog("Peringatan",
                    "Mohon jawab semua pertanyaan sebelum melanjutkan",
                    "OK",
                    "",
                    {},
                    {})

            }
        }

        binding.prevButton.setOnClickListener {
            viewModel.logAnswers()
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.questionnaire_container, EnvironmentStructuringQuestionFragment(
                        viewModel
                    ), EnvironmentStructuringQuestionFragment::class.java.simpleName
                )
            }
        }

        binding.infoIcon.setOnClickListener {
            requireContext().showCustomInformation(
                "Task Strategies",
                "Menerapkan teknik atau strategi tertentu untuk menyelesaikan tugas, seperti membuat ringkasan, mencatat, atau menggunakan peta konsep. Strategi ini membantu memproses informasi secara efektif.",
            )
        }

    }

    private fun setupRadioGroups() {
        binding.radioGroup9.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio9_1 -> 1
                R.id.radio9_2 -> 2
                R.id.radio9_3 -> 3
                R.id.radio9_4 -> 4
                R.id.radio9_5 -> 5
                R.id.radio9_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("9", answer)
        }

        binding.radioGroup10.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio10_1 -> 1
                R.id.radio10_2 -> 2
                R.id.radio10_3 -> 3
                R.id.radio10_4 -> 4
                R.id.radio10_5 -> 5
                R.id.radio10_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("10", answer)
        }

        binding.radioGroup11.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio11_1 -> 1
                R.id.radio11_2 -> 2
                R.id.radio11_3 -> 3
                R.id.radio11_4 -> 4
                R.id.radio11_5 -> 5
                R.id.radio11_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("11", answer)
        }

        binding.radioGroup12.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio12_1 -> 1
                R.id.radio12_2 -> 2
                R.id.radio12_3 -> 3
                R.id.radio12_4 -> 4
                R.id.radio12_5 -> 5
                R.id.radio12_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("12", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["9"]?.let { setRadioButtonChecked(binding.radioGroup9, it) }
            answers["10"]?.let { setRadioButtonChecked(binding.radioGroup10, it) }
            answers["11"]?.let { setRadioButtonChecked(binding.radioGroup11, it) }
            answers["12"]?.let { setRadioButtonChecked(binding.radioGroup12, it) }
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
        fun newInstance(viewModel: QuestionnaireViewModel) = TaskStrategyQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}