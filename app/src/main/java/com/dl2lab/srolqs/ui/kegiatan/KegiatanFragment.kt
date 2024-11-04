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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.databinding.FragmentKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kegiatan.adapter.KegiatanAdapter
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel

class KegiatanFragment : Fragment() {

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

        // Set up RecyclerView
        binding.rvKegiatan.layoutManager = LinearLayoutManager(requireContext())

        // Observe token from ProfileViewModel
        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                // Load data (fetch list of kegiatan) with the token
                kegiatanViewModel.fetchKegiatanList(token)
            }
        })

        // Observe the kegiatan list from KegiatanViewModel
        kegiatanViewModel.kegiatanList.observe(viewLifecycleOwner, Observer { kegiatanList ->
            if (kegiatanList.isNullOrEmpty()) {
                showEmptyState()
            } else {
                hideEmptyState()
                setupRecyclerView(kegiatanList)
            }
        })

        // Add button click listener
        binding.btnAddKegiatan.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_kegiatan_to_addKegiatanFragment)
        }

        kegiatanViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun setupRecyclerView(kegiatanList: List<KegiatanItem>) {
        val adapter = KegiatanAdapter(kegiatanList,
            onEditClick = { kegiatan ->
                // Handle edit action here
                Toast.makeText(requireContext(), "Edit ${kegiatan.namaKegiatan}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { kegiatan ->
                // Handle delete action here
                Toast.makeText(requireContext(), "Delete ${kegiatan.namaKegiatan}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvKegiatan.adapter = adapter
    }


    private fun showEmptyState() {
        if (binding.viewstubEmptyState.parent != null) {
            binding.viewstubEmptyState.inflate()
        }
        binding.rvKegiatan.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.viewstubEmptyState.visibility = View.GONE // Hide inflated empty state if it exists
        binding.rvKegiatan.visibility = View.VISIBLE
    }
}
