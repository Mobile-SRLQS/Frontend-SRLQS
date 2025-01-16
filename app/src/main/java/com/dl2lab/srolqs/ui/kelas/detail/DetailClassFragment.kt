package com.dl2lab.srolqs.ui.kelas.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.data.remote.response.PeriodDataItem
import com.dl2lab.srolqs.databinding.FragmentDetailClassBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.adapter.OnPeriodItemClickListener
import com.dl2lab.srolqs.ui.home.adapter.PeriodAdapter
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.utils.ExtractErrorMessage
import com.dl2lab.srolqs.utils.JwtUtils


class DetailClassFragment : Fragment(), OnPeriodItemClickListener {

    private var _binding: FragmentDetailClassBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val args: DetailClassFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        checkUserSession()
        setupDetailClass()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupDetailClass() {
        val classItem = args.classItem
        classItem.classId?.let {
            showLoading(true)
            viewModel.getClassInformation(it).observe(viewLifecycleOwner, Observer { response ->
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if(classItem.progress == "33%"){
                            binding.courseProgress.text = "Progress Pengukuran 1 dari 3 Periode (${classItem.progress})"
                        } else if(classItem.progress == "66%"){
                            binding.courseProgress.text = "Progress Pengukuran 2 dari 3 Periode (${classItem.progress})"
                        } else if(classItem.progress == "100%"){
                            binding.courseProgress.text = "Progress Pengukuran 3 dari 3 Periode (${classItem.progress})"
                        }
                        if (body != null) {
                            binding.headingCourseTitle.text = body.data?.className
                            binding.courseSemesterInformation.text = body.data?.classSemester
                            binding.courseDescription.text = body.data?.classDescription
                        }
                        val periodList = response.body()?.data?.periodData
                        val adapter = periodList?.let { it1 -> PeriodAdapter(it1, this) }
                        binding.rvPeriodList.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvPeriodList.adapter = adapter
                    } else {
                        requireContext().showCustomAlertDialog(
                            "",
                            ExtractErrorMessage.extractErrorMessage(response),
                            "OK",
                            "",
                            {},
                            {
                                binding.btnBack.performClick()
                            },
                        )
                    }
                } catch (e: Exception) {
                    requireContext().showCustomAlertDialog(
                        "Error",
                        "An unexpected error occurred.",
                        "OK",
                        "",
                        {},
                        {}
                    )
                } finally {
                    showLoading(false)
                }
            })
        } ?: showLoading(false)
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
                    viewModel.logout()
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                    requireContext().showCustomAlertDialog(
                        "",
                        "Session Expired. Please login again!",
                        "Login",
                        "",
                        {},
                        {},
                    )
                }
            } else {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })

    }



    private fun showLoading(isLoading: Boolean) {
        binding.contentLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(periodItem: PeriodDataItem) {
    }
}
