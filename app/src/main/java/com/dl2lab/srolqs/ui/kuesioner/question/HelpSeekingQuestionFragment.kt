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
import com.dl2lab.srolqs.databinding.FragmentHelpSeekingQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.customview.showCustomInformation

class HelpSeekingQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentHelpSeekingQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpSeekingQuestionBinding.inflate(layoutInflater)
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
            if (isAllAnswered()) {
                viewModel.logAnswers() // Log answers before navigating
                parentFragmentManager.commit {
                    addToBackStack(null)
                    replace(
                        R.id.questionnaire_container,
                        SelfEvaluationQuestionFragment(viewModel),
                        SelfEvaluationQuestionFragment::class.java.simpleName
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
            viewModel.logAnswers() // Log answers before navigating
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.questionnaire_container,
                    TimeManagementQuestionFragment(viewModel),
                    TimeManagementQuestionFragment::class.java.simpleName
                )
            }
        }
        binding.infoIcon.setOnClickListener {
            requireContext().showCustomInformation(
                "Help Seeking",
                "Mencari bantuan ketika menghadapi kesulitan, baik dari teman, guru, atau sumber belajar lainnya. Ini penting untuk memperbaiki pemahaman dan mengatasi hambatan dalam belajar.",
            )
        }
    }

    private fun isAllAnswered(): Boolean {
        return binding.radioGroup17.checkedRadioButtonId != -1 && binding.radioGroup18.checkedRadioButtonId != -1 && binding.radioGroup19.checkedRadioButtonId != -1 && binding.radioGroup20.checkedRadioButtonId != -1
    }

    private fun setupRadioGroups() {
        binding.radioGroup17.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio17_1 -> 1
                R.id.radio17_2 -> 2
                R.id.radio17_3 -> 3
                R.id.radio17_4 -> 4
                R.id.radio17_5 -> 5
                R.id.radio17_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("17", answer)
        }

        binding.radioGroup18.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio18_1 -> 1
                R.id.radio18_2 -> 2
                R.id.radio18_3 -> 3
                R.id.radio18_4 -> 4
                R.id.radio18_5 -> 5
                R.id.radio18_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("18", answer)
        }

        binding.radioGroup19.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio19_1 -> 1
                R.id.radio19_2 -> 2
                R.id.radio19_3 -> 3
                R.id.radio19_4 -> 4
                R.id.radio19_5 -> 5
                R.id.radio19_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("19", answer)
        }

        binding.radioGroup20.setOnCheckedChangeListener { _, checkedId ->
            val answer = when (checkedId) {
                R.id.radio20_1 -> 1
                R.id.radio20_2 -> 2
                R.id.radio20_3 -> 3
                R.id.radio20_4 -> 4
                R.id.radio20_5 -> 5
                R.id.radio20_6 -> 6
                else -> 0
            }
            viewModel.setAnswer("20", answer)
        }
        viewModel.logAnswers()
    }

    private fun restoreAnswers() {
        viewModel.getAnswers().value?.let { answers ->
            answers["17"]?.let { setRadioButtonChecked(binding.radioGroup17, it) }
            answers["18"]?.let { setRadioButtonChecked(binding.radioGroup18, it) }
            answers["19"]?.let { setRadioButtonChecked(binding.radioGroup19, it) }
            answers["20"]?.let { setRadioButtonChecked(binding.radioGroup20, it) }
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
        fun newInstance(viewModel: QuestionnaireViewModel) = HelpSeekingQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}