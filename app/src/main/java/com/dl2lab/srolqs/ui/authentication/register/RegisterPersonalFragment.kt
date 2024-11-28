package com.dl2lab.srolqs.ui.authentication.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentRegisterPersonalBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.viewmodel.RegisterViewModel
import java.util.Calendar


class RegisterPersonalFragment : Fragment() {

    private lateinit var binding: FragmentRegisterPersonalBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var role: String = "Student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = arguments?.getString("role") ?: "Student"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.personalInfo.value?.let { personalInfo ->
            binding.inputName.setText(personalInfo.name)
            binding.inputEmail.setText(personalInfo.email)
            binding.inputDob.setText(personalInfo.dob)
            binding.inputPassword.setText(personalInfo.password)
            binding.inputPasswordConfirm.setText(personalInfo.confirmPassword)
        }

        setupPasswordInputListener()

        binding.inputDob.setOnClickListener { showDatePicker() }
        binding.inputDobLayout.setEndIconOnClickListener { showDatePicker() }

        binding.btnNext.setOnClickListener {
            if (validateFields()) {
                // Pass data to ViewModel
                registerViewModel.setPersonalInfo(
                    name = binding.inputName.text.toString(),
                    email = binding.inputEmail.text.toString(),
                    dob = binding.inputDob.text.toString(),
                    password = binding.inputPassword.text.toString(),
                    confirmPassword = binding.inputPasswordConfirm.text.toString(),
                    role = role
                )

                // Navigate to AcademicInfoFragment
                val academicInfoFragment = RegisterEducationFragment().apply {
                    arguments = Bundle().apply {
                        putString("role", role)
                    }
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, academicInfoFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun setupPasswordInputListener() {
        val passwordLayout = binding.inputPasswordLayout
        val passwordInput = binding.inputPassword
        val passwordRecommendation = binding.tvPasswordReccomendation

        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showPasswordRecommendation(passwordRecommendation)
            } else {
                hidePasswordRecommendation(passwordRecommendation)
            }
        }

        passwordInput.setOnClickListener {
            showPasswordRecommendation(passwordRecommendation)
        }
    }

    private fun showPasswordRecommendation(view: View) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            translationY = -20f // Mulai sedikit ke atas
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    private fun hidePasswordRecommendation(view: View) {
        view.animate()
            .alpha(0f)
            .translationY(-20f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                view.visibility = View.GONE
            }
            .start()
    }



    private fun validateFields(): Boolean {
        val name = binding.inputName.text.toString()
        val email = binding.inputEmail.text.toString()
        val dob = binding.inputDob.text.toString()
        val password = binding.inputPassword.text.toString()
        val confirmPassword = binding.inputPasswordConfirm.text.toString()

        if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Kata Sandi tidak cocok!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
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
}
