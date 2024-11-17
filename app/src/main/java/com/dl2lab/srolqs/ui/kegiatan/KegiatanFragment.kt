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


class KegiatanFragment : Fragment(), KegiatanAdapter.OnKegiatanItemClickListener {

    private lateinit var binding: FragmentKegiatanBinding
    private val kegiatanViewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKegiatanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvKegiatan.layoutManager = LinearLayoutManager(requireContext())
        setUpSearchBar()

        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                kegiatanViewModel.fetchKegiatanList(token)
            }
        })

        kegiatanViewModel.kegiatanList.observe(viewLifecycleOwner, Observer { kegiatanList ->
            if (kegiatanList.isNullOrEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                setupRecyclerView(kegiatanList)
            }
        })

        binding.btnAddKegiatan.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_kegiatan_to_addKegiatanFragment)
        }

        kegiatanViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
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


    private fun setupRecyclerView(kegiatanList: List<KegiatanItem>) {
        val adapter = KegiatanAdapter(kegiatanList, this)
        binding.rvKegiatan.adapter = adapter
        if (kegiatanList.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    override fun onItemClick(kegiatanItem: KegiatanItem) {
        val action = KegiatanFragmentDirections.actionNavigationKegiatanToDetailKegiatanFragment(kegiatanItem.id)
        findNavController().navigate(action)
    }

    override fun onItemChecked(kegiatanItem: KegiatanItem, isChecked: Boolean) {
        if (isChecked) {
            profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
                user.token?.let { token ->
                    kegiatanViewModel.checklistKegiatan(kegiatanItem.id, token)
                }
            })
        }
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