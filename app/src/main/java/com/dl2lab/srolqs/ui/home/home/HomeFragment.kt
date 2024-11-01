package com.dl2lab.srolqs.ui.home.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.databinding.FragmentHomeBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.adapter.ClassAdapter
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.utils.ExtractErrorMessage
import com.dl2lab.srolqs.utils.JwtUtils

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        getUserName()
        setupJoinClass()
        getClassList()

        return root
    }

    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(MainViewModel::class.java)
    }

    private fun checkUserSession() {
        viewModel.getToken().observe(viewLifecycleOwner, Observer { userModel ->
            if (userModel.token == null) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })

        viewModel.getSessionWithToken().observe(viewLifecycleOwner, Observer { userModel ->
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

    fun setupJoinClass(){
        binding.btnJoinClass.setOnClickListener {
            val classId= binding.etCourseCode.text.toString()
            if (classId.isEmpty()){
                requireContext().showCustomAlertDialog(
                    "Please fill the class code",
                    "OK",
                    "",
                    {},
                    {},
                )
                return@setOnClickListener
            }
            viewModel.joinClass(classId).observe(viewLifecycleOwner, Observer { response ->
                if (response.isSuccessful) {
                    requireContext().showCustomAlertDialog(
                        response.body()?.message ?: "Success join class",
                        "OK",
                        "",
                        {},
                        {},
                    )
                } else {
                    requireContext().showCustomAlertDialog(
                        ExtractErrorMessage.extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {},
                    )
                }
            })
        }
    }

    fun getClassList() {
        viewModel.getListClass().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val classList = response.body()?.data ?: emptyList()
                val adapter = ClassAdapter(classList)
                binding.rvCourseList.layoutManager = LinearLayoutManager(requireContext())
                binding.rvCourseList.adapter = adapter
            } else {

            }
        })
    }

    private fun getUserName(){
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            binding.tvGreeting.text = "Hello, ${userModel.nama}"
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}