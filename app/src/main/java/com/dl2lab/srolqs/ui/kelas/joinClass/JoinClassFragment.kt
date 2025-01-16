package com.dl2lab.srolqs.ui.kelas.joinClass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentJoinClassBinding
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dl2lab.srolqs.data.remote.response.JoinDetailClassResponse
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.kuesioner.question.QuestionnaireQuestionActivity
import com.dl2lab.srolqs.utils.ExtractErrorMessage.extractErrorMessage
import com.dl2lab.srolqs.utils.JwtUtils
import com.dl2lab.srolqs.worker.QuestionnaireNotificationWorker
import java.util.UUID

class JoinClassFragment : Fragment() {

    private var _binding: FragmentJoinClassBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private lateinit var classData: JoinDetailClassResponse
    private val args: JoinClassFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinClassBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        setupClassDetail()
        setupButton()

        return root
    }

    private fun setupButton() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_joinClassFragment_to_navigation_home)
        }
        binding.btnDoQuesionnaire.setOnClickListener {
            doQuestionnaire()
        }
    }

    private fun setupNotificationWorker() {

        if (::classData.isInitialized && classData.data?.periods != null) {
            val periods = classData.data?.periods
            for (i in periods!!.indices) {
                val periodName = periods[i]?.periodName
                val endDate = periods[i]?.endDate
                if (periodName != null && endDate != null) {
                    val workRequest = OneTimeWorkRequestBuilder<QuestionnaireNotificationWorker>()
                        .setInputData(
                            workDataOf(
                                "class_name" to classData.data?.className,
                                "period_name" to periodName,
                                "end_date" to endDate
                            )
                        )
                        .build()
                    WorkManager.getInstance(requireContext()).enqueue(workRequest)
                }
            }
        }
    }

    private fun doQuestionnaire() {
        val classId = args.classId
        if (classId != null) {
            viewModel.joinClass(classId).observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    val body = response.body()
                    val intent = Intent(requireContext(), QuestionnaireQuestionActivity::class.java)
                    intent.putExtra("classId", classId)
                    intent.putExtra("period", "1")
                    startActivity(intent)
                    setupNotificationWorker()
                } else {
                    requireContext().showCustomAlertDialog(
                        "",
                        extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {
                            findNavController().navigate(R.id.action_joinClassFragment_to_navigation_home)
                        },
                    )
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(MainViewModel::class.java)
    }

    private fun checkUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if (userModel.token != null) {
                if (JwtUtils.isTokenExpired(userModel.token)) {
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                    requireContext().showCustomAlertDialog(
                        "",
                        "Session Expired. Please login again!",
                        "Login",
                        "",
                        { viewModel.logout() },
                        { },
                    )
                    viewModel.logout()
                }
            } else {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    private fun setupClassDetail() {
        val classId = args.classId
        if (classId != null) {
            viewModel.getClassDetail(classId).observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        classData = body

                        binding.headingCourseTitle.text = body.data?.className
                        binding.courseSemesterInformation.text = body.data?.classSemester
                        binding.courseDescription.text = body.data?.classDescription
                    }
                } else {
                    requireContext().showCustomAlertDialog(
                        "",
                        extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {
                            findNavController().navigate(R.id.action_changePasswordFragment_to_navigation_profile)
                        },
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        LoadingManager.cleanup()
        _binding = null
    }
}