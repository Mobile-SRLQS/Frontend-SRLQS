package com.dl2lab.srolqs.ui.kegiatan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dl2lab.srolqs.data.remote.response.KegiatanItem
import com.dl2lab.srolqs.databinding.ItemKegiatanBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KegiatanAdapter(
    private var listKegiatan: List<KegiatanItem?>,
    private val itemClickListener: OnKegiatanItemClickListener
) : RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder>() {

    interface OnKegiatanItemClickListener {
        fun onItemClick(kegiatanItem: KegiatanItem)
        fun onMarkAsDoneClick(idKegiatan: Int)
    }

    inner class KegiatanViewHolder(private val binding: ItemKegiatanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: KegiatanItem) {
            binding.tvNamaKegiatan.text = item.namaKegiatan
            val tenggat = formatTanggal(item.tenggat)
            binding.tvTenggatKegiatan.text = tenggat
            binding.tvJenisTugas.text = item.tipeKegiatan
            binding.tvCatatan.text = item.catatan
            binding

            if (item.isDone) {
                binding.btnMarkAsDone.text = "Diselesaikan"
                binding.btnMarkAsDone.isEnabled = false

            } else {
                binding.btnMarkAsDone.text = "Tandai Selesai"
                binding.btnMarkAsDone.isEnabled = true
                binding.btnMarkAsDone.setOnClickListener {
                    itemClickListener.onMarkAsDoneClick(item.id)
                }

            }


            binding.root.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanViewHolder {
        val binding =
            ItemKegiatanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KegiatanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KegiatanViewHolder, position: Int) {
        listKegiatan[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listKegiatan.size

    fun formatTanggal(tanggal: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val date: Date = inputFormat.parse(tanggal) ?: return tanggal
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            tanggal
        }
    }

    fun updateData(newList: List<KegiatanItem?>) {
        listKegiatan = newList
        notifyDataSetChanged()
    }
}