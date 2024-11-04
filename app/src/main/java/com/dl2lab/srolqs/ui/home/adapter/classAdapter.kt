package com.dl2lab.srolqs.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.data.remote.response.DataItem
import com.dl2lab.srolqs.databinding.ItemClassBinding

class ClassAdapter(private val classList: List<DataItem?>, private val itemClickListener: OnClassItemClickListener) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(classItem: DataItem) {
            binding.tvClassname.text = classItem.className
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(classItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        classList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = classList.size
}