package com.dl2lab.srolqs.ui.kegiatan.tambah

import KegiatanViewModelFactory
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.request.TambahKegiatanRequest
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.databinding.FragmentAddKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import java.util.Calendar
import java.util.Locale

class AddKegiatanFragment : Fragment() {

    private lateinit var binding: FragmentAddKegiatanBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val kegiatanViewModel: KegiatanViewModel by viewModels {
        KegiatanViewModelFactory(KegiatanRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddKegiatanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Date Picker for tenggat (due date)
        binding.inputTanggalKegiatan.setOnClickListener { showDatePicker() }
        binding.inputTanggalKegiatanLayout.setEndIconOnClickListener { showDatePicker() }

        // Set up dropdown for tipe kegiatan
        setUpTipeKegiatanDropdown()

        // Observe token from ProfileViewModel
        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                setupAddKegiatanListener(token)
            }
        })

        // Observe the result of addKegiatan
        kegiatanViewModel.addKegiatanResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Kegiatan berhasil ditambah", Toast.LENGTH_SHORT).show()
                // Navigate back to KegiatanFragment
                findNavController().navigate(R.id.action_addKegiatanFragment_to_navigation_kegiatan)
            }.onFailure {
                Toast.makeText(requireContext(), "Gagal menambah kegiatan: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })

        kegiatanViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun setupAddKegiatanListener(token: String) {
        binding.btnTambahKegiatan.setOnClickListener {
            addKegiatan(token)
        }
    }

    private fun addKegiatan(token: String) {
        // Collect input data from form fields
        val namaKegiatan = binding.inputName.text.toString()
        val tipeKegiatan = binding.inputTipeKegiatan.text.toString()
        val tenggat = binding.inputTanggalKegiatan.text.toString()
        val catatan = binding.inputCatatanKegiatan.text.toString()
        val link = binding.inputLinkKegiatan.text.toString()

        // Validate inputs
        if (namaKegiatan.isEmpty() || tipeKegiatan.isEmpty() || tenggat.isEmpty() || catatan.isEmpty() || link.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create request object
        val request = TambahKegiatanRequest(
            namaKegiatan = namaKegiatan,
            tipeKegiatan = tipeKegiatan,
            tenggat = tenggat,
            catatan = catatan,
            link = link
        )

        // Call ViewModel function with the token
        Log.d("AddKegiatanFragment", "Token: $token")
        kegiatanViewModel.addKegiatan(request, token)
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
        binding.inputTipeKegiatan.setText(tipeKegiatanOptions[0], false) // Set default to "Tugas"
    }
}
