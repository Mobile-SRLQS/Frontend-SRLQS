package com.dl2lab.srolqs.ui.home.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.databinding.FragmentHomeBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.adapter.ClassAdapter
import com.dl2lab.srolqs.ui.home.adapter.OnClassItemClickListener
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.kegiatan.adapter.KegiatanAdapter
import com.dl2lab.srolqs.utils.JwtUtils

class HomeFragment : Fragment(), OnClassItemClickListener, KegiatanAdapter.OnKegiatanItemClickListener {


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
        getActivityList()

        return root
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
                        "",
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
        binding.btnJoinClass.setOnClickListener{
            val classId= binding.etCourseCode.text.toString()
            if (classId.isEmpty()) {
                requireContext().showCustomAlertDialog(
                    "",
                    "Tolong masukkan kode kelas terlebih dahulu",
                    "OK",
                    "",
                    {},
                    {},
                )
            } else{
                viewModel.getClassDetail(classId).observe(viewLifecycleOwner) { response ->
                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body != null) {
                            // Dismiss any showing dialogs
                            val currentDialog = requireActivity().supportFragmentManager.findFragmentByTag("CustomDialog")
                            if (currentDialog != null) {
                                (currentDialog as DialogFragment).dismiss()
                            }
                            val action = HomeFragmentDirections.actionNavigationHomeToJoinClassFragment(classId)
                            findNavController().navigate(action)
                        }
                    } else {
                        requireContext().showCustomAlertDialog(
                            "",
                            "Pastikan kode kelas yang anda masukkan benar",
                            "OK",
                            "",
                            {},
                            {},
                        )
                    }
                }

            }
        }




    }
    override fun onItemClick(classItem: DataItem) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailClassFragment(classItem)
        findNavController().navigate(action)
    }

    fun getClassList() {
        viewModel.getListClass().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val classList = response.body()?.data ?: emptyList()
                val limitedClassList = classList.take(3) // Ini akan mengambil tiga item pertama
                val adapter = ClassAdapter(limitedClassList, this)
                binding.rvCourseList.layoutManager = LinearLayoutManager(requireContext())
                binding.rvCourseList.adapter = adapter
            } else {

            }
        })
    }

    fun getActivityList() {
        viewModel.getListKegiatan().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val listKegiatan = response.body()?.data ?: emptyList()
                val limitedListKegiatan = listKegiatan.take(3) // Ini akan mengambil tiga item pertama
                val adapter = KegiatanAdapter(limitedListKegiatan, this)
                binding.rvActivityList.layoutManager = LinearLayoutManager(requireContext())
                binding.rvActivityList.adapter = adapter
            } else {

            }
        })
    }

    private fun getUserName(){
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            binding.tvGreeting.text = "Hello, ${userModel.nama}"
        })
    }

    override fun onItemClick(kegiatanItem: KegiatanItem) {
        // Handle the item click event for KegiatanItem
//        val action = HomeFragmentDirections.actionNavigationHomeToDetailKegiatanFragment(kegiatanItem)
//        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}