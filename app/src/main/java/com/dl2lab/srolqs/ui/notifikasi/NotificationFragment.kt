package com.dl2lab.srolqs.ui.notifikasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dl2lab.srolqs.data.room.database.AppDatabase
import com.dl2lab.srolqs.data.room.repository.NotificationRepository
import com.dl2lab.srolqs.databinding.FragmentNotificationBinding
import com.dl2lab.srolqs.ui.notifikasi.adapter.NotificationAdapter
import com.dl2lab.srolqs.ui.notifikasi.viewmodel.NotificationViewModel
import com.dl2lab.srolqs.ui.notifikasi.viewmodel.NotificationViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels {
        NotificationViewModelFactory(
            NotificationRepository(
                AppDatabase.getDatabase(requireContext()).notificationDao()
            )
        )
    }

    private val adapter = NotificationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeNotifications()
        return binding.root
    }


    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = this@NotificationFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun observeNotifications() {
        viewModel.allNotifications.observe(viewLifecycleOwner) { notifications ->
            adapter.submitList(notifications)
            binding.emptyView.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        adapter.onItemClick = { notification ->
            viewModel.markAsRead(notification.id)
        }

        binding.clearAllButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Clear All Notifications")
            .setMessage("Are you sure you want to delete all notifications?")
            .setPositiveButton("Clear") { _, _ ->
                viewModel.deleteAllNotifications()
            }.setNegativeButton("Cancel", null).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
