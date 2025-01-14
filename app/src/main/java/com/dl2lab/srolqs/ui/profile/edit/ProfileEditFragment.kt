package com.dl2lab.srolqs.ui.profile.edit

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dl2lab.srolqs.databinding.FragmentProfileEditBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import com.dl2lab.srolqs.utils.uriToFile
import com.dl2lab.srolqs.utils.reduceFileImage

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private var selectedImageUri: Uri? = null
    private var selectedImageFile: File? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            displaySelectedImage(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
            Toast.makeText(requireContext(), "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)

        setupViewModel()
        populateProfileFields()
        setupBatchDropdown()
        setupDegreeDropdown()

        // DatePicker setup
        binding.inputDob.setOnClickListener { showDatePicker() }
        binding.inputDobLayout.setEndIconOnClickListener { showDatePicker() }

        // Image picker setup
        binding.icon.setOnClickListener { startGallery() }

        // Save changes button
        binding.btnEdit.setOnClickListener { saveChanges() }
        binding.btnBack.setOnClickListener {     try {
            findNavController().popBackStack()
        } catch (e: IllegalStateException) {
            // Handle navigation failure
            activity?.onBackPressed()
        }}
        return binding.root
    }

    private fun showLoading(isLoading : Boolean){
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnEdit.isEnabled = !isLoading
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(ProfileViewModel::class.java)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun displaySelectedImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(binding.icon)
    }

    private fun populateProfileFields() {
        viewModel.getSession().observe(viewLifecycleOwner) { userModel ->
            binding.inputName.setText(userModel.nama ?: "")
            binding.inputDob.setText(formatTanggal(userModel.birthDate))
            binding.inputUniversity.setText(userModel.institution ?: "")
            binding.inputNpm.setText(userModel.identityNumber ?: "")
            // Menambahkan post delayed untuk memastikan adapter sudah siap
            binding.root.post {
                binding.inputBatch.setText(userModel.batch ?: "", false)
                binding.inputDegree.setText(userModel.degree ?: "", false)
            }

            // Load existing profile picture
            Glide.with(this)
                .load(userModel.profilePicture)
                .placeholder(com.dl2lab.srolqs.R.drawable.ic_launcher_background)
                .circleCrop()
                .into(binding.icon)
        }
    }

    private fun setupBatchDropdown() {
        val batchOptions = listOf("2024", "2023", "2022", "2021", "2020")
        val batchAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, batchOptions)
        binding.inputBatch.apply {
            setAdapter(batchAdapter)
            threshold = 0 // Akan menampilkan dropdown saat diklik tanpa perlu mengetik
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDropDown()
                }
            }
            setOnClickListener {
                showDropDown()
            }
        }
    }

    private fun setupDegreeDropdown() {
        val degreeOptions = listOf("Sarjana (S1)", "Magister (S2)", "Doktor (S3)", "Ahli Madya (D3)", "Sarjana Terapan (D4)")
        val degreeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, degreeOptions)
        binding.inputDegree.apply {
            setAdapter(degreeAdapter)
            threshold = 0 // Akan menampilkan dropdown saat diklik tanpa perlu mengetik
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDropDown()
                }
            }
            setOnClickListener {
                showDropDown()
            }
        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentDate = binding.inputDob.text.toString()

        if (currentDate.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val date = dateFormat.parse(currentDate)
                date?.let { calendar.time = it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.inputDob.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveChanges() {
        showLoading(true)
        val nama = binding.inputName.text.toString()
        val birthDate = binding.inputDob.text.toString()
        val institution = binding.inputUniversity.text.toString()
        val degree = binding.inputDegree.text.toString()
        val identityNumber = binding.inputNpm.text.toString()
        val batch = binding.inputBatch.text.toString()

        val namaBody = nama.toRequestBody("text/plain".toMediaTypeOrNull())
        val birthDateBody = birthDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val institutionBody = institution.toRequestBody("text/plain".toMediaTypeOrNull())
        val degreeBody = degree.toRequestBody("text/plain".toMediaTypeOrNull())
        val identityNumberBody = identityNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val batchBody = batch.toRequestBody("text/plain".toMediaTypeOrNull())

        val profilePicturePart = selectedImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())?.reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile?.path}")
            imageFile?.let {
                MultipartBody.Part.createFormData(
                    "profile_picture",
                    it.name,
                    it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
            }
        }

        viewModel.editProfile(
            nama = namaBody,
            birthDate = birthDateBody,
            institution = institutionBody,
            degree = degreeBody,
            identityNumber = identityNumberBody,
            batch = batchBody,
            profilePicture = profilePicturePart
        )

        viewModel.editProfileData.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful)
            {
                showLoading(false)
                requireContext().showCustomAlertDialog(
                    "Akun Berhasil Diperbarui",
                    "Informasi Anda telah berhasil diperbarui. Semua perubahan yang Anda buat kini sudah tersimpan",
                    "Lanjutkan",
                    "",
                    {
                    findNavController().popBackStack()
                    },
                    {
                    },
                    error = false
                )
            } else {
                showLoading(false)
                requireContext().showCustomAlertDialog(
                    "Akun Gagal Diperbarui",
                    "Maaf, terjadi kesalahan saat mengubah kegiatan. Silakan coba lagi atau periksa koneksi Anda",
                    "Coba lagi",
                    "",
                    {
                    },
                    {
                    },
                )
            }
        }
    }

    private fun formatTanggal(tanggal: String?): String {
        if (tanggal.isNullOrBlank()) return ""

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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
