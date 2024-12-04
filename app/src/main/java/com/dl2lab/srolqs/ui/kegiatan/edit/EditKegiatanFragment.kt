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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.data.remote.request.UpdateKegiatanRequest
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.data.repository.KegiatanRepository
import com.dl2lab.srolqs.databinding.FragmentEditKegiatanBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel
import com.dl2lab.srolqs.ui.kegiatan.detail.DetailKegiatanFragmentDirections
import com.dl2lab.srolqs.ui.kegiatan.viewmodel.KegiatanViewModel
import java.text.SimpleDateFormat
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
        LoadingManager.init(this)
        _binding = FragmentEditKegiatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            kegiatanId = it.getInt("kegiatanId")
        }
        LoadingManager.init(this)

        binding.inputTanggalKegiatan.setOnClickListener { showDatePicker() }
        binding.inputTanggalKegiatanLayout.setEndIconOnClickListener { showDatePicker() }

        setUpTipeKegiatanDropdown()

        viewModel.kegiatanItem.observe(viewLifecycleOwner, Observer { item ->
            kegiatanItem = item
            binding.inputName.setText(item.namaKegiatan)
            binding.inputTipeKegiatan.setText(item.tipeKegiatan, false)
            binding.inputLinkKegiatan.setText(item.link)
            binding.inputTanggalKegiatan.setText(item.tenggat.formatDateFromApi())
            binding.inputCatatanKegiatan.setText(item.catatan)
            Log.d("EditKegiatanFragment", "Kegiatan Item: $item")
        })
        getKegiatanDetail()
        observeViewModel()
        binding.btnTambahKegiatan.setOnClickListener {
            updateKegiatan()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LoadingManager.cleanup()
    }
    fun String.formatDateFromApi(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id")) // Format Indonesia

            val date = inputFormat.parse(this)
            date?.let { outputFormat.format(it) } ?: this
        } catch (e: Exception) {
            this // Return original string if parsing fails
        }
    }


    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                LoadingManager.show()
            } else {
                LoadingManager.hide()
            }
        })

        viewModel.updateResult.observe(viewLifecycleOwner, Observer { result ->
            result.fold(
                onSuccess = {
                    requireContext().showCustomAlertDialog(
                        title = "Edit Kegiatan Berhasil",
                        subtitle = "Kegiatan \'${it.data.namaKegiatan}\' berhasil diubah",
                        positiveButtonText = "OK",
                        negativeButtonText = "",
                        onPositiveButtonClick ={
                            Navigation.findNavController(requireView()).popBackStack()
                        },
                        onNegativeButtonClick = {},
                        error =false,
                    )


                },
                onFailure = {
                    requireContext().showCustomAlertDialog(
                        title = "Edit Kegiatan Gagal",
                        subtitle = it.message ?: "Terjadi kesalahan saat mengubah kegiatan",
                        positiveButtonText = "OK",
                        negativeButtonText = "",
                        onPositiveButtonClick ={},
                        onNegativeButtonClick = {},
                        error =true,
                    )
                }
            )
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {

                requireContext().showCustomAlertDialog(
                    title = "Edit Kegiatan Gagal",
                    subtitle = it ?: "Terjadi kesalahan saat menambah kegiatan",
                    positiveButtonText = "Kembali",
                    negativeButtonText = "",
                    onPositiveButtonClick ={
                        findNavController().popBackStack()
                    },
                    onNegativeButtonClick = {},
                    error =true,
                )
            }
        })
    }

    private fun getKegiatanDetail() {
        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                viewModel.fetchDetailKegiatan(kegiatanId!!, token)
            }
        })
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