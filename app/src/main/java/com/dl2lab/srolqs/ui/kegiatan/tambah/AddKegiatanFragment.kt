package com.dl2lab.srolqs.ui.kegiatan.tambah

import KegiatanViewModelFactory
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.dl2lab.srolqs.utils.NotificationHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.worker.QuestionnaireNotificationWorker
import com.dl2lab.srolqs.worker.TodoNotificationWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.TimeUnit

class AddKegiatanFragment : Fragment() {
    private val REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1001

    private lateinit var binding: FragmentAddKegiatanBinding
    private lateinit var notificationHelper: NotificationHelper

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

        binding.inputTanggalKegiatan.setOnClickListener { showDatePicker() }
        binding.inputTanggalKegiatanLayout.setEndIconOnClickListener { showDatePicker() }
        notificationHelper = NotificationHelper(requireContext())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM), REQUEST_CODE_SCHEDULE_EXACT_ALARM)
            }
        }
        LoadingManager.init(this)
        setUpTipeKegiatanDropdown()
        setupReminderDropdown()
        setupTextWatchers()
        validateInputs()

        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                setupAddKegiatanListener(token)
            }
        })

//        // AddKegiatanFragment.kt
//        binding.btnTest.setOnClickListener {
//            val title = "Gabing"
//            val message = "This is a test notification"
//            notificationHelper.scheduleTestNotification(title, message)
//            Toast.makeText(
//                context,
//                "Test notification will appear in 10 seconds",
//                Toast.LENGTH_SHORT
//            ).show()
//        }

        kegiatanViewModel.addKegiatanResult.observe(viewLifecycleOwner, Observer { result ->
            result.fold(
                onSuccess = {
                    requireContext().showCustomAlertDialog(
                        title = "Tambah Kegiatan Berhasil",
                        subtitle = "Kegiatan \'${it.data.namaKegiatan}\' berhasil ditambah",
                        positiveButtonText = "OK",
                        negativeButtonText = "",
                        onPositiveButtonClick ={
                            saveActivity()
                            findNavController().navigate(R.id.action_addKegiatanFragment_to_navigation_kegiatan)},
                        onNegativeButtonClick = {},
                        error =false,
                    )


                },
                onFailure = {
                    requireContext().showCustomAlertDialog(
                        title = "Tambah Kegiatan Gagal",
                        subtitle = it.message ?: "Terjadi kesalahan saat menambah kegiatan",
                        positiveButtonText = "OK",
                        negativeButtonText = "",
                        onPositiveButtonClick ={
                            saveActivity()
                            findNavController().navigate(R.id.action_addKegiatanFragment_to_navigation_kegiatan)},
                        onNegativeButtonClick = {},
                        error =true,
                    )
                }
            )
        })

        kegiatanViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
           if (isLoading) {
               LoadingManager.show()
           } else {
               LoadingManager.hide()
           }
        })

        checkNotificationPermission()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                validateInputs()
            }
        }

        // Add TextWatcher to all EditText fields
        binding.inputName.addTextChangedListener(textWatcher)
        binding.inputTipeKegiatan.addTextChangedListener(textWatcher)
        binding.inputTanggalKegiatan.addTextChangedListener(textWatcher)
        binding.inputCatatanKegiatan.addTextChangedListener(textWatcher)
        binding.inputLinkKegiatan.addTextChangedListener(textWatcher)
        binding.inputReminder.addTextChangedListener(textWatcher)
    }

    private fun setupAddKegiatanListener(token: String) {
        binding.btnTambahKegiatan.setOnClickListener {
            addKegiatan(token)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun validateInputs() {
        val namaKegiatan = binding.inputName.text.toString()
        val tipeKegiatan = binding.inputTipeKegiatan.text.toString()
        val tenggat = binding.inputTanggalKegiatan.text.toString()
        val catatan = binding.inputCatatanKegiatan.text.toString()
        val link = binding.inputLinkKegiatan.text.toString()
        val reminder = binding.inputReminder.text.toString()

        val isValid = namaKegiatan.isNotEmpty() &&
                tipeKegiatan.isNotEmpty() &&
                tenggat.isNotEmpty() &&
                catatan.isNotEmpty() &&
                link.isNotEmpty() &&
                reminder.isNotEmpty()

        binding.btnTambahKegiatan.isEnabled = isValid

        // Optional: Change button appearance when disabled
        binding.btnTambahKegiatan.alpha = if (isValid) 1.0f else 0.5f
    }

    private fun addKegiatan(token: String) {
        val namaKegiatan = binding.inputName.text.toString()
        val tipeKegiatan = binding.inputTipeKegiatan.text.toString()
        val tenggat = binding.inputTanggalKegiatan.text.toString()
        val catatan = binding.inputCatatanKegiatan.text.toString()
        val link = binding.inputLinkKegiatan.text.toString()

        scheduleNotification()
        val request = TambahKegiatanRequest(
            namaKegiatan = namaKegiatan,
            tipeKegiatan = tipeKegiatan,
            tenggat = tenggat,
            catatan = catatan,
            link = link
        )

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
        binding.inputTipeKegiatan.setText(tipeKegiatanOptions[0], false)
    }

    private fun setupReminderDropdown() {
        val reminderOptions = arrayOf(
            "1 hari sebelumnya",
            "3 hari sebelumnya",
            "1 minggu sebelumnya",
            "1 bulan sebelumnya"
        )

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, reminderOptions)
        (binding.inputReminder as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
    }




    private fun getReminderOffsetInMillis(reminderOption: String): Long {
        return when (reminderOption) {
            "1 hari sebelumnya" -> 24 * 60
            "3 hari sebelumnya" -> 3 * 24 * 60
            "1 minggu sebelumnya" -> 7 * 24 * 60
            "1 bulan sebelumnya" -> 30L * 24 * 60
            else -> 60 * 60 * 1000L
        }
    }
    private fun scheduleNotification() {
        val namaKegiatan = binding.inputName.text.toString()
        var tenggat = binding.inputTanggalKegiatan.text.toString()
        val selectedReminder = binding.inputReminder.text.toString()

        val reminderOffset = getReminderOffsetInMillis(selectedReminder)
        val tenggatMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tenggat)?.time ?: 0
        val currentMillis = System.currentTimeMillis()
        var delay =  tenggatMillis - reminderOffset - currentMillis

        if (delay <= 0) {
            delay = 1
        }

        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInputData(
                workDataOf(
                    "nama_kegiatan" to namaKegiatan,
                    "tenggat" to tenggat
                )
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }

    private fun convertDateToMillis(dateStr: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateStr)
            date?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            Log.e("AddKegiatanFragment", "Error parsing date: ${e.message}")
            System.currentTimeMillis()
        }
    }

    private fun saveActivity() {
        val deadlineTime = binding.inputTanggalKegiatan.text.toString()
        val activityTitle = binding.inputName.text.toString()
        val deadlineMilliSeconds = convertDateToMillis(deadlineTime)

        if (binding.inputReminder.text.isNotEmpty()) {
            scheduleNotification()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showPermissionExplanationDialog()
        }
    }

    private fun showPermissionExplanationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Notification Permission")
            .setMessage("Enable notifications to receive reminders for your activities.")
            .setPositiveButton("Enable") { _, _ ->
                checkNotificationPermission()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                return
            } else {
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LoadingManager.cleanup()
    }
}