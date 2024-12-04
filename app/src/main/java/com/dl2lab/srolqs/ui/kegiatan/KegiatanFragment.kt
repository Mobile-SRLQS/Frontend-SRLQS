package com.dl2lab.srolqs.ui.kegiatan

import KegiatanViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.databinding.FragmentKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kegiatan.adapter.KegiatanAdapter
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import androidx.appcompat.widget.SearchView
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.TextAttribute
import com.akndmr.library.Type
import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.adapter.ClassAdapter
import com.facebook.shimmer.ShimmerFrameLayout


class KegiatanFragment : Fragment(), KegiatanAdapter.OnKegiatanItemClickListener {

    private lateinit var binding: FragmentKegiatanBinding
    private val kegiatanViewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKegiatanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvKegiatan.layoutManager = LinearLayoutManager(requireContext())
        setUpSearchBar()
        getKegiatanData()

        binding.btnAddKegiatan.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_kegiatan_to_addKegiatanFragment)
        }

    }

    private fun getKegiatanData() {
        showRVActivity(false)
        showShimmerActivity(true)

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            user.token.let { token ->
                kegiatanViewModel.fetchKegiatanList(token)

                kegiatanViewModel.kegiatanList.observe(viewLifecycleOwner) { kegiatanList ->
                    showShimmerActivity(false)

                    if (kegiatanList.isNullOrEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                        setupRecyclerView(kegiatanList)
                        showRVActivity(true)
                    }
                }

                kegiatanViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(
                            requireContext(),
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                kegiatanViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                    showShimmerActivity(isLoading)
                    showRVActivity(!isLoading)
                }
            }
        }
    }

    private fun setUpSearchBar() {
        binding.searchView.queryHint = "Cari kegiatan Anda"
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterKegiatanList(it)
                }
                return true
            }
        })
    }

    private fun filterKegiatanList(query: String) {
        val filteredList = kegiatanViewModel.kegiatanList.value?.filter {
            it.namaKegiatan.contains(query, ignoreCase = true)
        } ?: emptyList()



        setupRecyclerView(filteredList)
    }

    private fun showRVActivity(isShow: Boolean) {
        if (isShow) {
            binding.rvKegiatan.visibility = View.VISIBLE
        } else {
            binding.rvKegiatan.visibility = View.INVISIBLE
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


    private fun setupRecyclerView(kegiatanList: List<KegiatanItem>) {
        val adapter = KegiatanAdapter(kegiatanList, this)
        binding.rvKegiatan.adapter = adapter
        binding.rvKegiatan.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onItemClick(kegiatanItem: KegiatanItem) {
        val action =
            KegiatanFragmentDirections.actionNavigationKegiatanToDetailKegiatanFragment(kegiatanItem.id)
        findNavController().navigate(action)
    }

    override fun onMarkAsDoneClick(idKegiatan: Int) {
        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            user.token.let { token ->
                kegiatanViewModel.checklistKegiatan(idKegiatan, token)
            }
        }
        kegiatanViewModel.checklistResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {

                AirySnackbar.make(
                    source = AirySnackbarSource.ViewSource(binding.root),
                    type = Type.Success,
                    attributes = listOf(
                        TextAttribute.Text(text = "Berhasil menyelesaikan kegiatan!"),
                        TextAttribute.TextColor(textColor = R.color.white),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Bottom,
                        AnimationAttribute.FadeInOut
                    )
                ).show()
                getKegiatanData()
            } else {
                result?.message?.let {
                    requireContext().showCustomAlertDialog(
                        "Gagal Menyelesaikan Kegiatan",
                        it,
                        "Coba Ulang",
                        "",
                        {
                            onMarkAsDoneClick(idKegiatan)
                        },
                        {}
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getKegiatanData()
    }



    private fun showEmptyState() {
        if (binding.viewstubEmptyState.parent != null) {
            binding.viewstubEmptyState.inflate()
        }
        binding.viewstubEmptyState.visibility = View.VISIBLE
        binding.rvKegiatan.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.viewstubEmptyState.visibility = View.GONE
        binding.rvKegiatan.visibility = View.VISIBLE
    }
}