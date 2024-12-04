package com.dl2lab.srolqs.ui.kuesioner.question

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentSelfEvaluationQuestionBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.customview.showCustomInformation
import com.dl2lab.srolqs.ui.home.main.MainActivity
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.kuesioner.result.ChartActivity
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel
import com.dl2lab.srolqs.utils.ExtractErrorMessage
import com.dl2lab.srolqs.utils.ExtractErrorMessage.extractErrorMessage

class SelfEvaluationQuestionFragment(viewModel: QuestionnaireViewModel) : Fragment() {
    private lateinit var binding: FragmentSelfEvaluationQuestionBinding
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfEvaluationQuestionBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupRadioGroups()
        restoreAnswers()
    }

    fun observeError(){
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                requireContext().showCustomAlertDialog(
                    title = "Gagal Mengirim Kuesioner",
                    subtitle = "Terjadi kesalahan saat mengirim kuesioner. " + error,
                    positiveButtonText = "Coba Lagi",
                    negativeButtonText = "Keluar",
                    onPositiveButtonClick = {showFeedbackDialog()},
                    onNegativeButtonClick = {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                )
            }
        }
    }

    fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(QuestionnaireViewModel::class.java)
    }

    private fun isAllAnswered(): Boolean {
        return binding.radioGroup21.checkedRadioButtonId != -1 && binding.radioGroup22.checkedRadioButtonId != -1 && binding.radioGroup23.checkedRadioButtonId != -1 && binding.radioGroup24.checkedRadioButtonId != -1
    }

    private fun submitQS(feedback: String = ""){
        viewModel.logAnswers()
        val sortedAnswersArray = viewModel.getAnswersArray()
        Log.d(
            "FinalQuestionnaireFragment",
            "Sorted Answers Array: ${sortedAnswersArray.contentToString()}"
        )
        val answer = viewModel.getAnswerList()
        var period = viewModel.getPeriod().value ?: "1"
        var classId = viewModel.getClassId().value ?: ""
        viewModel.submitQuestionnaire(period, classId, answer, feedback) // Add feedback parameter to your API call
            .observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {

                    requireContext().showCustomAlertDialog(title ="Berhasil mengisi kuesioner",
                       subtitle =  "Selamat, jawaban Anda berhasil disimpan. Terima kasih telah mengisi kuesioner. Anda dapat melihat hasil kuesioner Anda dengan menekan tombol dibawah ini",
                      positiveButtonText =   "Lihat Hasil Kuesioner",
                       negativeButtonText =  "",
                        error=false,
                        onPositiveButtonClick = {

                            val intent = Intent(requireContext(), ChartActivity::class.java)
                            intent.putExtra("CLASSID", classId)
                            intent.putExtra("PERIOD", period)
                            startActivity(intent)
                            requireActivity().finish()
                        },
                        onNegativeButtonClick = {})


                }
            }
    }


    private fun setupAction() {
        binding.submitButton.setOnClickListener {
            if(isAllAnswered()){
                showFeedbackDialog()
            } else{
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
                    HelpSeekingQuestionFragment(viewModel),
                    HelpSeekingQuestionFragment::class.java.simpleName
                )
            }
        }

        binding.infoIcon.setOnClickListener {
            requireContext().showCustomInformation(
                "Self Evaluation",
                "Mengevaluasi kinerja belajar dengan mengukur hasil yang telah dicapai dibandingkan dengan tujuan awal. Ini mencakup refleksi terhadap apa yang sudah dikuasai dan apa yang masih perlu diperbaiki.",
            )
        }
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

    private fun showFeedbackDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_feedback)

        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnBack = dialog.findViewById<Button>(R.id.btn_back)
        val btnSubmit = dialog.findViewById<Button>(R.id.btn_submit)
        val etFeedback = dialog.findViewById<EditText>(R.id.et_feedback)

        btnBack.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val feedback = etFeedback.text.toString()
            dialog.dismiss()
            submitQS(feedback)
        }

        dialog.show()
    }


    companion object {
        fun newInstance(viewModel: QuestionnaireViewModel) = SelfEvaluationQuestionFragment(
            viewModel
        ).apply {
            this.viewModel = viewModel
        }
    }
}