package com.dl2lab.srolqs.ui.kegiatan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.databinding.FragmentEnvironmentStructuringQuestionBinding
import com.dl2lab.srolqs.databinding.FragmentKegiatanBinding
import com.dl2lab.srolqs.ui.kuesioner.viewmodel.QuestionnaireViewModel

class KegiatanFragment : Fragment() {
    private lateinit var binding: FragmentKegiatanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKegiatanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the RecyclerView
        binding.rvKegiatan.visibility = View.GONE

        // Inflate the empty state ViewStub
        if (binding.viewstubEmptyState.parent != null) {
            binding.viewstubEmptyState.inflate()
        }
        binding.btnAddKegiatan.setOnClickListener{findNavController().navigate(R.id.action_navigation_kegiatan_to_addKegiatanFragment)}
    }
}