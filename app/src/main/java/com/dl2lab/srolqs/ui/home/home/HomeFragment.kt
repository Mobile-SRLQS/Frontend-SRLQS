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
import com.facebook.shimmer.ShimmerFrameLayout

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

    private fun showShimmerClass(isShow: Boolean) {
        var linearLayout = binding.shimmerViewContainer
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            if (child is ShimmerFrameLayout) {
                if (isShow) {
                    child.startShimmer()
                } else {
                    child.stopShimmer()
                }
            }
        }
        if (isShow) {
            linearLayout.visibility = View.VISIBLE
        } else {
            linearLayout.visibility = View.GONE
        }
    }

    private fun showClassList(isShow: Boolean){
        if (isShow) {
            binding.rvCourseList.visibility = View.VISIBLE
        } else {
            binding.rvCourseList.visibility = View.INVISIBLE
        }
    }

    private fun showEmptyClassList(isShow: Boolean){
        if (isShow) {
            binding.clCourseListEmpty.visibility = View.VISIBLE
        } else {
            binding.clCourseListEmpty.visibility = View.INVISIBLE
        }
    }

    fun getClassList() {
        showShimmerClass(true)
        viewModel.getListClass().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                showShimmerClass(false)
                val classList = response.body()?.data ?: emptyList()

                if (classList.isEmpty()) {
                    showEmptyClassList(true)
                } else {
                    val limitedClassList = classList.take(3) // Ini akan mengambil tiga item pertama
                    val adapter = ClassAdapter(limitedClassList, this)
                    binding.rvCourseList.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvCourseList.adapter = adapter
                    showClassList(true)

                }

            } else {
                showShimmerClass(false)
                showEmptyClassList(true)
                binding.tvEmptyClassList.text = "Gagal memuat data kelas"

            }
        })
    }
    private fun showEmptyActivityList(isShow: Boolean){
        if (isShow) {
            binding.clActivityEmpty.visibility = View.VISIBLE
        } else {
            binding.clActivityEmpty.visibility = View.INVISIBLE
        }
    }

    private fun showShimmerActivity(isShow: Boolean) {
        var linearLayout = binding.shimmerActivityContainer
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            if (child is ShimmerFrameLayout) {
                if (isShow) {
                    child.startShimmer()
                } else {
                    child.stopShimmer()
                }
            }
        }
        if (isShow) {
            linearLayout.visibility = View.VISIBLE
        } else {
            linearLayout.visibility = View.GONE
        }
    }

    fun getActivityList() {
        showShimmerActivity(true)
        viewModel.getListKegiatan().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                showShimmerActivity(false)
                val listKegiatan = response.body()?.data ?: emptyList()
                if(listKegiatan.isEmpty()){
                    showEmptyActivityList(true)
                } else {
                    showEmptyActivityList(false)
                    val limitedListKegiatan =
                        listKegiatan.take(3) // Ini akan mengambil tiga item pertama
                    val adapter = KegiatanAdapter(limitedListKegiatan, this)
                    binding.rvActivityList.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvActivityList.adapter = adapter
                }
            } else {
                showShimmerActivity(false)
                showEmptyActivityList(true)
                binding.tvActivityEmpty.text = "Gagal memuat data kegiatan"



            }
        })
    }

    private fun getUserName(){
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            var nama= userModel.nama.split(" ").take(2).joinToString(" ")
            binding.tvGreeting.text = "Hello, ${nama}"
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