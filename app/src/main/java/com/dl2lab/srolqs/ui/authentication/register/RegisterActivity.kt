package com.dl2lab.srolqs.ui.authentication.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dl2lab.srolqs.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the DatePickerDialog when clicking on the Date of Birth input
        binding.inputDob.setOnClickListener {
            showDatePicker()
        }

        // Set up the DatePickerDialog when clicking on the calendar icon
        binding.inputDobLayout.setEndIconOnClickListener {
            showDatePicker()
        }

        // Set up the batch dropdown
        setUpBatchDropdown()
        setUpDegreeDropdown()
        binding.btnRegister.setOnClickListener {
            validateAndSubmitForm()
        }
    }

    // Function to show the DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date in SQL format (YYYY-MM-DD)
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.inputDob.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setUpBatchDropdown() {
        // Array of batch years in descending order
        val batchYears = arrayOf("2024", "2023", "2022", "2021", "2020", "2019", "2018")

        // Create an ArrayAdapter and attach it to the AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, batchYears)
        binding.inputBatch.setAdapter(adapter)

        // Show the dropdown when the AutoCompleteTextView is clicked
        binding.inputBatch.setOnClickListener {
            binding.inputBatch.showDropDown()
        }

        // Optional: Set a default value (if needed)
        binding.inputBatch.setText(batchYears[0], false) // Set default to 2024
    }

    // Function to set up the degree dropdown
    private fun setUpDegreeDropdown() {
        // Array of degree options
        val degreeOptions = arrayOf("Bachelor (S1)", "Magister (S2)", "Doctorate (S3)", "Diploma (D3)", "Associate (D4)")

        // Create an ArrayAdapter and attach it to the AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, degreeOptions)
        binding.inputDegree.setAdapter(adapter)

        // Show the dropdown when the AutoCompleteTextView is clicked
        binding.inputDegree.setOnClickListener {
            binding.inputDegree.showDropDown()
        }

        // Optional: Set a default value (if needed)
        binding.inputDegree.setText(degreeOptions[0], false) // Set default to Bachelor (S1)
    }

    // Function to validate the form and show a toast/log the input
    private fun validateAndSubmitForm() {
        // Get user input
        val name = binding.inputName.text.toString()
        val email = binding.inputEmail.text.toString()
        val dob = binding.inputDob.text.toString()
        val university = binding.inputUniversity.text.toString()
        val batch = binding.inputBatch.text.toString()
        val degree = binding.inputDegree.text.toString()
        val password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputPasswordConfirm.text.toString()

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || university.isEmpty() || batch.isEmpty() || degree.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if password and confirm password match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        // Log the user input to Logcat
        Log.d("RegisterActivity", "Name: $name")
        Log.d("RegisterActivity", "Email: $email")
        Log.d("RegisterActivity", "Date of Birth: $dob")
        Log.d("RegisterActivity", "University: $university")
        Log.d("RegisterActivity", "Batch: $batch")
        Log.d("RegisterActivity", "Degree: $degree")
        Log.d("RegisterActivity", "Password: $password") // Avoid logging passwords in production apps

        // Show a success toast
        Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_SHORT).show()
    }

}
