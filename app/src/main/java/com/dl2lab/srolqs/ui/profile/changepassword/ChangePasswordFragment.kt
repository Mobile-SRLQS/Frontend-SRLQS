package com.dl2lab.srolqs.ui.profile.changepassword

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.FragmentChangePasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import com.dl2lab.srolqs.utils.JwtUtils.isTokenExpired
import org.json.JSONObject
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChangePasswordFragment : Fragment() {

        private var _binding: FragmentChangePasswordBinding? = null
        private val binding get() = _binding!!
        private lateinit var viewModel: ProfileViewModel

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

            val root: View = binding.root
            setupViewModel()
            checkUserSession()

            binding.btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_changePasswordFragment_to_navigation_profile)

            }
            binding.btnSaveChanges.setOnClickListener {
                changePassword()
            }

            return root
        }

        private fun setupViewModel() {

            viewModel = ViewModelProvider(
                requireActivity(), ViewModelFactory.getInstance(requireContext())
            ).get(ProfileViewModel::class.java)
        }
        private fun checkUserSession() {
            viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
                if(userModel.token != null) {
                    if (isTokenExpired(userModel.token)) {
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

        private fun changePassword(){
            val oldPassword = binding.inputOldPassword.text.toString()
            val newPassword = binding.inputNewPassword.text.toString()
            val confirmedPassword = binding.inputConfirmNewPassword.text.toString()

            if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmedPassword.isEmpty()){
                requireContext().showCustomAlertDialog(
                    "Please input all fields",
                    "OK",
                    "Cancel",
                    {},
                    {
                        binding.btnBack.performClick()
                    },
                )
                return
            }

            if(newPassword != confirmedPassword){
                requireContext().showCustomAlertDialog(
                    "New password and confirmed password must be the same",
                    "OK",
                    "Cancel",
                    {},
                    {
                        binding.btnBack.performClick()
                    },
                )
                return
            }

            viewModel.changePassword(oldPassword, newPassword, confirmedPassword).observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        requireContext().showCustomAlertDialog(
                            body.message ?: "Change password success",
                            "OK",
                            "",
                            {
                                binding.btnBack.performClick()
                            },
                            {
                                binding.btnBack.performClick()
                            },
                        )
                    }
                } else {
                    requireContext().showCustomAlertDialog(
                        extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {
                            binding.btnBack.performClick()
                        },
                    )
                }

            }



        }

    private fun extractErrorMessage(response: Response<BasicResponse>): String {
        return try {
            val json = response.errorBody()?.string()
            val jsonObject = JSONObject(json)
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Unknown Error"
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }