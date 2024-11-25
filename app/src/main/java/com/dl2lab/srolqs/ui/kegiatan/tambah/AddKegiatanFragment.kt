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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        setUpTipeKegiatanDropdown()
        setupReminderDropdown()

        profileViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let { token ->
                setupAddKegiatanListener(token)
            }
        })

        // AddKegiatanFragment.kt
        binding.btnTest.setOnClickListener {
            val title = "Gabing"
            val message = "This is a test notification"
            notificationHelper.scheduleTestNotification(title, message)
            Toast.makeText(
                context,
                "Test notification will appear in 10 seconds",
                Toast.LENGTH_SHORT
            ).show()
        }

        kegiatanViewModel.addKegiatanResult.observe(viewLifecycleOwner, Observer { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(requireContext(), "Kegiatan berhasil ditambah", Toast.LENGTH_SHORT).show()
                    saveActivity()
                    findNavController().navigate(R.id.action_addKegiatanFragment_to_navigation_kegiatan)
                },
                onFailure = {
                    Toast.makeText(requireContext(), "Gagal menambah kegiatan: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        })

        kegiatanViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        checkNotificationPermission()
    }

    private fun setupAddKegiatanListener(token: String) {
        binding.btnTambahKegiatan.setOnClickListener {
            addKegiatan(token)
        }
    }

    private fun addKegiatan(token: String) {
        val namaKegiatan = binding.inputName.text.toString()
        val tipeKegiatan = binding.inputTipeKegiatan.text.toString()
        val tenggat = binding.inputTanggalKegiatan.text.toString()
        val catatan = binding.inputCatatanKegiatan.text.toString()
        val link = binding.inputLinkKegiatan.text.toString()

        if (namaKegiatan.isEmpty() || tipeKegiatan.isEmpty() || tenggat.isEmpty() || catatan.isEmpty() || link.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

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
            "1 hari sebelumnya" -> 24 * 60 * 60 * 1000L
            "3 hari sebelumnya" -> 3 * 24 * 60 * 60 * 1000L
            "1 minggu sebelumnya" -> 7 * 24 * 60 * 60 * 1000L
            "1 bulan sebelumnya" -> 30L * 24 * 60 * 60 * 1000L
            else -> 60 * 60 * 1000L
        }
    }
    private fun scheduleNotification(activityTitle: String, deadlineTime: Long) {
        val selectedReminder = binding.inputReminder.text.toString()
        val reminderOffset = getReminderOffsetInMillis(selectedReminder)

        notificationHelper.scheduleNotification(
            title = "Activity Reminder",
            message = "Upcoming activity: $activityTitle",
            deadlineTime = deadlineTime,
            reminderOffset = reminderOffset
        )
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
            scheduleNotification(activityTitle, deadlineMilliSeconds)
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
}