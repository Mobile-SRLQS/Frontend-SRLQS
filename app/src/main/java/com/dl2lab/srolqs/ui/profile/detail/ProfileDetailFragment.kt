package com.dl2lab.srolqs.ui.profile.detail

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
import com.dl2lab.srolqs.databinding.FragmentHomeBinding
import com.dl2lab.srolqs.databinding.FragmentProfileDetailBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileDetailFragment : Fragment() {

    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()
        getDetailProfileInformation()
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileDetailFragment_to_navigation_profile)

        }
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileDetailFragment_to_profileEditFragment)
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



    private fun getDetailProfileInformation(){
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            binding.profileName.text = userModel.nama
            binding.profileIdentityNumber.text = userModel.identityNumber
            binding.profileEmail.text = userModel.email
            binding.profileDob.text = formatTanggal(userModel.birthDate)
            binding.profileInstitution.text = userModel.institution
            binding.profileBatch.text = userModel.batch
            binding.profileDegree.text = userModel.degree

        })
    }
    fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(tanggal) ?: return tanggal
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            tanggal
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}