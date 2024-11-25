package com.dl2lab.srolqs.ui.kegiatan.edit

import KegiatanViewModelFactory
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.request.UpdateKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.databinding.FragmentEditKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.kegiatan.detail.DetailKegiatanFragmentDirections
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import java.util.Calendar
import java.util.Locale

class EditKegiatanFragment : Fragment() {
    private var _binding: FragmentEditKegiatanBinding? = null
    private var kegiatanId: Int? = null
    private var kegiatanItem: KegiatanItem? = null

    private val binding get() = _binding!!
    private val viewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditKegiatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            kegiatanId = it.getInt("kegiatanId")
        }

        binding.inputTanggalKegiatan.setOnClickListener { showDatePicker() }
        binding.inputTanggalKegiatanLayout.setEndIconOnClickListener { showDatePicker() }

        setUpTipeKegiatanDropdown()

        viewModel.kegiatanItem.observe(viewLifecycleOwner, Observer { item ->
            kegiatanItem = item
            binding.inputName.setText(item.namaKegiatan)
            binding.inputTipeKegiatan.setText(item.tipeKegiatan, false)
            binding.inputLinkKegiatan.setText(item.link)
            binding.inputTanggalKegiatan.setText(item.tenggat)
            binding.inputCatatanKegiatan.setText(item.catatan)
            Log.d("EditKegiatanFragment", "Kegiatan Item: $item")
        })
        getKegiatanDetail()
        binding.btnTambahKegiatan.setOnClickListener {
            updateKegiatan()
            Toast.makeText(context, "Kegiatan berhasil diubah", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getKegiatanDetail() {
        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                viewModel.fetchDetailKegiatan(kegiatanId!!, token)
            }
        })
        Log.d("EditKegiatanFragment", "Kegiatan ID: $kegiatanId")
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.inputTanggalKegiatan.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setUpTipeKegiatanDropdown() {
        val tipeKegiatanOptions = arrayOf("Tugas", "Belajar", "Meeting", "Presentasi")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tipeKegiatanOptions)
        binding.inputTipeKegiatan.setAdapter(adapter)
        binding.inputTipeKegiatan.setOnClickListener { binding.inputTipeKegiatan.showDropDown() }
    }

    private fun updateKegiatan() {
        val namaKegiatan = binding.inputName.text.toString()
        val tipeKegiatan = binding.inputTipeKegiatan.text.toString()
        val link = binding.inputLinkKegiatan.text.toString()
        val catatan = binding.inputCatatanKegiatan.text.toString()
        val tenggat = binding.inputTanggalKegiatan.text.toString()
        val request = UpdateKegiatanRequest(namaKegiatan, tipeKegiatan, link, catatan, tenggat)
        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                viewModel.updateKegiatan(request, kegiatanItem!!.id, token)
            }
        })
    }
}