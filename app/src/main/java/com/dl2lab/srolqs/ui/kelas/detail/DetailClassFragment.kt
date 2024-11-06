package com.dl2lab.srolqs.ui.kelas.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.data.remote.response.PeriodDataItem
import com.dl2lab.srolqs.databinding.FragmentChangePasswordBinding
import com.dl2lab.srolqs.databinding.FragmentDetailClassBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.adapter.ClassAdapter
import com.dl2lab.srolqs.ui.home.adapter.OnClassItemClickListener
import com.dl2lab.srolqs.ui.home.adapter.OnPeriodItemClickListener
import com.dl2lab.srolqs.ui.home.adapter.PeriodAdapter
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import com.dl2lab.srolqs.utils.ExtractErrorMessage
import com.dl2lab.srolqs.utils.JwtUtils
import org.json.JSONObject
import retrofit2.Response


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

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        setupDetailClass()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }

    private fun setupDetailClass(){
        val classItem = args.classItem
        classItem.classId?.let {
            viewModel.getClassInformation(it).observe(viewLifecycleOwner, Observer { response ->
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
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
                        ExtractErrorMessage.extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {
                            binding.btnBack.performClick()
                        },
                    )
                }
            })
        }

    }

    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(MainViewModel::class.java)
    }
    private fun checkUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if(userModel.token != null) {
                if (JwtUtils.isTokenExpired(userModel.token)) {
                    viewModel.logout()
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                    requireContext().showCustomAlertDialog(
                        "Session Expired. Please login again!",
                        "Login",
                        "",
                        {},
                        {},
                    )
                }
            } else{
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(periodItem: PeriodDataItem) {

    }
}