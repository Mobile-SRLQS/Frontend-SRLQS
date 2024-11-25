package com.dl2lab.srolqs.ui.profile.faq

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.preference.FAQItem
import com.dl2lab.srolqs.databinding.FragmentFaqBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.adapter.FAQAdapter
import com.dl2lab.srolqs.ui.home.welcome.WelcomeActivity
import com.dl2lab.srolqs.ui.profile.viewmodel.ProfileViewModel


class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViewModel()
        checkUserSession()

        val faqList = listOf(
            FAQItem("Apa itu kuesioner Self-Regulated Learning?", "Kuesioner SRL adalah alat untuk menilai kemampuan seseorang dalam mengatur dan mengelola proses belajarnya secara mandiri, termasuk strategi dan motivasi belajar."),
            FAQItem("Apa tujuan dari kuesioner SRL ini?", "Kuesioner ini bertujuan untuk mengukur bagaimana seseorang mengatur tujuan, waktu, dan sumber daya belajarnya untuk meningkatkan efektivitas belajar."),
            FAQItem("Apakah hasil kuesioner ini bersifat rahasia?", "Ya, semua jawaban dijaga kerahasiaannya dan hanya digunakan untuk analisis dan pengembangan pembelajaran."),
            FAQItem("Bagaimana cara menafsirkan hasil kuesioner SRL?", "Hasilnya berupa skor yang mencerminkan seberapa efektif strategi dan pengelolaan belajar seseorang, dengan skor lebih tinggi menunjukkan kemampuan lebih baik."),
            FAQItem("Apakah kuesioner ini bisa digunakan untuk semua usia?", "Iya, kuesioner ini bisa digunakan untuk semua usia. SRL-QS ini dirancang untuk mengukur dan mengevaluasi  kemampuan belajar mandiri mahasiswa untuk meningkatkan performa akademiknya")
        )

        val faqAdapter = FAQAdapter(faqList)
        binding.faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.faqRecyclerView.adapter = faqAdapter

        // Set up Back Button
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return root
    }


    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(ProfileViewModel::class.java)
    }
    private fun checkUserSession() {
        viewModel.getSession().observe(viewLifecycleOwner, Observer { userModel ->
            if (!userModel.isLogin) {
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}