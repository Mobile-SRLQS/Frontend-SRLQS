package com.dl2lab.srolqs.ui.kelas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.databinding.FragmentKelasBinding
import com.dl2lab.srolqs.ui.ViewModelFactory.ViewModelFactory
import com.dl2lab.srolqs.ui.home.adapter.ClassAdapter
import com.dl2lab.srolqs.ui.home.adapter.OnClassItemClickListener
import com.dl2lab.srolqs.ui.home.viewmodel.MainViewModel

class KelasFragment : Fragment(), OnClassItemClickListener {

    private var _binding: FragmentKelasBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelasBinding.inflate(inflater, container, false)
        setupViewModel()
        getClassList()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory.getInstance(requireContext())
        ).get(MainViewModel::class.java)
    }

    private fun getClassList() {
        viewModel.getListClass().observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                val classList = response.body()?.data ?: emptyList()
                if (classList.isEmpty()) {
                    showEmptyState()
                } else {
                    hideEmptyState()
                    setupRecyclerView(classList)
                }
            } else {
                // Handle error response if needed
                showEmptyState()
            }
        })
    }

    private fun setupRecyclerView(classList: List<DataItem?>) {
        val adapter = ClassAdapter(classList, this)
        binding.rvCourseList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourseList.adapter = adapter
    }

    private fun showEmptyState() {
        // Inflate the empty state ViewStub if it hasn't been inflated yet
        if (binding.viewstubEmptyKelasState.parent != null) {
            binding.viewstubEmptyKelasState.inflate()
        }
        binding.rvCourseList.visibility = View.GONE
    }

    private fun hideEmptyState() {
        // Hide the empty state layout if it exists and show the RecyclerView
        binding.viewstubEmptyKelasState.visibility = View.GONE
        binding.rvCourseList.visibility = View.VISIBLE
    }

    override fun onItemClick(classItem: DataItem) {
        val action = KelasFragmentDirections.actionNavigationMataKuliahToDetailClassFragment(classItem)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
