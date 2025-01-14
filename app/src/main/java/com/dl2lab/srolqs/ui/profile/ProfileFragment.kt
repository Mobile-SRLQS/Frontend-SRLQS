package com.dl2lab.srolqs.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dl2lab.srolqs.databinding.FragmentProfileBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog

class ProfileFragment : Fragment() {


    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        setupProfileDetail()
        setupButton()

        return root
    }

    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(ProfileViewModel::class.java)
    }

    private fun checkUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if (!userModel.isLogin!!) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    private fun setupProfileDetail() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            binding.profileName.text = userModel.nama
            binding.profileEmail.text = userModel.email
            binding.profileInstitution.text = userModel.institution
            Glide.with(this)
                .load(userModel.profilePicture)
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(binding.icon)
        })

        viewModel.getDetailProfile().observe(viewLifecycleOwner) { updatedUser ->
            updatedUser?.let {
                viewModel.saveSession(it) // Save updated user details to session
            }
        }
    }

    private fun setupButton() {
        binding.buttonAccountInformation.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_profileDetailFragment)
        }
        binding.buttonUpdatePassword.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_changePasswordFragment)
        }
        binding.buttonLogout.setOnClickListener {

            requireContext().showCustomAlertDialog(
                title = "Apakah Anda yakin ingin logout dari aplikasi?",
                subtitle = "",
                positiveButtonText = "Logout",
                negativeButtonText = "Tidak",
                onPositiveButtonClick = {
                    viewModel.logout()
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                },
                onNegativeButtonClick = {
                },
                showIcon = false
            )
        }

        binding.buttonAboutApplication.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_aboutFragment2)
        }
        binding.buttonFaq.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_faqFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
