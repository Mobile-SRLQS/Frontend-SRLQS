package com.dl2lab.srolqs.ui.authentication.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.databinding.ActivityLoginBinding
import com.dl2lab.srolqs.databinding.ActivityRegisterBinding
import com.dl2lab.srolqs.ui.authentication.viewmodel.RegisterViewModel
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.login.LoginActivity
import com.dl2lab.srolqs.ui.customview.LoadingManager
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var role: String

    // Set up RegisterViewModel
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        role = intent.getStringExtra("role") ?: "Student"
        LoadingManager.init(this)
        observeViewModel()
        if(role == "Instructor"){
            hideInput()
        }
        // Set up the DatePickerDialog for Date of Birth input
        binding.inputDob.setOnClickListener { showDatePicker() }
        binding.inputDobLayout.setEndIconOnClickListener { showDatePicker() }

        // Set up dropdowns
        setUpBatchDropdown()
        setUpDegreeDropdown()

        // Handle Register button click
        binding.btnRegister.setOnClickListener { validateAndSubmitForm() }
    }

    private fun hideInput(){
        binding.tvBatchLabel.visibility = View.GONE
        binding.inputBatchLayout.visibility = View.GONE
        binding.inputBatch.visibility = View.GONE
        binding.tvNpmLabel.visibility = View.GONE
        binding.inputNpm.visibility = View.GONE
        binding.tvDegreeLabel.visibility = View.GONE
        binding.inputDegreeLayout.visibility = View.GONE
        binding.inputDegree.visibility = View.GONE
    }

    private fun observeViewModel() {
        registerViewModel.registerUser.observe(this) { response ->
            if (response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                showCustomAlertDialog(
                    "Registrasi Berhasil!",
                    "Selamat! Akun Anda telah berhasil dibuat. Anda sekarang dapat masuk dan mulai menggunakan aplikasi. Jangan ragu untuk menjelajahi berbagai fitur yang tersedia.",
                    "Masuk Akun",
                    "",
                    {
                        finish()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("role", role)
                        startActivity(intent)
                    },
                    {}
                )
            }
        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            // Show loading indicator if needed
            if (isLoading) {
                LoadingManager.show()
            } else {
                LoadingManager.hide()
            }
        }

        registerViewModel.errorMessageRegister.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.inputDob.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setUpBatchDropdown() {
        val batchYears = arrayOf("2024", "2023", "2022", "2021", "2020", "2019", "2018")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, batchYears)
        binding.inputBatch.setAdapter(adapter)
        binding.inputBatch.setOnClickListener { binding.inputBatch.showDropDown() }
        binding.inputBatch.setText(batchYears[0], false) // Set default to 2024
    }

    private fun setUpDegreeDropdown() {
        val degreeOptions = arrayOf("Bachelor (S1)", "Magister (S2)", "Doctorate (S3)", "Diploma (D3)", "Associate (D4)")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, degreeOptions)
        binding.inputDegree.setAdapter(adapter)
        binding.inputDegree.setOnClickListener { binding.inputDegree.showDropDown() }
        binding.inputDegree.setText(degreeOptions[0], false) // Set default to Bachelor (S1)
    }

    private fun validateAndSubmitForm() {
        val name = binding.inputName.text.toString()
        val email = binding.inputEmail.text.toString()
        val dob = binding.inputDob.text.toString()
        val university = binding.inputUniversity.text.toString()
        val batch = binding.inputBatch.text.toString()
        val identityNumber = binding.inputNpm.text.toString()
        val degree = binding.inputDegree.text.toString()
        val password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputPasswordConfirm.text.toString()

        if (role == "Student") {
            if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || university.isEmpty() || batch.isEmpty() || degree.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || identityNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || university.isEmpty()  || password.isEmpty() || confirmPassword.isEmpty() ) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return
            }
        }


        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birthDate = dateFormat.parse(dob).let { dateFormat.format(it) }

            if(role == "Student")
                registerViewModel.register(
                    nama = name,
                    birthDate = birthDate,
                    email = email,
                    password = password,
                    confirmedPassword = confirmPassword,
                    identityNumber = identityNumber,
                    batch = batch,
                    institution = university,
                    degree = degree,
                    role = role
                )
            else {
                registerViewModel.register(
                    nama = name,
                    birthDate = birthDate,
                    email = email,
                    password = password,
                    confirmedPassword = confirmPassword,
                    institution = university,
                    role = role
                )
            }


        } catch (e: Exception) {
            Log.e("RegisterActivity", "Date parsing error: ${e.message}")
            Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT).show()
        }
    }
}
