package com.dl2lab.srolqs.ui.authentication.register

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dl2lab.srolqs.databinding.FragmentRegisterEducationBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.authentication.viewmodel.RegisterViewModel
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog

class RegisterEducationFragment : Fragment() {

    private lateinit var binding: FragmentRegisterEducationBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var role: String = "Student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        role = arguments?.getString("role") ?: "Student"
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        binding = FragmentRegisterEducationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.academicInfo.value?.let { academicInfo ->
            binding.inputUniversity.setText(academicInfo.university)
            binding.inputBatch.setText(academicInfo.batch, false) // For dropdowns
            binding.inputNpm.setText(academicInfo.npm)
            binding.inputDegree.setText(academicInfo.degree, false) // For dropdowns
        }

        setUpBatchDropdown()
        setUpDegreeDropdown()

        if (role == "Instructor") hideInputFields()

        binding.btnRegister.setOnClickListener {
            if (validateFields()) {
                registerViewModel.setAcademicInfo(
                    university = binding.inputUniversity.text.toString(),
                    batch = binding.inputBatch.text.toString(),
                    npm = binding.inputNpm.text.toString(),
                    degree = binding.inputDegree.text.toString()
                )

                registerViewModel.submitRegistration()

                // Observe the registration response for success
                registerViewModel.registerUser.observe(viewLifecycleOwner) { response ->
                    if (!response.error) {
                        // Show success dialog
                        requireContext().showCustomAlertDialog(
                            title = "Registrasi Berhasil!",
                            subtitle = "Selamat! Akun Anda telah berhasil dibuat. Anda sekarang dapat masuk dan mulai menggunakan aplikasi.",
                            positiveButtonText = "Masuk Akun",
                            negativeButtonText = "",
                            onPositiveButtonClick = {
                                (activity as RegisterActivity).navigateToLogin()
                            },
                            onNegativeButtonClick = {}
                        )
                    }
                }

                // Observe the error message for failures
                registerViewModel.errorMessageRegister.observe(viewLifecycleOwner) { errorMessage ->
                    if (!errorMessage.isNullOrEmpty()) {
                        // Show failure dialog
                        requireContext().showCustomAlertDialog(
                            title = "Registrasi Gagal!",
                            subtitle = "Maaf, proses registrasi Anda tidak berhasil. Email sudah pernah digunakan.",
                            positiveButtonText = "Coba Lagi",
                            negativeButtonText = "",
                            onPositiveButtonClick = {
                                parentFragmentManager.popBackStack()
                            },
                            onNegativeButtonClick = {}
                        )
                    }
                }
            }
        }
    }

    private fun hideInputFields() {
        binding.tvBatchLabel.visibility = android.view.View.GONE
        binding.inputBatchLayout.visibility = android.view.View.GONE
        binding.tvNpmLabel.visibility = android.view.View.GONE
        binding.inputNpmLayout.visibility = android.view.View.GONE
        binding.tvDegreeLabel.visibility = android.view.View.GONE
        binding.inputDegreeLayout.visibility = android.view.View.GONE
    }

    private fun validateFields(): Boolean {
        if (role == "Instructor") return true

        val university = binding.inputUniversity.text.toString()
        val batch = binding.inputBatch.text.toString()
        val npm = binding.inputNpm.text.toString()
        val degree = binding.inputDegree.text.toString()

        if (university.isEmpty() || batch.isEmpty() || npm.isEmpty() || degree.isEmpty()) {
            Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setUpBatchDropdown() {
        val batchYears = arrayOf("2024", "2023", "2022", "2021", "2020", "2019", "2018")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, batchYears)
        binding.inputBatch.setAdapter(adapter)
    }

    private fun setUpDegreeDropdown() {
        val degreeOptions = arrayOf("Sarjana (S1)", "Magister (S2)", "Doktor (S3)", "Ahli Madya (D3)", "Sarjana Terapan (D4)")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, degreeOptions)
        binding.inputDegree.setAdapter(adapter)
    }
}
