package com.dl2lab.srolqs.ui.profile.about

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
import com.dl2lab.srolqs.databinding.FragmentAboutBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment2_to_navigation_profile)

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
            if (!userModel.isLogin!!) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}