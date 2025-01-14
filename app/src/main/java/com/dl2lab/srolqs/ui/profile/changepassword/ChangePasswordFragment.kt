package com.dl2lab.srolqs.ui.profile.changepassword

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.remote.response.BasicResponse
import com.dl2lab.srolqs.databinding.FragmentChangePasswordBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.customview.showCustomAlertDialog
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel
import com.dl2lab.srolqs.utils.JwtUtils.isTokenExpired
import org.json.JSONObject
import retrofit2.Response


class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setupViewModel()
        checkUserSession()

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_changePasswordFragment_to_navigation_profile)

        }
        binding.btnSaveChanges.setOnClickListener {
            changePassword()
        }
        configureButton()
        return root
    }

    private fun showLoading(isLoading: Boolean){
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(ProfileViewModel::class.java)
    }

    private fun checkUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if (userModel.token != null) {
                if (isTokenExpired(userModel.token)) {
                    viewModel.logout()
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                    requireContext().showCustomAlertDialog(
                        "Sesi Anda Habis",
                        "Tolong masuk lagi dengan menggunakan akun yang sama.",
                        "Login",
                        "",
                        {},
                        {},
                    )
                }
            } else {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    private fun configureButton() {
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                val oldPassword = binding.inputOldPassword.text.toString()
                val newPassword = binding.inputNewPassword.text.toString()
                val confirmedPassword = binding.inputConfirmNewPassword.text.toString()
                binding.btnSaveChanges.isEnabled =
                    oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmedPassword.isNotEmpty()
            }
        }
        val oldPassword = binding.inputOldPassword.text.toString()
        val newPassword = binding.inputNewPassword.text.toString()
        val confirmedPassword = binding.inputConfirmNewPassword.text.toString()
        binding.inputOldPassword.addTextChangedListener(textWatcher)
        binding.inputNewPassword.addTextChangedListener(textWatcher)
        binding.inputConfirmNewPassword.addTextChangedListener(textWatcher)
        binding.btnSaveChanges.isEnabled =
            oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmedPassword.isNotEmpty()
    }

    private fun changePassword() {
        val oldPassword = binding.inputOldPassword.text.toString()
        val newPassword = binding.inputNewPassword.text.toString()
        val confirmedPassword = binding.inputConfirmNewPassword.text.toString()
        showLoading(true)
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmedPassword.isEmpty()) {
            showLoading(false)
            requireContext().showCustomAlertDialog(
                "Password Gagal Diubah",
                "Tolong masukkan password lama dan password baru Anda",
                "Coba Lagi",
                "",
                {},
                {
                    binding.btnBack.performClick()
                },
            )
            return
        }

        if (newPassword != confirmedPassword) {
            showLoading(false)

            requireContext().showCustomAlertDialog(
                "Password Gagal Diubah",
                "Kata sandi yang dimasukkan tidak sama. Tolong masukkan kata sandi yang sama pada kedua kolom.",
                "Coba Lagi",
                "",
                {},
                {
                    binding.btnBack.performClick()
                },
            )
            return
        }

        viewModel.changePassword(oldPassword, newPassword, confirmedPassword)
            .observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    showLoading(false)

                    val body = response.body()
                    if (body != null) {

                        requireContext().showCustomAlertDialog(
                            "Password Berhasil Diubah",
                            "Tetap perbaharui password secara berkala untuk mencegah pencurian data!",
                            "Kembali ke Profil",
                            "",
                            {
                                binding.btnBack.performClick()
                            },
                            {
                                binding.btnBack.performClick()
                            },
                            error = false
                        )
                    }
                } else {
                    showLoading(false)

                    requireContext().showCustomAlertDialog(
                        "Password Gagal Diubah",
                        extractErrorMessage(response),
                        "OK",
                        "",
                        {},
                        {
                            binding.btnBack.performClick()
                        },
                    )
                }

            }


    }

    private fun extractErrorMessage(response: Response<BasicResponse>): String {
        return try {
            val json = response.errorBody()?.string()
            val jsonObject = JSONObject(json)
            jsonObject.getString("message")
        } catch (e: Exception) {
            "Maaf, perubahan password Anda tidak berhasil. Silakan coba lagi atau hubungi dukungan jika masalah berlanjut"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}