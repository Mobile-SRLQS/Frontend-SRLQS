package com.dl2lab.srolqs.ui.kegiatan.detail

import KegiatanViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dl2lab.srolqs.databinding.FragmentDetailKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailKegiatanFragment : Fragment() {
    private var kegiatanId: Int? = null
    private var _binding: FragmentDetailKegiatanBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val viewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }
    private var kegiatanItem: KegiatanItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailKegiatanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            kegiatanId = it.getInt("kegiatanId")
        }

        if (kegiatanId != null) {
            getKegiatanDetail()
        } else {
            Log.e("DetailKegiatanFragment", "kegiatanId is null")
        }
    }

    private fun getKegiatanDetail() {
        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                viewModel.fetchDetailKegiatan(kegiatanId!!, token)
            }
        })

        viewModel.kegiatanItem.observe(viewLifecycleOwner, Observer { item ->
            if (item != null) {
                kegiatanItem = item
                binding.headingKegiatanTitle.text = item.namaKegiatan
                binding.tvTagKegiatan.text = item.tipeKegiatan
                binding.tvLinkKegiatan.text = item.link
                binding.tvTenggatKegiatan.text = formatTanggal(item.tenggat)
                binding.tvDeskripsiKegiatan.text = item.catatan
                binding.btnEdit.setOnClickListener {
                    Log.d("DetailKegiatanFragment", "Edit kegiatan clicked")
                    Log.d("DetailKegiatanFragment", "Kegiatan item: $kegiatanItem")

                    val action = DetailKegiatanFragmentDirections.actionNavigationDetailKegiatanToEditKegiatanFragment(item.id)
                    findNavController().navigate(action)
                }
            } else {
                Log.e("DetailKegiatanFragment", "kegiatanItem is null")
            }
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