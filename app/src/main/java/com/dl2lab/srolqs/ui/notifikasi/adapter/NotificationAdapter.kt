package com.dl2lab.srolqs.ui.notifikasi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.R
import com.dl2lab.srolqs.data.room.entity.NotificationEntity
import com.dl2lab.srolqs.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// NotificationAdapter.kt
class NotificationAdapter : ListAdapter<NotificationEntity, NotificationAdapter.NotificationViewHolder>(
    NotificationDiffCallback()
) {
    var onItemClick: ((NotificationEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(notification: NotificationEntity) {
            binding.apply {
                tvTitle.text = notification.title
                tvMessage.text = notification.message
                tvTimestamp.text = formatDate(notification.timestamp)
                if (!notification.isRead) {
                    root.setBackgroundResource(R.drawable.ic_launcher_background)
                } else {
                    root.setBackgroundResource(R.drawable.ic_launcher_background)
                }
            }
        }

        private fun formatDate(timestamp: Long): String {
            return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(Date(timestamp))
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem == newItem
        }
    }
}
